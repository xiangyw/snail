#!/usr/bin/env python3
"""
XMind 文件解析脚本
XMind 文件是 ZIP 格式，包含 content.json
"""

import zipfile
import json
import os
import glob

def parse_xmind_file(filepath):
    """解析单个 XMind 文件"""
    print(f"\n{'='*60}")
    print(f"解析文件：{filepath}")
    print('='*60)
    
    try:
        with zipfile.ZipFile(filepath, 'r') as z:
            # 读取 content.json
            if 'content.json' in z.namelist():
                with z.open('content.json') as f:
                    content = json.load(f)
                    return content
            else:
                print("警告：未找到 content.json")
                return None
    except Exception as e:
        print(f"错误：{e}")
        return None

def print_topic_tree(topic, level=0, max_depth=5):
    """打印主题树结构"""
    if not topic or level > max_depth:
        return
    
    if isinstance(topic, dict):
        title = topic.get('title', '')
        indent = "  " * level
        print(f"{indent}├── {title}")
        
        children = topic.get('children', {})
        attached = children.get('attached', []) if isinstance(children, dict) else []
        for child in attached:
            print_topic_tree(child, level + 1, max_depth)
    elif isinstance(topic, list):
        for item in topic:
            print_topic_tree(item, level, max_depth)

def extract_all_topics(topic, path=""):
    """递归提取所有主题"""
    results = []
    
    if isinstance(topic, dict):
        title = topic.get('title', '')
        current_path = f"{path}/{title}" if path else title
        
        results.append({
            'path': current_path,
            'title': title,
            'topic': topic
        })
        
        children = topic.get('children', {})
        attached = children.get('attached', []) if isinstance(children, dict) else []
        for child in attached:
            results.extend(extract_all_topics(child, current_path))
    
    elif isinstance(topic, list):
        for item in topic:
            results.extend(extract_all_topics(item, path))
    
    return results

def main():
    base_dir = "/home/openclaw/.openclaw/workspace/projects/snail/docs/requirements"
    
    # 查找所有 XMind 文件
    xmind_files = glob.glob(os.path.join(base_dir, "*.xmind"))
    print(f"找到的 XMind 文件：{xmind_files}")
    
    all_content = {}
    all_topics = {}
    
    for filepath in xmind_files:
        filename = os.path.basename(filepath)
        content = parse_xmind_file(filepath)
        if content:
            print(f"\n内容类型：{type(content)}")
            
            # 处理不同的内容结构
            if isinstance(content, list):
                print(f"根元素是列表，长度：{len(content)}")
                root_topics = content
            elif isinstance(content, dict):
                print(f"根元素是字典")
                root_topics = content.get('rootTopics', content.get('sheet', {}).get('rootTopic', []))
                if not root_topics and 'sheet' in content:
                    root_topics = [content['sheet'].get('rootTopic', {})]
            else:
                root_topics = [content]
            
            if root_topics:
                print(f"\n\n根主题结构:")
                for i, root in enumerate(root_topics):
                    if isinstance(root, dict):
                        title = root.get('title', f'Root {i}')
                        print(f"\n【{title}】")
                        print_topic_tree(root, max_depth=4)
                        
                        # 提取所有主题
                        topics = extract_all_topics(root)
                        all_topics[filename] = topics
            
            all_content[filename] = content
    
    # 保存解析结果
    output_file = os.path.join(base_dir, "parsed_content.json")
    with open(output_file, 'w', encoding='utf-8') as f:
        json.dump(all_content, f, ensure_ascii=False, indent=2)
    
    # 保存提取的主题
    topics_file = os.path.join(base_dir, "extracted_topics.json")
    with open(topics_file, 'w', encoding='utf-8') as f:
        json.dump(all_topics, f, ensure_ascii=False, indent=2)
    
    print(f"\n\n解析结果已保存到：{output_file}")
    print(f"主题提取已保存到：{topics_file}")
    return all_content, all_topics

if __name__ == "__main__":
    main()
