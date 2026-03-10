<!--
  Workspace Selection view component.
-->
<script setup>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import apiClient from '@/services/apiClient';

const workspaces = ref([]);
const loading = ref(true);
const router = useRouter();

onMounted(async () => {
  try {
    const response = await apiClient.get('/workspaces');
    console.log("Workspaces received from backend:", response.data);
    workspaces.value = response.data;
  } catch (error) {
    console.error("Failed to fetch workspaces", error);
  } finally {
    loading.value = false;
  }
});

const selectWorkspace = (id) => {
  router.push(`/workspaces/${id}`);
};
</script>

<template>
  <div class="selection-container">
    <header class="view-header">
      <h1>Select <span class="accent">Workspace</span></h1>
      <p>Choose an environment to manage your AI assets</p>
    </header>

    <div v-if="loading" class="state-msg">
      <i class="pi pi-spin pi-spinner"></i> Initializing Environment...
    </div>

    <div v-else-if="workspaces.length === 0" class="state-msg">
      <p>No workspaces found. Contact an administrator to provision your vault.</p>
    </div>

    <div v-else class="workspace-grid">
      <div
          v-for="ws in workspaces"
          :key="ws.id"
          class="ws-card"
          @click="selectWorkspace(ws.id)"
      >
        <div class="ws-icon">
          <i class="pi pi-box"></i>
        </div>
        <div class="ws-details">
          <h3>{{ ws.name }}</h3>
          <p>{{ ws.description || 'Enterprise AI Asset Silo' }}</p>
        </div>
        <i class="pi pi-chevron-right arrow"></i>
      </div>
    </div>
  </div>
</template>

<style scoped>
.selection-container {
  max-width: 800px;
  margin: 4rem auto;
  padding: 0 2rem;
  color: #e2e8f0;
}

.view-header {
  text-align: center;
  margin-bottom: 3rem;
}

h1 { font-size: 2.5rem; margin-bottom: 0.5rem; }
.accent { color: #3b82f6; }

.workspace-grid {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.ws-card {
  background: #1e293b;
  border: 1px solid #334155;
  padding: 1.5rem;
  border-radius: 0.75rem;
  display: flex;
  align-items: center;
  gap: 1.5rem;
  cursor: pointer;
  transition: all 0.2s;
}

.ws-card:hover {
  border-color: #3b82f6;
  background: #232f45;
  transform: translateX(5px);
}

.ws-icon {
  background: rgba(59, 130, 246, 0.1);
  color: #3b82f6;
  width: 50px;
  height: 50px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 0.5rem;
  font-size: 1.5rem;
}

.ws-details { flex-grow: 1; }
.ws-details h3 { margin: 0; font-size: 1.25rem; }
.ws-details p { margin: 0.25rem 0 0; color: #94a3b8; font-size: 0.9rem; }

.arrow { color: #475569; }
.ws-card:hover .arrow { color: #3b82f6; }

.state-msg { text-align: center; padding: 3rem; color: #94a3b8; }
</style>
