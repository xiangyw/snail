/**
 * 格式化日期时间
 * @param date - 日期字符串或Date对象
 * @returns 格式化后的日期字符串
 */
export function formatDate(date: string | Date | undefined): string {
  if (!date) {
    return '';
  }
  
  const dateObj = typeof date === 'string' ? new Date(date) : date;
  
  // 如果是今天，显示具体时间
  const today = new Date();
  const isToday = dateObj.toDateString() === today.toDateString();
  
  if (isToday) {
    return `${padZero(dateObj.getHours())}:${padZero(dateObj.getMinutes())}`;
  }
  
  // 如果是今年，不显示年份
  const isThisYear = dateObj.getFullYear() === today.getFullYear();
  
  if (isThisYear) {
    return `${padZero(dateObj.getMonth() + 1)}-${padZero(dateObj.getDate())}`;
  }
  
  // 显示完整日期
  return `${dateObj.getFullYear()}-${padZero(dateObj.getMonth() + 1)}-${padZero(dateObj.getDate())}`;
}

/**
 * 补零函数
 */
function padZero(num: number): string {
  return num.toString().padStart(2, '0');
}

/**
 * 格式化数字（如：1000 -> 1K, 1000000 -> 1M）
 */
export function formatNumber(num: number): string {
  if (num >= 1000000) {
    return `${(num / 1000000).toFixed(1)}M`;
  }
  if (num >= 1000) {
    return `${(num / 1000).toFixed(1)}K`;
  }
  return num.toString();
}

/**
 * 截取文本
 */
export function truncateText(text: string, maxLength: number): string {
  if (!text) return '';
  return text.length > maxLength ? text.substring(0, maxLength) + '...' : text;
}