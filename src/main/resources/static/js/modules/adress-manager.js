// static/js/modules/address-manager.js
import { Utils } from './utils.js';

export class AddressManager {
  constructor(apiService, appState) {
    this.api = apiService;
    this.state = appState;
    this.tableManager = null;
  }

  setTableManager(tableManager) {
    this.tableManager = tableManager;
  }

  async loadAddresses() {
    try {
      const addresses = await this.api.fetchAddresses();
      console.log('Loaded addresses:', addresses.length);
      this.state.setAddresses(addresses);
      
      if (this.tableManager) {
        this.tableManager.render('addresses');
      }
      
      this.showAllPlzs();
      return addresses;
    } catch (error) {
      console.error('Error loading addresses:', error);
      this.showError('addressesTable', `Fehler beim Laden der Adressen: ${error.message}`);
      throw error;
    }
  }

  showError(tableId, message) {
    const tbody = document.querySelector(`#${tableId} tbody`);
    if (tbody) {
      tbody.innerHTML = `
        <tr>
          <td colspan="6" class="text-center text-danger p-4">
            <i class="bi bi-exclamation-triangle me-2"></i>
            ${Utils.sanitizeHtml(message)}
          </td>
        </tr>
      `;
    }
  }

  showAllPlzs() {
    const plzs = [...new Set(
      this.state.addresses
        .map(a => Utils.extractPlz(a))
        .filter(p => p)
    )].sort();
    
    const grid = document.getElementById('plzGrid');
    if (!grid) return;

    grid.innerHTML = '';

    if (plzs.length === 0) {
      grid.innerHTML = '<div class="tree-item">Keine PLZs verfügbar</div>';
      return;
    }

    plzs.forEach(plz => {
      const item = Utils.createElement('div', 'tree-item', plz);
      item.onclick = () => this.selectPlz(plz);
      grid.appendChild(item);
    });
  }

  selectPlz(plz) {
    this.state.setNavigation({ step: 'street', plz, street: null, number: null });
    
    this.updateBreadcrumb([
      { text: `PLZ: ${plz}`, active: false },
      { text: 'Straße auswählen', active: true }
    ]);
    
    this.showNavigationElements('street');

    const streets = [...new Set(
      this.state.addresses
        .filter(a => Utils.extractPlz(a) == plz)
        .map(a => Utils.extractStreet(a))
        .filter(s => s)
    )].sort();

    this.renderGrid('streetGrid', streets, (street) => this.selectStreet(street));
  }

  selectStreet(street) {
    this.state.setNavigation({ ...this.state.navigation, step: 'number', street });
    
    this.updateBreadcrumb([
      { text: `PLZ: ${this.state.navigation.plz}`, active: false },
      { text: `Straße: ${street}`, active: false },
      { text: 'Hausnummer auswählen', active: true }
    ]);
    
    this.showNavigationElements('number');

    const numbers = this.state.addresses
      .filter(a => 
        Utils.extractPlz(a) == this.state.navigation.plz && 
        Utils.extractStreet(a) === street
      )
      .map(a => Utils.extractNumber(a))
      .filter(n => n)
      .sort((a, b) => {
        const numA = parseInt(a);
        const numB = parseInt(b);
        return !isNaN(numA) && !isNaN(numB) ? numA - numB : a.localeCompare(b);
      });

    this.renderGrid('numberGrid', numbers, (number) => this.selectNumber(number));
  }

  selectNumber(number) {
    const address = this.state.addresses.find(a => 
      Utils.extractPlz(a) == this.state.navigation.plz && 
      Utils.extractStreet(a) === this.state.navigation.street && 
      Utils.extractNumber(a) === number
    );
    
    if (address && window.modalManager) {
      window.modalManager.openNotesModal(address.id);
    }
  }

  goBack() {
    const nav = this.state.navigation;
    
    if (nav.step === 'street') {
      this.state.setNavigation({ step: 'plz', plz: null, street: null, number: null });
      this.updateBreadcrumb([{ text: 'PLZ auswählen', active: true }]);
      this.showNavigationElements('plz');
      this.showAllPlzs();
    } else if (nav.step === 'number') {
      this.state.setNavigation({ ...nav, step: 'street', street: null, number: null });
      this.selectPlz(nav.plz);
    }
  }

  updateBreadcrumb(items) {
    const breadcrumb = document.getElementById('breadcrumb');
    if (!breadcrumb) return;

    breadcrumb.innerHTML = items.map(item => 
      `<span class="breadcrumb-item ${item.active ? 'active' : ''}">${item.text}</span>`
    ).join('');
  }

  showNavigationElements(step) {
    const backButton = document.getElementById('backButton');
    const plzSelection = document.getElementById('plzSelection');
    const streetSelection = document.getElementById('streetSelection');
    const numberSelection = document.getElementById('numberSelection');

    if (backButton) backButton.style.display = step === 'plz' ? 'none' : 'block';
    if (plzSelection) plzSelection.style.display = step === 'plz' ? 'block' : 'none';
    if (streetSelection) streetSelection.style.display = step === 'street' ? 'block' : 'none';
    if (numberSelection) numberSelection.style.display = step === 'number' ? 'block' : 'none';
  }

  renderGrid(gridId, items, onClick) {
    const grid = document.getElementById(gridId);
    if (!grid) return;

    grid.innerHTML = '';
    
    if (items.length === 0) {
      grid.innerHTML = '<div class="tree-item">Keine Einträge verfügbar</div>';
      return;
    }

    items.forEach(item => {
      const element = Utils.createElement('div', 'tree-item', item);
      element.onclick = () => onClick(item);
      grid.appendChild(element);
    });
  }

  showAddressView(view) {
    this.state.setCurrentView(view);
    
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

    if (view === 'tree') {
      this.state.resetNavigation();
      this.showAllPlzs();
    }
    
    this.state.emit('navigation:section', view);
  }

  getAddressStats() {
    const addresses = this.state.addresses;
    const plzSet = new Set();
    const streetSet = new Set();
    let projectCount = 0;

    addresses.forEach(addr => {
      const plz = Utils.extractPlz(addr);
      const street = Utils.extractStreet(addr);
      
      if (plz) plzSet.add(plz);
      if (street) streetSet.add(street);
      if (addr.projects) projectCount += addr.projects.length;
    });

    return {
      totalAddresses: addresses.length,
      uniquePlzs: plzSet.size,
      uniqueStreets: streetSet.size,
      totalProjects: projectCount,
      averageProjectsPerAddress: addresses.length > 0 ? (projectCount / addresses.length).toFixed(2) : 0
    };
  }
}