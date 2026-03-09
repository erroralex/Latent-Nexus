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
                onLoad: 'login-required', // Forces login screen on app load
                checkLoginIframe: false
            };

            this.keycloak = new Keycloak(initOptions);

            try {
                const authenticated = await this.keycloak.init(initOptions);
                this.authenticated = authenticated;

                if (authenticated) {
                    this.token = this.keycloak.token;
                    this.userProfile = await this.keycloak.loadUserProfile();

                    // Set up periodic token refresh (every 60 seconds)
                    setInterval(() => {
                        this.keycloak.updateToken(70).then((refreshed) => {
                            if (refreshed) {
                                this.token = this.keycloak.token;
                            }
                        });
                    }, 60000);
                }
            } catch (error) {
                console.error('Keycloak initialization failed', error);
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