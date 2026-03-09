import axios from 'axios';
import {useAuthStore} from '@/stores/authStore';

const apiClient = axios.create({
    baseURL: '/api/v1' // Proxied via vite.config.js
});

apiClient.interceptors.request.use((config) => {
    const authStore = useAuthStore();
    if (authStore.token) {
        config.headers.Authorization = `Bearer ${authStore.token}`;
    }
    return config;
});

export default apiClient;