// static/js/main.js - Application Entry Point
import { AppState } from './modules/app-state.js';
import { ApiService } from './modules/api-service.js';
import { Utils } from './modules/utils.js';
import { AddressManager } from './modules/address-manager.js';
import { NotesManager } from './modules/notes-manager.js';
import { TableManager } from './modules/table-manager.js';
import { ModalManager } from './modules/modal-manager.js';
import { ImportManager } from './modules/import-manager.js';
import { AuthManager } from './modules/auth-manager.js';

class SimpleSalesmanApp {
  constructor() {
    this.initialized = false;
    this.managers = {};
    this.state = null;
  }

  async initialize() {
    if (this.initialized) {
      console.warn('App already initialized');
      return;
    }

    try {
      console.log('Initializing SimpleSalesman Application...');
      
      this.initializeCoreServices();
      this.setupManagerRelationships();
      this.exposeGlobalAPI();
      this.setupStateSubscriptions();
      this.setupEventHandlers();
      await this.loadInitialData();
      this.initializeFeatures();
      
      this.initialized = true;
      console.log('SimpleSalesman Application initialized successfully');
      
      window.dispatchEvent(new CustomEvent('app:initialized'));
      
    } catch (error) {
      console.error('Failed to initialize application:', error);
      this.handleInitializationError(error);
    }
  }

  initializeCoreServices() {
    this.state = new AppState();
    this.managers.api = new ApiService();
    this.managers.auth = new AuthManager(this.managers.api, this.state);
    
    this.managers.address = new AddressManager(this.managers.api, this.state);
    this.managers.notes = new NotesManager(this.managers.api, this.state, this.managers.address);
    this.managers.table = new TableManager(this.state, this.managers.address);
    this.managers.modal = new ModalManager(
      this.managers.api, 
      this.state, 
      this.managers.address, 
      this.managers.notes
    );
    this.managers.import = new ImportManager(this.managers.api, this.state);
  }

  setupManagerRelationships() {
    this.managers.address.setTableManager(this.managers.table);
    
    if (this.managers.modal.setupKeyboardShortcuts) {
      this.managers.modal.setupKeyboardShortcuts();
    }
  }

  exposeGlobalAPI() {
    window.app = this;
    window.addressManager = this.managers.address;
    window.notesManager = this.managers.notes;
    window.tableManager = this.managers.table;
    window.modalManager = this.managers.modal;
    window.importManager = this.managers.import;
    window.authManager = this.managers.auth;
    window.Utils = Utils;
  }

  setupStateSubscriptions() {
    this.state.subscribe('addresses:updated', () => {
      if (this.managers.table) {
        this.managers.table.render('addresses');
      }
    });

    this.state.subscribe('notes:updated', () => {
      if (this.managers.table) {
        this.managers.table.render('notes');
      }
    });

    this.state.subscribe('view:changed', (view) => {
      this.updateViewButtons(view);
    });

    if (this.isDebugMode()) {
      this.state.subscribe('*', (event, data) => {
        console.debug(`State event: ${event}`, data);
      });
    }
  }

  setupEventHandlers() {
    window.showSection = (sectionName) => this.showSection(sectionName);
    window.showAddressView = (view) => this.managers.address.showAddressView(view);
    window.sortTable = (type, field) => this.managers.table.sortTable(type, field);
    window.filterTable = (type) => this.filterTable(type);
    window.changePageSize = (type) => this.changePageSize(type);
    window.goBack = () => this.managers.address.goBack();
    window.logout = () => this.managers.auth.logout();
    window.toggleSidebar = () => this.toggleSidebar();
    window.requestNotificationPermission = () => Utils.requestNotificationPermission();
  }

  async loadInitialData() {
    try {
      if (!this.managers.auth.isAuthenticated()) {
        console.warn('User not authenticated, redirecting to login');
        this.managers.auth.redirectToLogin();
        return;
      }

      await this.managers.address.loadAddresses();
      await this.managers.notes.loadAllNotes();
      
      this.loadWeather().catch(error => {
        console.warn('Weather loading failed:', error);
      });
      
    } catch (error) {
      console.error('Error loading initial data:', error);
      
      const errorMessage = error.message.includes('401') || error.message.includes('403') 
        ? 'Sitzung abgelaufen. Bitte melden Sie sich erneut an.' 
        : 'Fehler beim Laden der Daten. Bitte laden Sie die Seite neu.';
      
      Utils.showNotification('Fehler', errorMessage, 'error');
      
      if (error.message.includes('401') || error.message.includes('403')) {
        this.managers.auth.handleAuthFailure();
      }
    }
  }

  initializeFeatures() {
    if (this.isDebugMode()) {
      this.initializeDebugFeatures();
    }
    
    this.setupPerformanceMonitoring();
  }

  showSection(sectionName) {
    document.querySelectorAll('.section').forEach(section => {
      section.classList.remove('active');
    });
    
    document.querySelectorAll('.nav-item').forEach(navItem => {
      navItem.classList.remove('active');
    });
    
    const targetSection = document.getElementById(sectionName + 'Section');
    const targetNav = document.getElementById('nav-' + sectionName);
    
    if (targetSection) targetSection.classList.add('active');
    if (targetNav) targetNav.classList.add('active');
    
    this.closeMobileSidebar();

    if (sectionName === 'notes' && this.state.notes.length === 0 && this.state.addresses.length > 0) {
      this.managers.notes.loadAllNotes().catch(error => {
        console.error('Error loading notes for section:', error);
      });
    }
    
    this.state.emit('navigation:section', sectionName);
  }

  toggleSidebar() {
    const sidebar = document.getElementById('sidebar');
    const overlay = document.getElementById('sidebarOverlay');
    
    if (!sidebar || !overlay) return;
    
    const isShown = sidebar.classList.contains('show');
    
    sidebar.classList.toggle('show', !isShown);
    overlay.style.display = isShown ? 'none' : 'block';
  }

  closeMobileSidebar() {
    const sidebar = document.getElementById('sidebar');
    const overlay = document.getElementById('sidebarOverlay');
    
    if (sidebar && sidebar.classList.contains('show')) {
      sidebar.classList.remove('show');
      if (overlay) overlay.style.display = 'none';
    }
  }

  filterTable(type) {
    const searchValue = document.getElementById(`${type}SearchInput`)?.value || '';
    this.managers.table.filterTable(type, searchValue);
  }

  changePageSize(type) {
    const size = document.getElementById(`${type}PageSize`)?.value;
    if (size) {
      this.managers.table.changePageSize(type, parseInt(size));
    }
  }

  updateViewButtons(view) {
    const tableBtn = document.getElementById('view-table');
    const treeBtn = document.getElementById('view-tree');
    
    if (tableBtn && treeBtn) {
      tableBtn.className = view === 'table' ? 'btn btn-primary active' : 'btn btn-secondary';
      treeBtn.className = view === 'tree' ? 'btn btn-primary active' : 'btn btn-secondary';
    }

    const tableView = document.getElementById('addressesTableView');
    const treeView = document.getElementById('addressesTreeView');
    
    if (tableView) tableView.style.display = view === 'table' ? 'block' : 'none';
    if (treeView) treeView.style.display = view === 'tree' ? 'block' : 'none';
  }

  async loadWeather() {
    const weatherElement = document.getElementById('weather');
    if (!weatherElement) return;

    weatherElement.textContent = 'Wetter wird geladen...';

    try {
      if (!navigator.geolocation) {
        weatherElement.textContent = 'Geolocation nicht unterstützt';
        return;
      }

      const position = await this.getCurrentPosition();
      const weather = await this.managers.api.fetchWeather({
        lat: position.coords.latitude,
        lon: position.coords.longitude
      });
      
      weatherElement.textContent = weather;
    } catch (error) {
      console.error('Weather error:', error);
      weatherElement.textContent = 'Wetter nicht verfügbar';
    }
  }

  getCurrentPosition() {
    return new Promise((resolve, reject) => {
      navigator.geolocation.getCurrentPosition(resolve, reject, {
        timeout: 10000,
        enableHighAccuracy: false
      });
    });
  }

  isDebugMode() {
    return window.location.search.includes('debug=true') || 
           localStorage.getItem('debug') === 'true';
  }

  initializeDebugFeatures() {
    console.log('=== SimpleSalesman Debug Mode ===');
    console.log('Auth Manager:', this.managers.auth.debugAuth());
    console.log('State:', this.state);
    console.log('Managers:', this.managers);
    
    window.debugApp = {
      state: this.state,
      managers: this.managers,
      stats: () => this.getAppStats(),
      exportState: () => this.exportAppState(),
      clearCache: () => this.clearAppCache()
    };
  }

  setupPerformanceMonitoring() {
    const originalRender = this.managers.table.render.bind(this.managers.table);
    this.managers.table.render = (type) => {
      const start = performance.now();
      const result = originalRender(type);
      const end = performance.now();
      
      if (end - start > 100) {
        console.warn(`Slow table render (${type}): ${(end - start).toFixed(2)}ms`);
      }
      
      return result;
    };
  }

  handleInitializationError(error) {
    const errorContainer = document.createElement('div');
    errorContainer.className = 'alert alert-danger m-3';
    errorContainer.innerHTML = `
      <h6><i class="bi bi-exclamation-triangle-fill me-2"></i>Anwendung konnte nicht gestartet werden</h6>
      <p class="mb-2">Es ist ein Fehler beim Laden der Anwendung aufgetreten:</p>
      <pre class="small text-danger">${error.message}</pre>
      <button class="btn btn-outline-danger btn-sm mt-2" onclick="location.reload()">
        <i class="bi bi-arrow-clockwise me-1"></i>Seite neu laden
      </button>
    `;
    
    document.body.insertBefore(errorContainer, document.body.firstChild);
  }

  getAppStats() {
    return {
      addresses: this.state.addresses.length,
      notes: this.state.notes.length,
      initialized: this.initialized,
      isAuthenticated: this.managers.auth.isAuthenticated(),
      currentUser: this.managers.auth.getUsername(),
      addressStats: this.managers.address.getAddressStats(),
      notesStats: this.managers.notes.getNotesStats()
    };
  }

  exportAppState() {
    const stateExport = {
      timestamp: new Date().toISOString(),
      version: '1.0.0',
      addresses: this.state.addresses.length,
      notes: this.state.notes.length,
      tables: this.state.tables,
      currentView: this.state.currentView
    };
    
    const blob = new Blob([JSON.stringify(stateExport, null, 2)], 
      { type: 'application/json' });
    const url = URL.createObjectURL(blob);
    const link = document.createElement('a');
    link.href = url;
    link.download = `simplesalesman-state-${Date.now()}.json`;
    link.click();
    URL.revokeObjectURL(url);
  }

  clearAppCache() {
    const authKeys = ['access_token', 'refresh_token', 'username', 'user_id'];
    Object.keys(sessionStorage).forEach(key => {
      if (!authKeys.includes(key)) {
        sessionStorage.removeItem(key);
      }
    });
    
    localStorage.removeItem('debug');
    console.log('App cache cleared');
  }

  destroy() {
    if (this.managers.auth && this.managers.auth.refreshTimer) {
      clearTimeout(this.managers.auth.refreshTimer);
    }
    
    delete window.app;
    delete window.addressManager;
    delete window.notesManager;
    delete window.tableManager;
    delete window.modalManager;
    delete window.importManager;
    delete window.authManager;
    
    this.initialized = false;
  }
}

document.addEventListener('DOMContentLoaded', async function() {
  const app = new SimpleSalesmanApp();
  
  try {
    await app.initialize();
  } catch (error) {
    console.error('Application initialization failed:', error);
  }
});

window.addEventListener('beforeunload', function() {
  if (window.app && typeof window.app.destroy === 'function') {
    window.app.destroy();
  }
});

export { SimpleSalesmanApp };
