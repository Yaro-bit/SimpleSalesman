// static/js/modules/utils.js
export class Utils {
  static extractPlz(addr) {
    if (!addr) return '';
    return addr.postalCode || addr.plz || this.extractFromText(addr.addressText, /\b\d{4,5}\b/);
  }

  static extractStreet(addr) {
    if (!addr) return '';
    if (addr.street) return addr.street;
    if (addr.addressText) {
      const match = addr.addressText.match(/^([^,\d]+)/);
      return match ? match[1].trim() : addr.addressText.split(',')[0].trim();
    }
    return '';
  }

  static extractNumber(addr) {
    if (!addr) return '';
    return addr.houseNumber || this.extractFromText(addr.addressText, /\d+[a-zA-Z]*/);
  }

  static extractFromText(text, regex) {
    if (!text) return '';
    const match = text.match(regex);
    return match ? match[0] : '';
  }

  static getAddressString(addr) {
    if (!addr) return '-';
    const plz = this.extractPlz(addr);
    const street = this.extractStreet(addr);
    const number = this.extractNumber(addr);
    return `${plz} ${street} ${number}`.trim();
  }

  static showNotification(title, body, type = 'info') {
    if ('Notification' in window && Notification.permission === 'granted') {
      const notification = new Notification(title, {
        body,
        icon: '/logo_saleman_original.png',
        tag: `simplesalesman-${type}`,
        requireInteraction: type === 'error'
      });

      if (type !== 'error') {
        setTimeout(() => notification.close(), 5000);
      }

      return notification;
    }
    
    console.log(`${title}: ${body}`);
    return null;
  }

  static async requestNotificationPermission() {
    if (!('Notification' in window)) {
      console.warn('This browser does not support desktop notifications');
      return false;
    }

    if (Notification.permission === 'granted') {
      return true;
    }

    if (Notification.permission !== 'denied') {
      const permission = await Notification.requestPermission();
      return permission === 'granted';
    }

    return false;
  }

  static formatDate(dateString, options = {}) {
    if (!dateString) return '-';
    
    const defaultOptions = {
      year: 'numeric',
      month: '2-digit',
      day: '2-digit',
      hour: '2-digit',
      minute: '2-digit',
      ...options
    };

    try {
      return new Date(dateString).toLocaleString('de-DE', defaultOptions);
    } catch (error) {
      console.error('Date formatting error:', error);
      return dateString;
    }
  }

  static formatDateShort(dateString) {
    return this.formatDate(dateString, { 
      year: 'numeric', 
      month: '2-digit', 
      day: '2-digit' 
    });
  }

  static formatFileSize(bytes) {
    if (bytes === 0) return '0 Bytes';
    
    const k = 1024;
    const sizes = ['Bytes', 'KB', 'MB', 'GB'];
    const i = Math.floor(Math.log(bytes) / Math.log(k));
    
    return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i];
  }

  static getFileExtension(filename) {
    return filename.slice((filename.lastIndexOf('.') - 1 >>> 0) + 2).toLowerCase();
  }

  static isValidExcelFile(file) {
    const validExtensions = ['xlsx', 'xls'];
    const extension = this.getFileExtension(file.name);
    return validExtensions.includes(extension);
  }

  static createElement(tag, className = '', innerHTML = '') {
    const element = document.createElement(tag);
    if (className) element.className = className;
    if (innerHTML) element.innerHTML = innerHTML;
    return element;
  }

  static sanitizeHtml(str) {
    const temp = document.createElement('div');
    temp.textContent = str;
    return temp.innerHTML;
  }

  static debounce(func, wait, immediate = false) {
    let timeout;
    return function executedFunction(...args) {
      const later = () => {
        timeout = null;
        if (!immediate) func.apply(this, args);
      };
      const callNow = immediate && !timeout;
      clearTimeout(timeout);
      timeout = setTimeout(later, wait);
      if (callNow) func.apply(this, args);
    };
  }

  static async requestWakeLock() {
    if ('wakeLock' in navigator) {
      try {
        const wakeLock = await navigator.wakeLock.request('screen');
        console.log('Wake lock acquired');
        
        wakeLock.addEventListener('release', () => {
          console.log('Wake lock released');
        });
        
        return wakeLock;
      } catch (err) {
        console.warn('Failed to acquire wake lock:', err);
        return null;
      }
    }
    console.warn('Wake Lock API not supported');
    return null;
  }

  static releaseWakeLock(wakeLock) {
    if (wakeLock) {
      try {
        wakeLock.release();
        return true;
      } catch (error) {
        console.error('Error releasing wake lock:', error);
        return false;
      }
    }
    return false;
  }

  static createErrorAlert(message, type = 'danger') {
    const alert = this.createElement('div', `alert alert-${type} alert-dismissible fade show`);
    alert.innerHTML = `
      <i class="bi bi-exclamation-triangle-fill me-2"></i>
      ${this.sanitizeHtml(message)}
      <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
    `;
    return alert;
  }

  static createSuccessAlert(message) {
    return this.createErrorAlert(message, 'success');
  }

  static getProjectStatusClass(status) {
    switch (status?.toLowerCase()) {
      case 'completed': return 'status-completed';
      case 'pending': return 'status-pending';  
      case 'cancelled': return 'status-cancelled';
      default: return '';
    }
  }

  static getProjectStatusIcon(status) {
    switch (status?.toLowerCase()) {
      case 'completed': return 'bi-check-circle-fill';
      case 'pending': return 'bi-clock-fill';
      case 'cancelled': return 'bi-x-circle-fill';
      default: return 'bi-question-circle-fill';
    }
  }

  static normalizeSearchText(text) {
    return text.toLowerCase()
      .normalize('NFD')
      .replace(/[\u0300-\u036f]/g, '')
      .trim();
  }

  static groupBy(array, key) {
    return array.reduce((groups, item) => {
      const value = typeof key === 'function' ? key(item) : item[key];
      if (!groups[value]) {
        groups[value] = [];
      }
      groups[value].push(item);
      return groups;
    }, {});
  }

  static sortBy(array, key, order = 'asc') {
    return [...array].sort((a, b) => {
      const aVal = typeof key === 'function' ? key(a) : a[key];
      const bVal = typeof key === 'function' ? key(b) : b[key];
      
      if (aVal === bVal) return 0;
      
      const comparison = aVal < bVal ? -1 : 1;
      return order === 'asc' ? comparison : -comparison;
    });
  }
}
