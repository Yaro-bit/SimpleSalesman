// static/js/modules/import-manager.js
import { Utils } from './utils.js';

export class ImportManager {
  constructor(apiService, appState) {
    this.api = apiService;
    this.state = appState;
    this.selectedFile = null;
    this.wakeLock = null;
    this.initializeImportUI();
  }

  initializeImportUI() {
    this.setupFileInput();
    this.setupDragAndDrop();
    this.setupImportForm();
  }

  setupFileInput() {
    const fileInput = document.getElementById('fileInput');
    if (fileInput) {
      fileInput.addEventListener('change', (e) => {
        const file = e.target.files[0];
        this.handleFileSelection(file);
      });
    }
  }

  setupDragAndDrop() {
    const importZone = document.querySelector('.import-zone');
    if (!importZone) return;

    importZone.addEventListener('click', () => {
      document.getElementById('fileInput')?.click();
    });

    importZone.addEventListener('dragover', (e) => {
      e.preventDefault();
      importZone.classList.add('dragover');
    });

    importZone.addEventListener('dragleave', (e) => {
      e.preventDefault();
      if (!importZone.contains(e.relatedTarget)) {
        importZone.classList.remove('dragover');
      }
    });

    importZone.addEventListener('drop', (e) => {
      e.preventDefault();
      importZone.classList.remove('dragover');
      
      const files = e.dataTransfer.files;
      if (files.length > 0) {
        this.handleFileSelection(files[0]);
      }
    });
  }

  setupImportForm() {
    const importForm = document.getElementById('importForm');
    if (importForm) {
      importForm.addEventListener('submit', (e) => {
        e.preventDefault();
        this.startImport();
      });
    }
  }

  handleFileSelection(file) {
    if (!file) {
      this.clearSelectedFile();
      return;
    }

    if (!Utils.isValidExcelFile(file)) {
      Utils.showNotification(
        'Ungültiger Dateityp', 
        'Bitte wählen Sie eine Excel-Datei (.xlsx oder .xls)', 
        'error'
      );
      this.clearSelectedFile();
      return;
    }

    const sizeMB = file.size / (1024 * 1024);
    if (sizeMB > 100) {
      const proceed = confirm(
        `Die Datei ist sehr groß (${sizeMB.toFixed(1)} MB). ` +
        'Der Import kann sehr lange dauern. Fortfahren?'
      );
      
      if (!proceed) {
        this.clearSelectedFile();
        return;
      }
    }

    this.selectedFile = file;
    this.updateFileInputDisplay(file);
    
    const fileInput = document.getElementById('fileInput');
    if (fileInput && fileInput.files[0] !== file) {
      const dt = new DataTransfer();
      dt.items.add(file);
      fileInput.files = dt.files;
    }
  }

  updateFileInputDisplay(file = null) {
    const zone = document.querySelector('.import-zone');
    const clearBtn = document.getElementById('clearFileBtn');

    if (!zone || !clearBtn) return;

    if (file) {
      const sizeFormatted = Utils.formatFileSize(file.size);
      const estimatedTime = this.estimateImportTime(file.size);
      
      zone.classList.add('file-selected');
      zone.innerHTML = `
        <i class="bi bi-file-earmark-check" style="font-size: 3rem; color: var(--success); margin-bottom: 1rem;"></i>
        <h6 class="mb-2 text-success">Datei ausgewählt</h6>
        <p class="mb-1"><strong>${file.name}</strong></p>
        <p class="text-muted mb-1">Größe: ${sizeFormatted}</p>
        <small class="text-muted">Geschätzte Importdauer: ${estimatedTime}</small>
        <div class="mt-2">
          <small class="text-info">Klicken zum Ändern der Datei</small>
        </div>
      `;
      clearBtn.style.display = 'inline-flex';
    } else {
      zone.classList.remove('file-selected');
      zone.innerHTML = `
        <i class="bi bi-cloud-upload" style="font-size: 3rem; color: var(--primary-color); margin-bottom: 1rem;"></i>
        <h6 class="mb-2">Excel-Datei auswählen</h6>
        <p class="text-muted mb-1">Klicken oder Datei hierher ziehen</p>
        <small class="text-muted">Unterstützte Formate: .xlsx, .xls (max. 500MB)</small>
      `;
      clearBtn.style.display = 'none';
    }
  }

  estimateImportTime(fileSize) {
    const sizeMB = fileSize / (1024 * 1024);
    
    if (sizeMB < 5) return '1-5 Minuten';
    if (sizeMB < 20) return '5-30 Minuten';
    if (sizeMB < 50) return '30-90 Minuten';
    if (sizeMB < 100) return '1-2 Stunden';
    return '2+ Stunden';
  }

  clearSelectedFile() {
    this.selectedFile = null;
    
    const fileInput = document.getElementById('fileInput');
    if (fileInput) {
      fileInput.value = '';
    }
    
    this.updateFileInputDisplay(null);
  }

  async startImport() {
    if (!this.selectedFile) {
      this.showImportResult('Bitte wählen Sie eine Datei aus.', 'danger');
      return;
    }

    const sizeMB = this.selectedFile.size / (1024 * 1024);
    const estimatedTime = this.estimateImportTime(this.selectedFile.size);

    const confirmMessage = `
Import Details:
- Datei: ${this.selectedFile.name}
- Größe: ${Utils.formatFileSize(this.selectedFile.size)}
- Geschätzte Dauer: ${estimatedTime}

Wichtige Hinweise:
- Browser-Tab offen lassen
- Computer nicht in Standby versetzen
- Import kann nicht abgebrochen werden

Möchten Sie fortfahren?
    `.trim();

    if (!confirm(confirmMessage)) {
      return;
    }

    this.state.importInProgress = true;
    await this.prepareImportEnvironment();

    const submitButton = document.querySelector('#importForm button[type="submit"]');
    const importResult = document.getElementById('importResult');
    
    if (submitButton) {
      submitButton.disabled = true;
      submitButton.innerHTML = '<div class="spinner-border spinner-border-sm me-2"></div>Import läuft...';
    }

    const startTime = Date.now();
    let timerInterval;

    try {
      this.showImportProgress(this.selectedFile, estimatedTime);
      timerInterval = this.startImportTimer(startTime);

      const result = await this.performImport(this.selectedFile);
      
      if (timerInterval) clearInterval(timerInterval);
      
      const totalTime = Date.now() - startTime;
      const minutes = Math.floor(totalTime / 60000);
      const seconds = Math.floor((totalTime % 60000) / 1000);

      if (result && result.success) {
        this.showImportSuccess(result, minutes, seconds);
        
        if (window.addressManager) {
          await window.addressManager.loadAddresses();
        }
        if (window.notesManager) {
          await window.notesManager.loadAllNotes();
        }

        Utils.showNotification(
          'Import erfolgreich!', 
          `${result.recordsProcessed || 'Unbekannte Anzahl'} Datensätze verarbeitet in ${minutes}:${seconds.toString().padStart(2, '0')}`, 
          'success'
        );

        setTimeout(() => this.clearSelectedFile(), 5000);
        
      } else {
        throw new Error(result?.error || 'Import fehlgeschlagen');
      }

    } catch (error) {
      if (timerInterval) clearInterval(timerInterval);
      
      console.error('Import error:', error);
      this.showImportError(error.message);
      
      Utils.showNotification('Import fehlgeschlagen', error.message, 'error');
      
    } finally {
      this.state.importInProgress = false;
      this.cleanupImportEnvironment();
      
      if (submitButton) {
        submitButton.disabled = false;
        submitButton.innerHTML = '<i class="bi bi-upload"></i>Import starten';
      }
    }
  }

  async prepareImportEnvironment() {
    this.wakeLock = await Utils.requestWakeLock();
    await Utils.requestNotificationPermission();
    this.disablePageVisibilityWarning();
  }

  cleanupImportEnvironment() {
    if (this.wakeLock) {
      Utils.releaseWakeLock(this.wakeLock);
      this.wakeLock = null;
    }
    
    this.enablePageVisibilityWarning();
  }

  disablePageVisibilityWarning() {
    this.beforeUnloadHandler = (e) => {
      if (this.state.importInProgress) {
        e.preventDefault();
        e.returnValue = 'Import läuft noch. Seite wirklich verlassen?';
        return e.returnValue;
      }
    };
    
    window.addEventListener('beforeunload', this.beforeUnloadHandler);
  }

  enablePageVisibilityWarning() {
    if (this.beforeUnloadHandler) {
      window.removeEventListener('beforeunload', this.beforeUnloadHandler);
      this.beforeUnloadHandler = null;
    }
  }

  async performImport(file) {
    return this.api.importFile(file, (progress) => {
      this.updateImportProgress(progress);
    });
  }

  updateImportProgress(progress) {
    const progressElement = document.getElementById('importProgress');
    if (progressElement && progress.percentage !== undefined) {
      progressElement.style.width = `${progress.percentage}%`;
      progressElement.textContent = `${progress.percentage}%`;
    }
  }

  showImportProgress(file, estimatedTime) {
    const importResult = document.getElementById('importResult');
    if (!importResult) return;

    const sizeMB = (file.size / (1024 * 1024)).toFixed(1);
    
    importResult.innerHTML = `
      <div class="alert alert-warning">
        <div class="d-flex align-items-start">
          <div class="spinner-border spinner-border-sm me-3 mt-1" role="status"></div>
          <div class="flex-grow-1">
            <h6 class="mb-2">
              <i class="bi bi-hourglass-split me-2"></i>
              Import läuft
            </h6>
            <div class="mb-3">
              <div><strong>Datei:</strong> ${file.name}</div>
              <div><strong>Größe:</strong> ${sizeMB} MB</div>
              <div><strong>Geschätzte Dauer:</strong> ${estimatedTime}</div>
            </div>
            <div class="progress mb-2" style="height: 20px;">
              <div id="importProgress" class="progress-bar progress-bar-striped progress-bar-animated" 
                   role="progressbar" style="width: 0%">0%</div>
            </div>
            <div class="small">
              <div>Laufzeit: <span id="timerDisplay" class="fw-bold">00:00:00</span></div>
              <div class="text-muted mt-1">
                <i class="bi bi-info-circle me-1"></i>
                Browser-Tab offen lassen und Computer nicht in Standby versetzen
              </div>
            </div>
          </div>
        </div>
      </div>
    `;
  }

  startImportTimer(startTime) {
    return setInterval(() => {
      const elapsed = Date.now() - startTime;
      const hours = Math.floor(elapsed / 3600000);
      const minutes = Math.floor((elapsed % 3600000) / 60000);
      const seconds = Math.floor((elapsed % 60000) / 1000);
      
      const display = document.getElementById('timerDisplay');
      if (display) {
        display.textContent = `${hours.toString().padStart(2, '0')}:${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}`;
      }
    }, 1000);
  }

  showImportSuccess(result, minutes, seconds) {
    const importResult = document.getElementById('importResult');
    if (!importResult) return;

    importResult.innerHTML = `
      <div class="alert alert-success">
        <h6>
          <i class="bi bi-check-circle-fill me-2"></i>
          Import erfolgreich abgeschlossen!
        </h6>
        <div class="mt-2">
          <div><strong>Verarbeitete Datensätze:</strong> ${result.recordsProcessed || 'Unbekannt'}</div>
          <div><strong>Dauer:</strong> ${minutes}:${seconds.toString().padStart(2, '0')} Minuten</div>
          ${result.warnings ? `<div><strong>Warnungen:</strong> ${result.warnings}</div>` : ''}
          ${result.errors ? `<div class="text-warning"><strong>Fehler:</strong> ${result.errors}</div>` : ''}
        </div>
        <div class="mt-3">
          <small class="text-muted">
            <i class="bi bi-info-circle me-1"></i>
            Die importierten Daten wurden automatisch in die Tabellen geladen.
          </small>
        </div>
      </div>
    `;
  }

  showImportError(message) {
    const importResult = document.getElementById('importResult');
    if (!importResult) return;

    importResult.innerHTML = `
      <div class="alert alert-danger">
        <h6>
          <i class="bi bi-exclamation-triangle-fill me-2"></i>
          Import fehlgeschlagen
        </h6>
        <div class="mt-2">
          <strong>Fehler:</strong> ${Utils.sanitizeHtml(message)}
        </div>
        <div class="mt-3">
          <small class="text-muted">
            <strong>Mögliche Lösungen:</strong>
            <ul class="mb-0 mt-1">
              <li>Überprüfen Sie das Dateiformat (nur .xlsx und .xls unterstützt)</li>
              <li>Stellen Sie sicher, dass die Datei nicht beschädigt ist</li>
              <li>Versuchen Sie es mit einer kleineren Datei</li>
              <li>Überprüfen Sie Ihre Internetverbindung</li>
            </ul>
          </small>
        </div>
      </div>
    `;
  }

  showImportResult(message, type = 'info') {
    const importResult = document.getElementById('importResult');
    if (!importResult) return;

    const alertClass = `alert alert-${type}`;
    const icon = type === 'success' ? 'check-circle-fill' : 
                type === 'danger' ? 'exclamation-triangle-fill' : 
                'info-circle-fill';

    importResult.innerHTML = `
      <div class="${alertClass}">
        <i class="bi bi-${icon} me-2"></i>
        ${Utils.sanitizeHtml(message)}
      </div>
    `;
  }

  getSelectedFile() {
    return this.selectedFile;
  }

  isImportInProgress() {
    return this.state.importInProgress;
  }

  validateImportFile(file) {
    const errors = [];

    if (!file) {
      errors.push('Keine Datei ausgewählt');
      return { isValid: false, errors };
    }

    if (!Utils.isValidExcelFile(file)) {
      errors.push('Ungültiger Dateityp. Nur .xlsx und .xls Dateien sind erlaubt');
    }

    const maxSize = 500 * 1024 * 1024; // 500MB
    if (file.size > maxSize) {
      errors.push(`Datei zu groß. Maximum: ${Utils.formatFileSize(maxSize)}`);
    }

    const minSize = 1024; // 1KB
    if (file.size < minSize) {
      errors.push('Datei zu klein. Möglicherweise leer oder beschädigt');
    }

    return {
      isValid: errors.length === 0,
      errors,
      warnings: file.size > 50 * 1024 * 1024 ? ['Große Datei - Import kann lange dauern'] : []
    };
  }
}