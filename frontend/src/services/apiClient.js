/**
 * Centralized Axios API client for the Latent Nexus frontend.
 * 
 * This service provides a pre-configured Axios instance for making HTTP requests 
 * to the backend API. It includes a request interceptor that automatically 
 * injects the user's OAuth2 access token (retrieved from the AuthStore) into 
 * the Authorization header of every outgoing request, ensuring secure 
 * communication with the protected backend endpoints.
 */
import axios from 'axios';
import {useAuthStore} from '@/stores/authStore';

const apiClient = axios.create({
    baseURL: '/api/v1'
});

apiClient.interceptors.request.use((config) => {
    const authStore = useAuthStore();
    if (authStore.token) {
        config.headers.Authorization = `Bearer ${authStore.token}`;
    }
    return config;
});

export default apiClient;
