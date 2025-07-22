// static/js/modules/api-service.js
export class ApiService {
  constructor() {
    this.baseUrl = '/api/v1';
  }

  getAuthHeaders() {
    const token = sessionStorage.getItem('access_token');
    return token ? { 'Authorization': `Bearer ${token}` } : {};
  }

  async request(endpoint, options = {}) {
    const url = `${this.baseUrl}${endpoint}`;
    const config = {
      headers: {
        'Content-Type': 'application/json',
        ...this.getAuthHeaders(),
        ...options.headers
      },
      ...options
    };

    try {
      const response = await fetch(url, config);
      
      if (!response.ok) {
        throw new Error(`HTTP ${response.status}: ${response.statusText}`);
      }

      const contentLength = response.headers.get('content-length');
      if (contentLength === '0') {
        return null;
      }

      const contentType = response.headers.get('content-type');
      if (contentType && contentType.includes('application/json')) {
        return response.json();
      }
      
      return response.text();
    } catch (error) {
      console.error(`API Error [${options.method || 'GET'}] ${url}:`, error);
      throw error;
    }
  }

  async fetchAddresses() {
    return this.request('/addresses');
  }

  async fetchNotes(addressId) {
    return this.request(`/notes/${addressId}`);
  }

  async fetchAllNotes() {
    return this.request('/notes');
  }

  async createNote(addressId, text, createdBy) {
    return this.request(`/notes/${addressId}`, {
      method: 'POST',
      body: JSON.stringify({ text, createdBy })
    });
  }

  async updateNote(noteId, text) {
    return this.request(`/notes/note/${noteId}`, {
      method: 'PUT',
      body: JSON.stringify({ text })
    });
  }

  async deleteNote(noteId) {
    return this.request(`/notes/note/${noteId}`, {
      method: 'DELETE'
    });
  }

  async importFile(file, onProgress = null) {
    const formData = new FormData();
    formData.append('file', file);
    
    const config = {
      method: 'POST',
      headers: {
        ...this.getAuthHeaders()
      },
      body: formData
    };

    if (onProgress && typeof onProgress === 'function') {
      return this.requestWithProgress(`${this.baseUrl}/import`, config, onProgress);
    }

    const response = await fetch(`${this.baseUrl}/import`, config);
    
    if (!response.ok) {
      throw new Error(`Import failed: HTTP ${response.status}`);
    }
    
    return response.json();
  }

  requestWithProgress(url, config, onProgress) {
    return new Promise((resolve, reject) => {
      const xhr = new XMLHttpRequest();
      
      xhr.upload.addEventListener('progress', (e) => {
        if (e.lengthComputable) {
          onProgress({
            loaded: e.loaded,
            total: e.total,
            percentage: Math.round((e.loaded * 100) / e.total)
          });
        }
      });

      xhr.addEventListener('load', () => {
        if (xhr.status >= 200 && xhr.status < 300) {
          try {
            resolve(JSON.parse(xhr.responseText));
          } catch (e) {
            resolve(xhr.responseText);
          }
        } else {
          reject(new Error(`HTTP ${xhr.status}: ${xhr.statusText}`));
        }
      });

      xhr.addEventListener('error', () => {
        reject(new Error('Network error occurred'));
      });

      xhr.open(config.method, url);
      
      Object.entries(config.headers).forEach(([key, value]) => {
        xhr.setRequestHeader(key, value);
      });

      xhr.send(config.body);
    });
  }

  async fetchWeather(params) {
    const query = new URLSearchParams(params).toString();
    return this.request(`/weather?${query}`);
  }

  isAuthenticated() {
    return !!sessionStorage.getItem('access_token');
  }

  getUsername() {
    return sessionStorage.getItem('username') || 'User';
  }

  async healthCheck() {
    try {
      const response = await fetch('/actuator/health');
      return response.ok;
    } catch {
      return false;
    }
  }
}
