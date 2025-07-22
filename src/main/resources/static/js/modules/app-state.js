// static/js/modules/app-state.js
export class AppState {
  constructor() {
    this.addresses = [];
    this.notes = [];
    this.selectedAddressId = null;
    this.currentView = 'table';
    this.navigation = { step: 'plz', plz: null, street: null, number: null };
    this.tables = {
      addresses: { page: 1, size: 20, sort: 'plz', order: 'asc', search: '' },
      notes: { page: 1, size: 20, sort: 'id', order: 'desc', search: '' }
    };
    this.wakeLock = null;
    this.importInProgress = false;
    this.listeners = new Map();
  }

  subscribe(event, callback) {
    if (!this.listeners.has(event)) {
      this.listeners.set(event, []);
    }
    this.listeners.get(event).push(callback);
    return () => {
      const callbacks = this.listeners.get(event);
      if (callbacks) {
        const index = callbacks.indexOf(callback);
        if (index > -1) callbacks.splice(index, 1);
      }
    };
  }

  emit(event, data) {
    if (this.listeners.has(event)) {
      this.listeners.get(event).forEach(callback => {
        try {
          callback(data);
        } catch (error) {
          console.error(`Error in event listener for ${event}:`, error);
        }
      });
    }
  }

  setAddresses(addresses) {
    this.addresses = addresses;
    this.emit('addresses:updated', addresses);
  }

  setNotes(notes) {
    this.notes = notes;
    this.emit('notes:updated', notes);
  }

  setSelectedAddress(addressId) {
    this.selectedAddressId = addressId;
    this.emit('address:selected', addressId);
  }

  updateTableState(type, updates) {
    Object.assign(this.tables[type], updates);
    this.emit(`table:${type}:updated`, this.tables[type]);
  }

  setCurrentView(view) {
    this.currentView = view;
    this.emit('view:changed', view);
  }

  setNavigation(navigation) {
    this.navigation = { ...this.navigation, ...navigation };
    this.emit('navigation:updated', this.navigation);
  }

  getAddressById(id) {
    return this.addresses.find(addr => addr.id === id);
  }

  getNotesForAddress(addressId) {
    return this.notes.filter(note => note.addressId === addressId);
  }

  resetNavigation() {
    this.setNavigation({ step: 'plz', plz: null, street: null, number: null });
  }

  resetTableState(type) {
    this.updateTableState(type, { page: 1, search: '' });
  }
}