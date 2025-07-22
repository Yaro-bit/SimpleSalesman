// static/js/modules/table-manager.js
import { Utils } from './utils.js';

export class TableManager {
  constructor(appState, addressManager) {
    this.state = appState;
    this.addressManager = addressManager;
    this.renderDebounced = Utils.debounce(this.render.bind(this), 100);
  }

  render(type) {
    const config = this.getConfig(type);
    const data = this.filterAndSort(type);
    const paginated = this.paginate(data, type);
    
    this.renderTableBody(type, paginated, config);
    this.renderPagination(type, Math.ceil(data.length / this.state.tables[type].size));
    this.updateSortIndicators(type);
  }

  getConfig(type) {
    if (type === 'addresses') {
      return {
        columns: ['plz', 'street', 'number', 'region', 'projects', 'actions'],
        renderRow: (addr) => {
          const projects = this.renderProjectTags(addr.projects || []);
          return [
            Utils.extractPlz(addr) || '-',
            Utils.extractStreet(addr) || '-',
            Utils.extractNumber(addr) || '-',
            addr.regionName || '-',
            projects || '-',
            this.renderAddressActions(addr)
          ];
        }
      };
    } else {
      return {
        columns: ['id', 'address', 'text', 'createdBy', 'createdAt', 'actions'],
        renderRow: (note) => {
          const addressStr = Utils.getAddressString(note.address);
          const dateStr = Utils.formatDate(note.createdAt);
          const textPreview = this.createTextPreview(note.text);
          return [
            note.id || '-',
            addressStr,
            textPreview,
            note.createdBy || '-',
            dateStr,
            this.renderNoteActions(note)
          ];
        }
      };
    }
  }

  renderProjectTags(projects) {
    if (!projects || projects.length === 0) {
      return '<span class="text-muted">-</span>';
    }

    return projects.map(project => {
      const status = project.status || 'unknown';
      const statusClass = Utils.getProjectStatusClass(status);
      const statusIcon = Utils.getProjectStatusIcon(status);
      
      return `
        <span class="project-tag ${statusClass}" title="Status: ${status}">
          <i class="bi ${statusIcon} me-1"></i>
          ${status}
        </span>
      `;
    }).join('');
  }

  renderAddressActions(addr) {
    return `
      <div class="btn-group" role="group">
        <button onclick="window.modalManager.openNotesModal(${addr.id})" 
                class="btn btn-primary btn-sm" 
                title="Notizen anzeigen">
          <i class="bi bi-journal-text"></i>
        </button>
      </div>
    `;
  }

  renderNoteActions(note) {
    return `
      <div class="btn-group" role="group">
        <button onclick="window.modalManager.openNotesModal(${note.address?.id})" 
                class="btn btn-primary btn-sm" 
                title="Alle Notizen anzeigen">
          <i class="bi bi-eye"></i>
        </button>
        <button onclick="window.notesManager.editNote(${note.id})" 
                class="btn btn-outline-secondary btn-sm" 
                title="Bearbeiten">
          <i class="bi bi-pencil"></i>
        </button>
        <button onclick="window.notesManager.deleteNoteWithConfirm(${note.id})" 
                class="btn btn-outline-danger btn-sm" 
                title="Löschen">
          <i class="bi bi-trash"></i>
        </button>
      </div>
    `;
  }

  createTextPreview(text, maxLength = 80) {
    if (!text) return '<span class="text-muted">Kein Text</span>';
    
    const sanitizedText = Utils.sanitizeHtml(text);
    if (sanitizedText.length <= maxLength) {
      return `<span title="${sanitizedText}">${sanitizedText}</span>`;
    }
    
    const preview = sanitizedText.substring(0, maxLength) + '...';
    return `<span title="${sanitizedText}" class="text-truncate">${preview}</span>`;
  }

  filterAndSort(type) {
    const tableState = this.state.tables[type];
    const data = type === 'addresses' ? this.state.addresses : this.state.notes;
    
    let filtered = data;
    if (tableState.search) {
      filtered = this.applySearchFilter(data, tableState.search, type);
    }

    return this.applySorting(filtered, tableState, type);
  }

  applySearchFilter(data, searchTerm, type) {
    const normalizedSearch = Utils.normalizeSearchText(searchTerm);
    
    return data.filter(item => {
      let searchableText;
      
      if (type === 'addresses') {
        searchableText = [
          Utils.extractPlz(item),
          Utils.extractStreet(item),
          Utils.extractNumber(item),
          item.regionName || '',
          item.addressText || ''
        ].join(' ');
      } else {
        searchableText = [
          item.text || '',
          item.createdBy || '',
          Utils.getAddressString(item.address),
          item.id?.toString() || ''
        ].join(' ');
      }

      return Utils.normalizeSearchText(searchableText).includes(normalizedSearch);
    });
  }

  applySorting(data, tableState, type) {
    return [...data].sort((a, b) => {
      let aVal, bVal;

      if (type === 'addresses') {
        switch (tableState.sort) {
          case 'plz':
            aVal = Utils.extractPlz(a) || '';
            bVal = Utils.extractPlz(b) || '';
            break;
          case 'street':
            aVal = Utils.extractStreet(a) || '';
            bVal = Utils.extractStreet(b) || '';
            break;
          case 'number':
            aVal = Utils.extractNumber(a) || '';
            bVal = Utils.extractNumber(b) || '';
            break;
          case 'region':
            aVal = a.regionName || '';
            bVal = b.regionName || '';
            break;
          default:
            aVal = a.id || 0;
            bVal = b.id || 0;
        }
      } else {
        switch (tableState.sort) {
          case 'id':
            aVal = a.id || 0;
            bVal = b.id || 0;
            break;
          case 'address':
            aVal = Utils.getAddressString(a.address);
            bVal = Utils.getAddressString(b.address);
            break;
          case 'text':
            aVal = a.text || '';
            bVal = b.text || '';
            break;
          case 'createdBy':
            aVal = a.createdBy || '';
            bVal = b.createdBy || '';
            break;
          case 'createdAt':
            aVal = new Date(a.createdAt || 0);
            bVal = new Date(b.createdAt || 0);
            break;
          default:
            aVal = a.id || 0;
            bVal = b.id || 0;
        }
      }

      if (tableState.sort === 'createdAt' || tableState.sort === 'id') {
        const comparison = aVal - bVal;
        return tableState.order === 'asc' ? comparison : -comparison;
      } else if (tableState.sort === 'number') {
        const aNum = parseInt(aVal) || 0;
        const bNum = parseInt(bVal) || 0;
        if (aNum !== bNum) {
          return tableState.order === 'asc' ? aNum - bNum : bNum - aNum;
        }
        const comparison = aVal.toString().localeCompare(bVal.toString(), 'de', { numeric: true });
        return tableState.order === 'asc' ? comparison : -comparison;
      } else {
        const comparison = aVal.toString().localeCompare(bVal.toString(), 'de', { numeric: true });
        return tableState.order === 'asc' ? comparison : -comparison;
      }
    });
  }

  paginate(data, type) {
    const tableState = this.state.tables[type];
    const start = (tableState.page - 1) * tableState.size;
    return data.slice(start, start + tableState.size);
  }

  renderTableBody(type, data, config) {
    const tbody = document.querySelector(`#${type}Table tbody`);
    if (!tbody) return;

    tbody.innerHTML = '';

    if (data.length === 0) {
      this.renderEmptyState(tbody, config.columns.length, type);
      return;
    }

    data.forEach((item, index) => {
      const row = document.createElement('tr');
      row.className = 'fade-in';
      row.style.animationDelay = `${index * 50}ms`;
      
      const cells = config.renderRow(item);
      row.innerHTML = cells.map(cell => `<td>${cell}</td>`).join('');
      
      tbody.appendChild(row);
    });
  }

  renderEmptyState(tbody, colspan, type) {
    const emptyMessage = type === 'addresses' ? 'Keine Adressen gefunden' : 'Keine Notizen gefunden';
    const icon = type === 'addresses' ? 'bi-house' : 'bi-journal-x';
    
    tbody.innerHTML = `
      <tr>
        <td colspan="${colspan}" class="text-center p-5">
          <div class="text-muted">
            <i class="bi ${icon}" style="font-size: 3rem; opacity: 0.3;"></i>
            <div class="mt-3">
              <h6>${emptyMessage}</h6>
              <small>Versuchen Sie es mit anderen Suchbegriffen</small>
            </div>
          </div>
        </td>
      </tr>
    `;
  }

  renderPagination(type, totalPages) {
    const pagination = document.getElementById(`${type}Pagination`);
    if (!pagination) return;
    
    pagination.innerHTML = '';
    
    if (totalPages <= 1) {
      pagination.style.display = 'none';
      return;
    }

    pagination.style.display = 'flex';
    pagination.className = 'pagination justify-content-center mt-4';

    const tableState = this.state.tables[type];
    
    this.addPaginationButton(pagination, '‹', tableState.page - 1, tableState.page === 1, type);
    
    const { start, end } = this.calculateVisiblePages(tableState.page, totalPages);
    
    if (start > 1) {
      this.addPaginationButton(pagination, '1', 1, false, type);
      if (start > 2) {
        this.addPaginationEllipsis(pagination);
      }
    }

    for (let page = start; page <= end; page++) {
      this.addPaginationButton(pagination, page.toString(), page, false, type, page === tableState.page);
    }

    if (end < totalPages) {
      if (end < totalPages - 1) {
        this.addPaginationEllipsis(pagination);
      }
      this.addPaginationButton(pagination, totalPages.toString(), totalPages, false, type);
    }
    
    this.addPaginationButton(pagination, '›', tableState.page + 1, tableState.page === totalPages, type);
  }

  calculateVisiblePages(currentPage, totalPages, maxVisible = 5) {
    let start = Math.max(1, currentPage - Math.floor(maxVisible / 2));
    let end = Math.min(totalPages, start + maxVisible - 1);
    
    if (end - start + 1 < maxVisible) {
      start = Math.max(1, end - maxVisible + 1);
    }
    
    return { start, end };
  }

  addPaginationButton(pagination, text, page, disabled, type, active = false) {
    const li = document.createElement('li');
    li.className = `page-item ${disabled ? 'disabled' : ''} ${active ? 'active' : ''}`;
    
    const link = document.createElement('a');
    link.className = 'page-link';
    link.href = '#';
    link.textContent = text;
    
    if (!disabled) {
      link.onclick = (e) => {
        e.preventDefault();
        this.changePage(type, page);
      };
    }
    
    li.appendChild(link);
    pagination.appendChild(li);
  }

  addPaginationEllipsis(pagination) {
    const li = document.createElement('li');
    li.className = 'page-item disabled';
    li.innerHTML = '<span class="page-link">…</span>';
    pagination.appendChild(li);
  }

  updateSortIndicators(type) {
    const tableState = this.state.tables[type];
    
    document.querySelectorAll(`#${type}Table th`).forEach(th => {
      th.classList.remove('sorted-asc', 'sorted-desc');
      
      const icon = th.querySelector('.sort-icon');
      if (icon) {
        icon.className = 'bi bi-arrow-down-up sort-icon ms-1';
      }
    });

    const currentSortTh = document.querySelector(`#${type}Table th[data-sort="${tableState.sort}"]`);
    if (currentSortTh) {
      currentSortTh.classList.add(`sorted-${tableState.order}`);
      
      const icon = currentSortTh.querySelector('.sort-icon');
      if (icon) {
        icon.className = `bi bi-arrow-${tableState.order === 'asc' ? 'up' : 'down'} sort-icon ms-1`;
      }
    }
  }

  sortTable(type, field) {
    const tableState = this.state.tables[type];
    
    if (tableState.sort === field) {
      tableState.order = tableState.order === 'asc' ? 'desc' : 'asc';
    } else {
      tableState.sort = field;
      tableState.order = (field === 'createdAt' || field === 'id') ? 'desc' : 'asc';
    }
    
    tableState.page = 1;
    this.state.updateTableState(type, tableState);
    this.render(type);
  }

  filterTable(type, searchValue) {
    const tableState = this.state.tables[type];
    tableState.search = searchValue;
    tableState.page = 1;
    this.state.updateTableState(type, tableState);
    this.renderDebounced(type);
  }

  changePageSize(type, size) {
    const tableState = this.state.tables[type];
    tableState.size = parseInt(size);
    tableState.page = 1;
    this.state.updateTableState(type, tableState);
    this.render(type);
  }

  changePage(type, page) {
    if (page < 1) return;
    
    const tableState = this.state.tables[type];
    const data = this.filterAndSort(type);
    const maxPage = Math.ceil(data.length / tableState.size);
    
    if (page > maxPage) return;
    
    tableState.page = page;
    this.state.updateTableState(type, tableState);
    this.render(type);
  }
}