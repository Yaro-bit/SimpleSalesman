// static/js/modules/modal-manager.js
import { Utils } from './utils.js';

export class ModalManager {
  constructor(apiService, appState, addressManager, notesManager) {
    this.api = apiService;
    this.state = appState;
    this.addressManager = addressManager;
    this.notesManager = notesManager;
    this.currentModal = null;
    this.initializeModals();
  }

  initializeModals() {
    this.setupNotesModal();
  }

  setupNotesModal() {
    const modalElement = document.getElementById('notesModal');
    if (!modalElement) return;

    this.notesModal = new bootstrap.Modal(modalElement);
    
    const noteForm = document.getElementById('modalNoteForm');
    if (noteForm) {
      noteForm.addEventListener('submit', (e) => this.handleNoteSubmission(e));
    }

    modalElement.addEventListener('hidden.bs.modal', () => {
      this.cleanupModal();
    });
  }

  async openNotesModal(addressId) {
    if (!addressId) {
      Utils.showNotification('Fehler', 'Ungültige Adresse', 'error');
      return;
    }

    const address = this.state.getAddressById(addressId);
    if (!address) {
      Utils.showNotification('Fehler', 'Adresse nicht gefunden', 'error');
      return;
    }

    this.state.setSelectedAddress(addressId);
    this.updateModalHeader(address);
    
    if (this.notesModal) {
      this.notesModal.show();
    }

    await Promise.all([
      this.loadNotesInModal(addressId),
      this.loadProjectsInModal(address)
    ]);
  }

  updateModalHeader(address) {
    const titleElement = document.getElementById('modalTitle');
    const addressInfoElement = document.getElementById('modalAddressInfo');

    if (titleElement) {
      titleElement.textContent = `Notizen - ${Utils.getAddressString(address)}`;
    }

    if (addressInfoElement) {
      addressInfoElement.innerHTML = `
        <div class="d-flex justify-content-between align-items-center">
          <span>${Utils.getAddressString(address)}</span>
          <small class="text-muted">
            <i class="bi bi-hash me-1"></i>ID: ${address.id}
          </small>
        </div>
        ${address.regionName ? `
          <small class="text-muted mt-1 d-block">
            <i class="bi bi-geo-alt me-1"></i>Region: ${address.regionName}
          </small>
        ` : ''}
      `;
    }
  }

  async loadNotesInModal(addressId) {
    const notesContainer = document.getElementById('modalNotesList');
    if (!notesContainer) return;

    notesContainer.innerHTML = `
      <div class="text-center p-3">
        <div class="spinner-border spinner-border-sm me-2" role="status"></div>
        Notizen werden geladen...
      </div>
    `;

    try {
      const notes = await this.notesManager.loadNotesForAddress(addressId);
      
      if (!notes || notes.length === 0) {
        notesContainer.innerHTML = `
          <div class="text-center text-muted p-3">
            <i class="bi bi-journal-x" style="font-size: 2rem; opacity: 0.5;"></i>
            <div class="mt-2">
              <h6>Keine Notizen vorhanden</h6>
              <small>Erstellen Sie die erste Notiz für diese Adresse</small>
            </div>
          </div>
        `;
        return;
      }

      this.renderNotesInModal(notes, notesContainer);
      
    } catch (error) {
      console.error('Error loading notes in modal:', error);
      notesContainer.innerHTML = `
        <div class="text-center text-danger p-3">
          <i class="bi bi-exclamation-triangle me-2"></i>
          Fehler beim Laden der Notizen: ${error.message}
        </div>
      `;
    }
  }

  renderNotesInModal(notes, container) {
    container.innerHTML = '';

    const sortedNotes = [...notes].sort((a, b) => {
      return new Date(b.createdAt || 0) - new Date(a.createdAt || 0);
    });

    sortedNotes.forEach((note, index) => {
      const noteElement = this.createModalNoteElement(note);
      noteElement.style.animationDelay = `${index * 50}ms`;
      container.appendChild(noteElement);
    });
  }

  createModalNoteElement(note) {
    const noteDiv = Utils.createElement('div', 'note-item fade-in');
    
    const timeString = Utils.formatDate(note.createdAt);
    
    noteDiv.innerHTML = `
      <div class="note-content mb-2">${Utils.sanitizeHtml(note.text || 'Notiz ohne Text')}</div>
      <div class="note-meta d-flex justify-content-between align-items-center">
        <small class="text-muted">
          <i class="bi bi-person me-1"></i>${note.createdBy || 'Unbekannt'}
          <span class="mx-2">•</span>
          <i class="bi bi-clock me-1"></i>${timeString}
        </small>
        <div class="note-actions">
          <button onclick="window.modalManager.editNoteInModal(${note.id})" 
                  class="btn btn-sm btn-outline-secondary me-1" 
                  title="Bearbeiten">
            <i class="bi bi-pencil"></i>
          </button>
          <button onclick="window.modalManager.deleteNoteInModal(${note.id})" 
                  class="btn btn-sm btn-outline-danger" 
                  title="Löschen">
            <i class="bi bi-trash"></i>
          </button>
        </div>
      </div>
    `;
    
    return noteDiv;
  }

  async loadProjectsInModal(address) {
    const projectsContainer = document.getElementById('modalProjectList');
    if (!projectsContainer) return;

    projectsContainer.innerHTML = `
      <div class="text-center p-2">
        <div class="spinner-border spinner-border-sm me-2" role="status"></div>
        Projekte werden geladen...
      </div>
    `;

    try {
      const projects = address.projects || [];
      
      if (projects.length === 0) {
        projectsContainer.innerHTML = `
          <div class="text-center text-muted p-3">
            <i class="bi bi-folder-x me-2"></i>
            Keine Projekte vorhanden
          </div>
        `;
        return;
      }

      this.renderProjectsInModal(projects, projectsContainer);
      
    } catch (error) {
      console.error('Error loading projects in modal:', error);
      projectsContainer.innerHTML = `
        <div class="text-center text-danger p-3">
          <i class="bi bi-exclamation-triangle me-2"></i>
          Fehler beim Laden der Projekte: ${error.message}
        </div>
      `;
    }
  }

  renderProjectsInModal(projects, container) {
    container.innerHTML = '';

    projects.forEach((project, index) => {
      const projectElement = this.createModalProjectElement(project);
      projectElement.style.animationDelay = `${index * 50}ms`;
      container.appendChild(projectElement);
    });
  }

  createModalProjectElement(project) {
    const projectDiv = Utils.createElement('div', 'project-item fade-in mb-2 p-2 border rounded');
    
    const id = project.id || '-';
    const status = project.status || '-';
    const operator = project.operator || '-';
    const price = project.productPrice != null ? `${project.productPrice.toFixed(2)} €` : '-';
    const start = project.salesStart ? Utils.formatDateShort(project.salesStart) : '-';
    const end = project.salesEnd ? Utils.formatDateShort(project.salesEnd) : '-';
    const homes = project.numberOfHomes || '-';

    const statusClass = Utils.getProjectStatusClass(status);
    const statusIcon = Utils.getProjectStatusIcon(status);

    projectDiv.innerHTML = `
      <div class="d-flex justify-content-between align-items-start">
        <div class="flex-grow-1">
          <div class="d-flex align-items-center gap-2 mb-1">
            <strong class="text-primary">Projekt ${id}</strong>
            <span class="project-tag ${statusClass}">
              <i class="bi ${statusIcon} me-1"></i>
              ${status}
            </span>
          </div>
          <div class="row text-muted small">
            <div class="col-sm-6">
              <div><i class="bi bi-building me-1"></i><strong>Anbieter:</strong> ${operator}</div>
              <div><i class="bi bi-currency-euro me-1"></i><strong>Preis:</strong> ${price}</div>
            </div>
            <div class="col-sm-6">
              <div><i class="bi bi-house me-1"></i><strong>Einheiten:</strong> ${homes}</div>
              <div><i class="bi bi-calendar-range me-1"></i><strong>Laufzeit:</strong> ${start} - ${end}</div>
            </div>
          </div>
        </div>
      </div>
    `;
    
    return projectDiv;
  }

  async handleNoteSubmission(event) {
    event.preventDefault();
    
    const textArea = document.getElementById('modalNewNote');
    const submitButton = event.target.querySelector('button[type="submit"]');
    
    if (!textArea || !this.state.selectedAddressId) {
      return;
    }

    const text = textArea.value.trim();
    if (!text) {
      this.showModalAlert('Bitte geben Sie einen Notiztext ein.', 'warning');
      return;
    }

    const validation = this.notesManager.validateNote(text);
    if (!validation.isValid) {
      this.showModalAlert(validation.errors.join('<br>'), 'danger');
      return;
    }

    const originalButtonText = submitButton.innerHTML;
    submitButton.disabled = true;
    submitButton.innerHTML = '<div class="spinner-border spinner-border-sm me-2"></div>Speichern...';

    try {
      await this.notesManager.createNote(
        this.state.selectedAddressId,
        text,
        this.api.getUsername()
      );

      textArea.value = '';
      this.showModalAlert('Notiz erfolgreich gespeichert!', 'success');
      await this.loadNotesInModal(this.state.selectedAddressId);
      
    } catch (error) {
      console.error('Error creating note in modal:', error);
      this.showModalAlert(`Fehler beim Speichern der Notiz: ${error.message}`, 'danger');
    } finally {
      submitButton.disabled = false;
      submitButton.innerHTML = originalButtonText;
    }
  }

  showModalAlert(message, type = 'info') {
    const modalBody = document.querySelector('#notesModal .modal-body');
    if (!modalBody) return;

    modalBody.querySelectorAll('.modal-alert').forEach(alert => alert.remove());

    const alertDiv = Utils.createErrorAlert(message, type);
    alertDiv.classList.add('modal-alert');
    
    const form = document.getElementById('modalNoteForm');
    if (form) {
      modalBody.insertBefore(alertDiv, form);
    } else {
      modalBody.appendChild(alertDiv);
    }

    if (type === 'success' || type === 'warning') {
      setTimeout(() => {
        alertDiv.remove();
      }, 3000);
    }
  }

  async editNoteInModal(noteId) {
    const note = this.notesManager.getNoteById(noteId);
    if (!note) {
      this.showModalAlert('Notiz nicht gefunden', 'error');
      return;
    }

    const newText = prompt('Notiz bearbeiten:', note.text);
    
    if (newText !== null && newText.trim() !== note.text) {
      try {
        await this.notesManager.updateNote(noteId, newText);
        this.showModalAlert('Notiz erfolgreich aktualisiert', 'success');
        await this.loadNotesInModal(this.state.selectedAddressId);
      } catch (error) {
        this.showModalAlert(`Fehler beim Aktualisieren: ${error.message}`, 'danger');
      }
    }
  }

  async deleteNoteInModal(noteId) {
    const note = this.notesManager.getNoteById(noteId);
    if (!note) {
      this.showModalAlert('Notiz nicht gefunden', 'error');
      return;
    }

    const confirmMessage = `Möchten Sie diese Notiz wirklich löschen?\n\n"${note.text.substring(0, 100)}${note.text.length > 100 ? '...' : ''}"`;
    
    if (confirm(confirmMessage)) {
      try {
        await this.notesManager.deleteNote(noteId);
        this.showModalAlert('Notiz erfolgreich gelöscht', 'success');
        await this.loadNotesInModal(this.state.selectedAddressId);
      } catch (error) {
        this.showModalAlert(`Fehler beim Löschen: ${error.message}`, 'danger');
      }
    }
  }

  cleanupModal() {
    this.state.selectedAddressId = null;
    
    const textArea = document.getElementById('modalNewNote');
    if (textArea) {
      textArea.value = '';
    }
    
    document.querySelectorAll('.modal-alert').forEach(alert => alert.remove());
  }

  closeModal() {
    if (this.notesModal) {
      this.notesModal.hide();
    }
  }

  isModalOpen() {
    const modalElement = document.getElementById('notesModal');
    return modalElement?.classList.contains('show') || false;
  }

  setupKeyboardShortcuts() {
    document.addEventListener('keydown', (event) => {
      if (event.key === 'Escape' && this.isModalOpen()) {
        this.closeModal();
        return;
      }

      if ((event.ctrlKey || event.metaKey) && event.key === 'Enter' && this.isModalOpen()) {
        const form = document.getElementById('modalNoteForm');
        if (form) {
          form.dispatchEvent(new Event('submit'));
        }
        event.preventDefault();
      }
    });
  }
}
