// static/js/modules/notes-manager.js
import { Utils } from './utils.js';

export class NotesManager {
  constructor(apiService, appState, addressManager) {
    this.api = apiService;
    this.state = appState;
    this.addressManager = addressManager;
  }

  async loadAllNotes() {
    try {
      const notes = await this.api.fetchAllNotes();
      console.log('Loaded notes:', notes.length);
      
      const notesWithAddresses = notes.map(note => {
        const address = this.state.addresses.find(a => a.id === note.addressId);
        return {
          ...note,
          address: address || { 
            id: note.addressId, 
            addressText: note.addressText || `Address ID: ${note.addressId}` 
          }
        };
      });
      
      this.state.setNotes(notesWithAddresses);
      return notesWithAddresses;
    } catch (error) {
      console.error('Error loading notes:', error);
      this.showNotesError(`Fehler beim Laden der Notizen: ${error.message}`);
      throw error;
    }
  }

  async loadNotesForAddress(addressId) {
    try {
      const notes = await this.api.fetchNotes(addressId);
      console.log(`Loaded ${notes.length} notes for address ${addressId}`);
      return notes;
    } catch (error) {
      console.error(`Error loading notes for address ${addressId}:`, error);
      throw error;
    }
  }

  async createNote(addressId, text, createdBy) {
    if (!text?.trim()) {
      throw new Error('Notiztext ist erforderlich');
    }

    if (!addressId) {
      throw new Error('Adresse ist erforderlich');
    }

    try {
      const newNote = await this.api.createNote(addressId, text.trim(), createdBy || this.api.getUsername());
      
      const address = this.state.getAddressById(addressId);
      const noteWithAddress = {
        ...newNote,
        address: address || { id: addressId, addressText: `Address ID: ${addressId}` }
      };
      
      this.state.notes.unshift(noteWithAddress);
      this.state.emit('notes:updated', this.state.notes);
      
      Utils.showNotification('Notiz erstellt', 'Notiz wurde erfolgreich gespeichert', 'success');
      
      return newNote;
    } catch (error) {
      console.error('Error creating note:', error);
      Utils.showNotification('Fehler', `Fehler beim Erstellen der Notiz: ${error.message}`, 'error');
      throw error;
    }
  }

  async updateNote(noteId, text) {
    if (!text?.trim()) {
      throw new Error('Notiztext ist erforderlich');
    }

    try {
      const updatedNote = await this.api.updateNote(noteId, text.trim());
      
      const noteIndex = this.state.notes.findIndex(n => n.id === noteId);
      if (noteIndex !== -1) {
        this.state.notes[noteIndex] = {
          ...this.state.notes[noteIndex],
          ...updatedNote
        };
        this.state.emit('notes:updated', this.state.notes);
      }
      
      Utils.showNotification('Notiz aktualisiert', 'Notiz wurde erfolgreich aktualisiert', 'success');
      
      return updatedNote;
    } catch (error) {
      console.error('Error updating note:', error);
      Utils.showNotification('Fehler', `Fehler beim Aktualisieren der Notiz: ${error.message}`, 'error');
      throw error;
    }
  }

  async deleteNote(noteId) {
    try {
      await this.api.deleteNote(noteId);
      
      this.state.notes = this.state.notes.filter(n => n.id !== noteId);
      this.state.emit('notes:updated', this.state.notes);
      
      Utils.showNotification('Notiz gelöscht', 'Notiz wurde erfolgreich gelöscht', 'success');
      
      return true;
    } catch (error) {
      console.error('Error deleting note:', error);
      Utils.showNotification('Fehler', `Fehler beim Löschen der Notiz: ${error.message}`, 'error');
      throw error;
    }
  }

  async deleteNoteWithConfirm(noteId) {
    const note = this.state.notes.find(n => n.id === noteId);
    if (!note) {
      Utils.showNotification('Fehler', 'Notiz nicht gefunden', 'error');
      return;
    }

    const confirmMessage = `Möchten Sie diese Notiz wirklich löschen?\n\n"${note.text.substring(0, 100)}${note.text.length > 100 ? '...' : ''}"`;
    
    if (confirm(confirmMessage)) {
      try {
        await this.deleteNote(noteId);
      } catch (error) {
        // Error is already handled in deleteNote method
      }
    }
  }

  showNotesError(message) {
    const tbody = document.querySelector('#notesTable tbody');
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

  editNote(noteId) {
    const note = this.state.notes.find(n => n.id === noteId);
    if (!note) {
      Utils.showNotification('Fehler', 'Notiz nicht gefunden', 'error');
      return;
    }

    const newText = prompt('Notiz bearbeiten:', note.text);
    
    if (newText !== null && newText.trim() !== note.text) {
      this.updateNote(noteId, newText);
    }
  }

  getNotesStats() {
    const notes = this.state.notes;
    const authors = new Set(notes.map(n => n.createdBy).filter(Boolean));
    const addressesWithNotes = new Set(notes.map(n => n.addressId));
    
    const today = new Date();
    today.setHours(0, 0, 0, 0);
    
    const notesToday = notes.filter(note => {
      const noteDate = new Date(note.createdAt);
      noteDate.setHours(0, 0, 0, 0);
      return noteDate.getTime() === today.getTime();
    });

    const thisWeek = new Date();
    thisWeek.setDate(thisWeek.getDate() - 7);
    
    const notesThisWeek = notes.filter(note => {
      return new Date(note.createdAt) >= thisWeek;
    });

    return {
      totalNotes: notes.length,
      uniqueAuthors: authors.size,
      addressesWithNotes: addressesWithNotes.size,
      notesToday: notesToday.length,
      notesThisWeek: notesThisWeek.length,
      averageNotesPerAddress: addressesWithNotes.size > 0 ? 
        (notes.length / addressesWithNotes.size).toFixed(2) : 0
    };
  }

  validateNote(text) {
    const errors = [];

    if (!text || typeof text !== 'string') {
      errors.push('Notiztext ist erforderlich');
    } else {
      const trimmedText = text.trim();
      
      if (trimmedText.length === 0) {
        errors.push('Notiztext darf nicht leer sein');
      }
      
      if (trimmedText.length > 10000) {
        errors.push('Notiztext ist zu lang (max. 10.000 Zeichen)');
      }
    }

    return {
      isValid: errors.length === 0,
      errors
    };
  }

  getNoteById(noteId) {
    return this.state.notes.find(note => note.id === noteId);
  }
}