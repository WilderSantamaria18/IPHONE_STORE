/**
 * IphoneStore Service Worker
 * Provides offline functionality and caching for better performance
 */

const CACHE_NAME = 'iphone-store-v1.0';
const STATIC_CACHE_URLS = [
    '/',
    '/menu',
    '/login',
    '/modern-styles.css',
    '/app.js',
    '/manifest.json',
    'https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css',
    'https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css',
    'https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js'
];

const DYNAMIC_CACHE_URLS = [
    '/productos/listar',
    '/Cliente/listar',
    '/proveedores/listar',
    '/pedidos/listar',
    '/comprobantes/listar',
    '/usuarios/listar'
];

// Install event - cache static resources
self.addEventListener('install', event => {
    console.log('Service Worker: Installing...');
    
    event.waitUntil(
        caches.open(CACHE_NAME)
            .then(cache => {
                console.log('Service Worker: Caching static files');
                return cache.addAll(STATIC_CACHE_URLS);
            })
            .then(() => {
                console.log('Service Worker: Installation complete');
                return self.skipWaiting();
            })
            .catch(error => {
                console.error('Service Worker: Installation failed', error);
            })
    );
});

// Activate event - clean up old caches
self.addEventListener('activate', event => {
    console.log('Service Worker: Activating...');
    
    event.waitUntil(
        caches.keys()
            .then(cacheNames => {
                return Promise.all(
                    cacheNames
                        .filter(cacheName => cacheName !== CACHE_NAME)
                        .map(oldCache => {
                            console.log('Service Worker: Deleting old cache', oldCache);
                            return caches.delete(oldCache);
                        })
                );
            })
            .then(() => {
                console.log('Service Worker: Activation complete');
                return self.clients.claim();
            })
            .catch(error => {
                console.error('Service Worker: Activation failed', error);
            })
    );
});

// Fetch event - serve cached content when offline
self.addEventListener('fetch', event => {
    const { request } = event;
    
    // Skip non-GET requests
    if (request.method !== 'GET') {
        return;
    }
    
    // Skip chrome-extension requests
    if (request.url.startsWith('chrome-extension://')) {
        return;
    }
    
    event.respondWith(
        caches.match(request)
            .then(cachedResponse => {
                // Return cached version if available
                if (cachedResponse) {
                    console.log('Service Worker: Serving from cache', request.url);
                    return cachedResponse;
                }
                
                // Fetch from network
                return fetch(request)
                    .then(response => {
                        // Don't cache unsuccessful responses
                        if (!response || response.status !== 200 || response.type !== 'basic') {
                            return response;
                        }
                        
                        // Check if URL should be cached dynamically
                        const shouldCache = DYNAMIC_CACHE_URLS.some(url => 
                            request.url.includes(url)
                        ) || request.url.includes('/static/');
                        
                        if (shouldCache) {
                            const responseToCache = response.clone();
                            caches.open(CACHE_NAME)
                                .then(cache => {
                                    console.log('Service Worker: Caching new resource', request.url);
                                    cache.put(request, responseToCache);
                                })
                                .catch(error => {
                                    console.warn('Service Worker: Failed to cache', request.url, error);
                                });
                        }
                        
                        return response;
                    })
                    .catch(error => {
                        console.warn('Service Worker: Fetch failed', request.url, error);
                        
                        // Return offline page for navigation requests
                        if (request.mode === 'navigate') {
                            return caches.match('/menu') || 
                                   new Response('Offline - Please check your connection', {
                                       status: 200,
                                       headers: {'Content-Type': 'text/html'}
                                   });
                        }
                        
                        throw error;
                    });
            })
    );
});

// Background sync for offline actions
self.addEventListener('sync', event => {
    console.log('Service Worker: Background sync', event.tag);
    
    if (event.tag === 'background-sync') {
        event.waitUntil(doBackgroundSync());
    }
});

async function doBackgroundSync() {
    try {
        // Implement offline data synchronization here
        console.log('Service Worker: Performing background sync');
        
        // Example: sync offline form submissions
        const offlineData = await getOfflineData();
        if (offlineData.length > 0) {
            await syncOfflineData(offlineData);
        }
    } catch (error) {
        console.error('Service Worker: Background sync failed', error);
    }
}

async function getOfflineData() {
    // Get data stored while offline
    try {
        const cache = await caches.open('offline-data');
        const keys = await cache.keys();
        return keys.filter(key => key.url.includes('offline-'));
    } catch (error) {
        console.error('Service Worker: Failed to get offline data', error);
        return [];
    }
}

async function syncOfflineData(offlineData) {
    // Sync offline data with server
    for (const data of offlineData) {
        try {
            // Implement actual sync logic based on your API
            console.log('Service Worker: Syncing', data.url);
        } catch (error) {
            console.error('Service Worker: Failed to sync', data.url, error);
        }
    }
}

// Push notifications (if needed in the future)
self.addEventListener('push', event => {
    console.log('Service Worker: Push received');
    
    const options = {
        body: event.data ? event.data.text() : 'Nueva notificación de IphoneStore',
        icon: '/icons/icon-192x192.png',
        badge: '/icons/badge-72x72.png',
        vibrate: [100, 50, 100],
        data: {
            dateOfArrival: Date.now(),
            primaryKey: '1'
        },
        actions: [
            {
                action: 'explore',
                title: 'Ver',
                icon: '/icons/checkmark.png'
            },
            {
                action: 'close',
                title: 'Cerrar',
                icon: '/icons/xmark.png'
            }
        ]
    };
    
    event.waitUntil(
        self.registration.showNotification('IphoneStore', options)
    );
});

// Notification click handling
self.addEventListener('notificationclick', event => {
    console.log('Service Worker: Notification clicked');
    
    event.notification.close();
    
    if (event.action === 'explore') {
        event.waitUntil(
            clients.openWindow('/menu')
        );
    }
});

// Message handling from main thread
self.addEventListener('message', event => {
    console.log('Service Worker: Message received', event.data);
    
    if (event.data && event.data.type === 'SKIP_WAITING') {
        self.skipWaiting();
    }
    
    if (event.data && event.data.type === 'CACHE_URLS') {
        event.waitUntil(
            caches.open(CACHE_NAME)
                .then(cache => cache.addAll(event.data.payload))
        );
    }
});