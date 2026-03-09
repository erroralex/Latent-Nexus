/**
 * Pinia store for managing AI-generated assets and their associated operations.
 * 
 * This store centralizes the state and logic for interacting with the asset-related 
 * backend endpoints. It manages the list of assets for the current workspace, 
 * tracks loading and uploading states, and handles error reporting. It provides 
 * actions for fetching paginated asset lists and uploading new files to the 
 * secure vault.
 */
import { defineStore } from 'pinia';
import apiClient from '@/services/apiClient';

export const useAssetStore = defineStore('assets', {
    state: () => ({
        assets: [],
        loading: false,
        uploading: false,
        error: null
    }),

    actions: {
        async fetchAssets(workspaceId) {
            this.loading = true;
            this.error = null;
            try {
                const response = await apiClient.get(`/workspaces/${workspaceId}/assets`);
                this.assets = response.data;
            } catch (err) {
                this.error = 'Failed to load assets.';
                console.error(err);
            } finally {
                this.loading = false;
            }
        },

        async uploadAsset(workspaceId, file) {
            this.uploading = true;
            const formData = new FormData();
            formData.append('file', file);

            try {
                const response = await apiClient.post(
                    `/workspaces/${workspaceId}/assets`,
                    formData,
                    {
                        headers: { 'Content-Type': 'multipart/form-data' }
                    }
                );
                this.assets.push(response.data);
                return response.data;
            } catch (err) {
                this.error = 'Upload failed.';
                throw err;
            } finally {
                this.uploading = false;
            }
        }
    }
});
