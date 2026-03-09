/**
 * Main entry point for the Latent Nexus frontend application.
 * 
 * This script initializes the Vue.js application, sets up the Pinia state management 
 * store, and configures the Vue Router. It also triggers the authentication 
 * initialization process via the AuthStore, ensuring that the application is 
 * only mounted once the user's authentication state (via Keycloak) has been 
 * successfully resolved.
 */
import {createApp} from 'vue';
import {createPinia} from 'pinia';
import App from './App.vue';
import router from './router';
import {useAuthStore} from './stores/authStore';

const app = createApp(App);
const pinia = createPinia();

app.use(pinia);
app.use(router);

const authStore = useAuthStore();

authStore.initAuth().then(() => {
    app.mount('#app');
});
