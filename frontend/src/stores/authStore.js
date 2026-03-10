/**
 * Pinia store for managing user authentication and profile state via Keycloak.
 * 
 * This store handles the initialization of the Keycloak JS adapter, manages the 
 * user's authentication status, stores the OAuth2 access token, and retrieves 
 * the user's profile information. It also implements a periodic token refresh 
 * mechanism to ensure that the application maintains a valid session with the 
 * authentication server.
 */
import {defineStore} from 'pinia';
import Keycloak from 'keycloak-js';

export const useAuthStore = defineStore('auth', {
    state: () => ({
        keycloak: null,
        authenticated: false,
        userProfile: null,
        token: null
    }),

    actions: {
        async initAuth() {
            const initOptions = {
                url: 'http://localhost:8080',
                realm: 'latent-nexus',
                clientId: 'latent-nexus-frontend',
                onLoad: 'login-required',
                checkLoginIframe: false,
                pkceMethod: 'S256'
            };

            this.keycloak = new Keycloak(initOptions);

            try {
                const authenticated = await this.keycloak.init({
                    onLoad: 'login-required',
                    checkLoginIframe: false,
                    pkceMethod: 'S256'
                });
                
                this.authenticated = authenticated;

                if (authenticated) {
                    this.token = this.keycloak.token;
                    this.userProfile = await this.keycloak.loadUserProfile();

                    setInterval(() => {
                        this.keycloak.updateToken(70).then((refreshed) => {
                            if (refreshed) {
                                this.token = this.keycloak.token;
                            }
                        }).catch(() => {
                            console.error('Failed to refresh token');
                        });
                    }, 60000);
                }
            } catch (error) {
                console.error('Keycloak initialization failed. Ensure the realm and client exist.', error);
            }
        },

        logout() {
            if (this.keycloak) {
                this.keycloak.logout({redirectUri: window.location.origin});
            }
        }
    },

    getters: {
        isAdmin: (state) => state.keycloak?.hasRealmRole('ADMIN') || false,
        username: (state) => state.userProfile?.username || 'Guest'
    }
});
