/**
 * iPhone Store - Modern Frontend JavaScript
 * Enhanced user experience with modern interactions
 */

class iPhoneStoreApp {
    constructor() {
        this.init();
    }

    init() {
        this.setupThemeToggle();
        this.setupFormValidation();
        this.setupTableEnhancements();
        this.setupNotifications();
        this.setupLoadingStates();
        this.setupSearchAndFilter();
        this.setupAnimations();
        this.setupKeyboardShortcuts();
        this.setupMobileOptimizations();
    }

    // Theme Management
    setupThemeToggle() {
        const toggleButton = document.querySelector('.theme-toggle');
        if (!toggleButton) return;

        const currentTheme = localStorage.getItem('theme') || 'light';
        document.documentElement.setAttribute('data-theme', currentTheme);
        this.updateThemeIcon(currentTheme);

        toggleButton.addEventListener('click', () => {
            const newTheme = document.documentElement.getAttribute('data-theme') === 'light' ? 'dark' : 'light';
            document.documentElement.setAttribute('data-theme', newTheme);
            localStorage.setItem('theme', newTheme);
            this.updateThemeIcon(newTheme);
        });
    }

    updateThemeIcon(theme) {
        const icon = document.querySelector('.theme-toggle i');
        if (icon) {
            icon.className = theme === 'light' ? 'bi bi-moon' : 'bi bi-sun';
        }
    }

    // Enhanced Form Validation
    setupFormValidation() {
        const forms = document.querySelectorAll('form');
        
        forms.forEach(form => {
            const inputs = form.querySelectorAll('input, select, textarea');
            
            inputs.forEach(input => {
                // Real-time validation
                input.addEventListener('input', () => this.validateField(input));
                input.addEventListener('blur', () => this.validateField(input));
                
                // Enhanced user feedback
                this.setupFieldFeedback(input);
            });

            // Form submission with loading state
            form.addEventListener('submit', (e) => this.handleFormSubmit(e, form));
        });
    }

    validateField(field) {
        const value = field.value.trim();
        const type = field.type;
        const isRequired = field.hasAttribute('required');
        
        let isValid = true;
        let errorMessage = '';

        // Required validation
        if (isRequired && !value) {
            isValid = false;
            errorMessage = 'Este campo es obligatorio';
        }
        
        // Type-specific validation
        if (value && !this.validateFieldType(field)) {
            isValid = false;
            errorMessage = this.getTypeErrorMessage(type);
        }

        this.showFieldValidation(field, isValid, errorMessage);
        return isValid;
    }

    validateFieldType(field) {
        const value = field.value.trim();
        const type = field.type;

        switch (type) {
            case 'email':
                return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(value);
            case 'tel':
                return /^[\d\s\-\+\(\)]+$/.test(value);
            case 'number':
                return !isNaN(value) && value !== '';
            default:
                return true;
        }
    }

    getTypeErrorMessage(type) {
        const messages = {
            email: 'Ingrese un email válido',
            tel: 'Ingrese un teléfono válido',
            number: 'Ingrese un número válido'
        };
        return messages[type] || 'Valor inválido';
    }

    setupFieldFeedback(field) {
        const wrapper = document.createElement('div');
        wrapper.className = 'field-wrapper';
        field.parentNode.insertBefore(wrapper, field);
        wrapper.appendChild(field);

        const feedback = document.createElement('div');
        feedback.className = 'field-feedback';
        wrapper.appendChild(feedback);
    }

    showFieldValidation(field, isValid, message) {
        const wrapper = field.parentNode;
        const feedback = wrapper.querySelector('.field-feedback');
        
        field.classList.toggle('is-valid', isValid && field.value.trim());
        field.classList.toggle('is-invalid', !isValid);
        
        if (feedback) {
            feedback.textContent = message;
            feedback.className = `field-feedback ${isValid ? 'text-success' : 'text-danger'}`;
        }
    }

    handleFormSubmit(e, form) {
        const submitButton = form.querySelector('button[type="submit"]');
        const inputs = form.querySelectorAll('input, select, textarea');
        
        let isFormValid = true;
        inputs.forEach(input => {
            if (!this.validateField(input)) {
                isFormValid = false;
            }
        });

        if (!isFormValid) {
            e.preventDefault();
            this.showNotification('Por favor, corrija los errores en el formulario', 'error');
            return;
        }

        if (submitButton) {
            submitButton.classList.add('btn-loading');
            submitButton.disabled = true;
        }
    }

    // Table Enhancements
    setupTableEnhancements() {
        const tables = document.querySelectorAll('table');
        
        tables.forEach(table => {
            this.makeTableResponsive(table);
            this.addTableSorting(table);
            this.addTableSearch(table);
        });
    }

    makeTableResponsive(table) {
        const wrapper = document.createElement('div');
        wrapper.className = 'table-responsive-wrapper';
        table.parentNode.insertBefore(wrapper, table);
        wrapper.appendChild(table);

        // Add horizontal scroll indicators
        this.addScrollIndicators(wrapper);
    }

    addScrollIndicators(wrapper) {
        const leftIndicator = document.createElement('div');
        leftIndicator.className = 'scroll-indicator scroll-indicator-left';
        leftIndicator.innerHTML = '<i class="bi bi-chevron-left"></i>';

        const rightIndicator = document.createElement('div');
        rightIndicator.className = 'scroll-indicator scroll-indicator-right';
        rightIndicator.innerHTML = '<i class="bi bi-chevron-right"></i>';

        wrapper.appendChild(leftIndicator);
        wrapper.appendChild(rightIndicator);

        wrapper.addEventListener('scroll', () => {
            const { scrollLeft, scrollWidth, clientWidth } = wrapper;
            leftIndicator.style.opacity = scrollLeft > 0 ? '1' : '0';
            rightIndicator.style.opacity = scrollLeft < scrollWidth - clientWidth ? '1' : '0';
        });

        // Initial check
        setTimeout(() => {
            wrapper.dispatchEvent(new Event('scroll'));
        }, 100);
    }

    addTableSorting(table) {
        const headers = table.querySelectorAll('th');
        
        headers.forEach((header, index) => {
            if (header.textContent.trim()) {
                header.style.cursor = 'pointer';
                header.innerHTML += ' <i class="bi bi-arrow-down-up sort-icon"></i>';
                
                header.addEventListener('click', () => {
                    this.sortTable(table, index);
                });
            }
        });
    }

    sortTable(table, column) {
        const tbody = table.querySelector('tbody');
        const rows = Array.from(tbody.querySelectorAll('tr'));
        const header = table.querySelectorAll('th')[column];
        const isAscending = !header.classList.contains('sort-asc');

        // Reset all headers
        table.querySelectorAll('th').forEach(th => {
            th.classList.remove('sort-asc', 'sort-desc');
            const icon = th.querySelector('.sort-icon');
            if (icon) icon.className = 'bi bi-arrow-down-up sort-icon';
        });

        // Set current header
        header.classList.add(isAscending ? 'sort-asc' : 'sort-desc');
        const icon = header.querySelector('.sort-icon');
        if (icon) {
            icon.className = `bi bi-arrow-${isAscending ? 'up' : 'down'} sort-icon`;
        }

        // Sort rows
        rows.sort((a, b) => {
            const aVal = a.cells[column].textContent.trim();
            const bVal = b.cells[column].textContent.trim();
            
            const comparison = isNaN(aVal) || isNaN(bVal) 
                ? aVal.localeCompare(bVal)
                : parseFloat(aVal) - parseFloat(bVal);
                
            return isAscending ? comparison : -comparison;
        });

        // Reappend sorted rows
        rows.forEach(row => tbody.appendChild(row));
    }

    addTableSearch(table) {
        const wrapper = table.closest('.table-responsive-wrapper') || table.parentNode;
        
        const searchWrapper = document.createElement('div');
        searchWrapper.className = 'table-search-wrapper mb-3';
        searchWrapper.innerHTML = `
            <div class="input-group">
                <span class="input-group-text">
                    <i class="bi bi-search"></i>
                </span>
                <input type="text" class="form-control" placeholder="Buscar en la tabla...">
                <button class="btn btn-outline-secondary" type="button" title="Limpiar búsqueda">
                    <i class="bi bi-x-lg"></i>
                </button>
            </div>
        `;

        wrapper.parentNode.insertBefore(searchWrapper, wrapper);

        const searchInput = searchWrapper.querySelector('input');
        const clearButton = searchWrapper.querySelector('button');

        searchInput.addEventListener('input', (e) => {
            this.filterTable(table, e.target.value);
        });

        clearButton.addEventListener('click', () => {
            searchInput.value = '';
            this.filterTable(table, '');
            searchInput.focus();
        });
    }

    filterTable(table, searchTerm) {
        const tbody = table.querySelector('tbody');
        const rows = tbody.querySelectorAll('tr');
        const term = searchTerm.toLowerCase();

        rows.forEach(row => {
            const text = row.textContent.toLowerCase();
            row.style.display = text.includes(term) ? '' : 'none';
        });

        // Update empty state
        const visibleRows = tbody.querySelectorAll('tr:not([style*="display: none"])');
        let emptyRow = tbody.querySelector('.empty-search-row');
        
        if (visibleRows.length === 0 && searchTerm) {
            if (!emptyRow) {
                emptyRow = document.createElement('tr');
                emptyRow.className = 'empty-search-row';
                emptyRow.innerHTML = `
                    <td colspan="100%" class="text-center text-muted py-4">
                        <i class="bi bi-search"></i><br>
                        No se encontraron resultados para "${searchTerm}"
                    </td>
                `;
                tbody.appendChild(emptyRow);
            }
        } else if (emptyRow) {
            emptyRow.remove();
        }
    }

    // Notification System
    setupNotifications() {
        // Create notification container
        const container = document.createElement('div');
        container.id = 'notification-container';
        container.className = 'notification-container';
        document.body.appendChild(container);
    }

    showNotification(message, type = 'info', duration = 5000) {
        const container = document.getElementById('notification-container');
        if (!container) return;

        const notification = document.createElement('div');
        notification.className = `notification notification-${type} slide-up`;
        
        const icons = {
            success: 'bi-check-circle',
            error: 'bi-x-circle',
            warning: 'bi-exclamation-triangle',
            info: 'bi-info-circle'
        };

        notification.innerHTML = `
            <i class="bi ${icons[type] || icons.info}"></i>
            <span>${message}</span>
            <button class="notification-close" type="button">
                <i class="bi bi-x"></i>
            </button>
        `;

        container.appendChild(notification);

        // Auto-remove
        const autoRemove = setTimeout(() => this.removeNotification(notification), duration);

        // Manual close
        notification.querySelector('.notification-close').addEventListener('click', () => {
            clearTimeout(autoRemove);
            this.removeNotification(notification);
        });
    }

    removeNotification(notification) {
        notification.style.animation = 'slideOut 0.3s ease forwards';
        setTimeout(() => {
            if (notification.parentNode) {
                notification.parentNode.removeChild(notification);
            }
        }, 300);
    }

    // Loading States
    setupLoadingStates() {
        // Handle link clicks with loading states
        document.addEventListener('click', (e) => {
            if (e.target.matches('a[href]:not([href^="#"]):not([target="_blank"])')) {
                const link = e.target;
                if (!link.classList.contains('no-loading')) {
                    this.showPageLoading();
                }
            }
        });

        // Hide loading on page load
        window.addEventListener('load', () => {
            this.hidePageLoading();
        });
    }

    showPageLoading() {
        let loader = document.getElementById('page-loader');
        if (!loader) {
            loader = document.createElement('div');
            loader.id = 'page-loader';
            loader.className = 'page-loader';
            loader.innerHTML = `
                <div class="loader-content">
                    <div class="loader-spinner"></div>
                    <div class="loader-text">Cargando...</div>
                </div>
            `;
            document.body.appendChild(loader);
        }
        loader.style.display = 'flex';
    }

    hidePageLoading() {
        const loader = document.getElementById('page-loader');
        if (loader) {
            loader.style.display = 'none';
        }
    }

    // Search and Filter
    setupSearchAndFilter() {
        const searchInputs = document.querySelectorAll('.global-search');
        
        searchInputs.forEach(input => {
            let debounceTimer;
            input.addEventListener('input', (e) => {
                clearTimeout(debounceTimer);
                debounceTimer = setTimeout(() => {
                    this.performGlobalSearch(e.target.value);
                }, 300);
            });
        });
    }

    performGlobalSearch(term) {
        // This would typically make an AJAX request to the server
        // For now, we'll just show a notification
        if (term.length > 2) {
            console.log(`Searching for: ${term}`);
        }
    }

    // Animations
    setupAnimations() {
        // Intersection Observer for scroll animations
        const observerOptions = {
            threshold: 0.1,
            rootMargin: '0px 0px -50px 0px'
        };

        const observer = new IntersectionObserver((entries) => {
            entries.forEach(entry => {
                if (entry.isIntersecting) {
                    entry.target.classList.add('animate-in');
                }
            });
        }, observerOptions);

        // Observe elements with animation classes
        document.querySelectorAll('.fade-in, .slide-up').forEach(el => {
            observer.observe(el);
        });
    }

    // Keyboard Shortcuts
    setupKeyboardShortcuts() {
        document.addEventListener('keydown', (e) => {
            // Ctrl/Cmd + K for search
            if ((e.ctrlKey || e.metaKey) && e.key === 'k') {
                e.preventDefault();
                const searchInput = document.querySelector('.global-search, .table-search-wrapper input');
                if (searchInput) {
                    searchInput.focus();
                }
            }

            // Escape to close modals/notifications
            if (e.key === 'Escape') {
                const activeModal = document.querySelector('.modal.show');
                if (activeModal) {
                    const closeButton = activeModal.querySelector('[data-bs-dismiss="modal"]');
                    if (closeButton) closeButton.click();
                }

                const notifications = document.querySelectorAll('.notification');
                notifications.forEach(notification => {
                    const closeButton = notification.querySelector('.notification-close');
                    if (closeButton) closeButton.click();
                });
            }
        });
    }

    // Mobile Optimizations
    setupMobileOptimizations() {
        // Touch-friendly interactions
        if ('ontouchstart' in window) {
            document.body.classList.add('touch-device');
            
            // Add swipe gestures to tables
            this.setupSwipeGestures();
        }

        // Viewport height fix for mobile
        this.setViewportHeight();
        window.addEventListener('resize', () => this.setViewportHeight());
    }

    setViewportHeight() {
        document.documentElement.style.setProperty('--vh', `${window.innerHeight * 0.01}px`);
    }

    setupSwipeGestures() {
        const tables = document.querySelectorAll('.table-responsive-wrapper');
        
        tables.forEach(table => {
            let startX = 0;
            let scrollLeft = 0;

            table.addEventListener('touchstart', (e) => {
                startX = e.touches[0].pageX - table.offsetLeft;
                scrollLeft = table.scrollLeft;
            });

            table.addEventListener('touchmove', (e) => {
                if (!startX) return;
                e.preventDefault();
                const x = e.touches[0].pageX - table.offsetLeft;
                const walk = (x - startX) * 2;
                table.scrollLeft = scrollLeft - walk;
            });

            table.addEventListener('touchend', () => {
                startX = 0;
            });
        });
    }
}

// CSS for additional components
const additionalStyles = `
    .field-wrapper {
        position: relative;
        margin-bottom: 1rem;
    }

    .field-feedback {
        font-size: 0.875rem;
        margin-top: 0.25rem;
    }

    .table-responsive-wrapper {
        position: relative;
        overflow-x: auto;
        -webkit-overflow-scrolling: touch;
    }

    .scroll-indicator {
        position: absolute;
        top: 50%;
        transform: translateY(-50%);
        background: rgba(0, 0, 0, 0.5);
        color: white;
        padding: 0.5rem;
        border-radius: 50%;
        opacity: 0;
        transition: opacity 0.3s;
        z-index: 10;
        pointer-events: none;
    }

    .scroll-indicator-left {
        left: 0.5rem;
    }

    .scroll-indicator-right {
        right: 0.5rem;
    }

    .sort-icon {
        font-size: 0.75rem;
        margin-left: 0.25rem;
        opacity: 0.5;
        transition: opacity 0.2s;
    }

    th:hover .sort-icon {
        opacity: 1;
    }

    .notification-container {
        position: fixed;
        top: 1rem;
        right: 1rem;
        z-index: 1050;
        max-width: 400px;
    }

    .notification {
        background: white;
        border: 1px solid var(--border-color);
        border-radius: var(--radius-md);
        padding: 1rem;
        margin-bottom: 0.5rem;
        box-shadow: var(--shadow-lg);
        display: flex;
        align-items: center;
        gap: 0.75rem;
    }

    .notification-success {
        border-left: 4px solid var(--success-color);
    }

    .notification-error {
        border-left: 4px solid var(--danger-color);
    }

    .notification-warning {
        border-left: 4px solid var(--warning-color);
    }

    .notification-info {
        border-left: 4px solid var(--info-color);
    }

    .notification-close {
        background: none;
        border: none;
        color: var(--text-secondary);
        cursor: pointer;
        margin-left: auto;
        padding: 0.25rem;
        border-radius: 50%;
        transition: background-color 0.2s;
    }

    .notification-close:hover {
        background-color: var(--bg-secondary);
    }

    .page-loader {
        position: fixed;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
        background: rgba(255, 255, 255, 0.9);
        display: flex;
        align-items: center;
        justify-content: center;
        z-index: 9999;
    }

    .loader-content {
        text-align: center;
    }

    .loader-spinner {
        width: 40px;
        height: 40px;
        border: 3px solid var(--border-color);
        border-top-color: var(--primary-color);
        border-radius: 50%;
        animation: spin 1s linear infinite;
        margin: 0 auto 1rem;
    }

    .loader-text {
        color: var(--text-secondary);
        font-weight: 500;
    }

    @keyframes slideOut {
        to {
            transform: translateX(100%);
            opacity: 0;
        }
    }

    .touch-device .btn,
    .touch-device .nav-link {
        min-height: 44px;
        min-width: 44px;
        display: flex;
        align-items: center;
        justify-content: center;
    }

    @media (max-width: 768px) {
        .notification-container {
            left: 1rem;
            right: 1rem;
            max-width: none;
        }
    }
`;

// Inject additional styles
const styleSheet = document.createElement('style');
styleSheet.textContent = additionalStyles;
document.head.appendChild(styleSheet);

// Initialize the app when DOM is loaded
document.addEventListener('DOMContentLoaded', () => {
    new iPhoneStoreApp();
});

// Export for potential external use
window.iPhoneStoreApp = iPhoneStoreApp;