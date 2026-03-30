#!/usr/bin/env python3
"""
XMind 需求分析脚本 - 提取完整需求结构
"""

import zipfile
import json
import os
import glob

def parse_xmind_file(filepath):
    """解析单个 XMind 文件"""
    try:
        with zipfile.ZipFile(filepath, 'r') as z:
            if 'content.json' in z.namelist():
                with z.open('content.json') as f:
                    content = json.load(f)
                    return content
    except Exception as e:
        print(f"错误 {filepath}: {e}")
    return None

def extract_topics_recursive(topic, parent_path=""):
    """递归提取所有主题"""
    results = []
    
    if isinstance(topic, dict):
        title = topic.get('title', '')
        current_path = f"{parent_path} > {title}" if parent_path else title
        
        # 提取主题信息
        topic_info = {
            'path': current_path,
            'title': title,
            'id': topic.get('id', ''),
        }
        
        # 提取备注/注释
        if 'notes' in topic:
            topic_info['notes'] = topic['notes']
        
        # 提取标签
        if 'labels' in topic:
            topic_info['labels'] = topic['labels']
        
        results.append(topic_info)
        
        # 递归处理子主题
        children = topic.get('children', {})
        if isinstance(children, dict):
            attached = children.get('attached', [])
            for child in attached:
                results.extend(extract_topics_recursive(child, current_path))
    
    elif isinstance(topic, list):
        for item in topic:
            results.extend(extract_topics_recursive(item, parent_path))
    
    return results

def analyze_file(filepath):
    """分析单个 XMind 文件"""
    filename = os.path.basename(filepath)
    print(f"\n{'='*60}")
    print(f"分析：{filename}")
    print('='*60)
    
    content = parse_xmind_file(filepath)
    if not content:
        return None
    
    # 提取根主题
    if isinstance(content, list) and len(content) > 0:
        sheet = content[0]
        root_topic = sheet.get('rootTopic', {})
    elif isinstance(content, dict):
        root_topic = content.get('rootTopic', {})
    else:
        root_topic = {}
    
    # 提取所有主题
    all_topics = extract_topics_recursive(root_topic)
    
    print(f"\n共提取 {len(all_topics)} 个主题节点")
    
    # 按层级分组
    by_level = {}
    for topic in all_topics:
        level = topic['path'].count(' > ')
        if level not in by_level:
            by_level[level] = []
        by_level[level].append(topic)
    
    print("\n层级结构:")
    for level in sorted(by_level.keys()):
        print(f"  L{level}: {len(by_level[level])} 个节点")
    
    return {
        'filename': filename,
        'topics': all_topics,
        'root_title': root_topic.get('title', '')
    }

def main():
    base_dir = "/home/openclaw/.openclaw/workspace/projects/snail/docs/requirements"
    xmind_files = glob.glob(os.path.join(base_dir, "*.xmind"))
    
    print(f"找到 {len(xmind_files)} 个 XMind 文件")
    
    all_analysis = {}
    for filepath in xmind_files:
        result = analyze_file(filepath)
        if result:
            all_analysis[result['filename']] = result
    
    # 保存分析结果
    output_file = os.path.join(base_dir, "analysis_result.json")
    with open(output_file, 'w', encoding='utf-8') as f:
        json.dump(all_analysis, f, ensure_ascii=False, indent=2)
    
    print(f"\n分析结果已保存到：{output_file}")
    
    # 打印关键需求概览
    print("\n\n" + "="*60)
    print("需求概览")
    print("="*60)
    
    for filename, data in all_analysis.items():
        print(f"\n【{filename}】")
        topics = data['topics']
        
        # 打印第一级和第二级主题
        level1 = [t for t in topics if t['path'].count(' > ') == 0]
        level2 = [t for t in topics if t['path'].count(' > ') == 1]
        
        print(f"  一级分类 ({len(level1)}):")
        for t in level1[:10]:
            print(f"    - {t['title']}")
        
        print(f"  二级功能 ({len(level2)}):")
        for t in level2[:20]:
            print(f"    - {t['title']}")
    
    return all_analysis

if __name__ == "__main__":
    main()
