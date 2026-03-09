<!--
  Asset Registry view component for a specific workspace.

  This component displays the main asset gallery for a selected workspace. It
  retrieves the `workspaceId` from the route parameters and passes it to the
  `MasonryGallery` component. It provides a header with navigation back to the
  workspace selection and placeholder actions for filtering assets.
-->
<script setup>
import { useRoute, useRouter } from 'vue-router';
import MasonryGallery from '@/components/media/MasonryGallery.vue';

const route = useRoute();
const router = useRouter();
const workspaceId = route.params.workspaceId;

const goBack = () => {
  router.push('/');
};
</script>

<template>
  <div class="registry-container">
    <header class="registry-header">
      <div class="header-content">
        <button @click="goBack" class="btn-back" aria-label="Go back to workspaces">
          <i class="pi pi-arrow-left"></i>
        </button>
        <div class="title-group">
          <h1>Workspace <span class="accent">Registry</span></h1>
          <p class="workspace-id">ID: {{ workspaceId }}</p>
        </div>
      </div>
      <div class="header-actions">
        <button class="btn-outline">
          <i class="pi pi-filter"></i> Filters
        </button>
      </div>
    </header>

    <main class="registry-main">
      <MasonryGallery :workspace-id="workspaceId" />
    </main>
  </div>
</template>

<style scoped>
.registry-container {
  padding: 2rem;
  color: #e2e8f0;
}

.registry-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-end;
  margin-bottom: 2rem;
  border-bottom: 1px solid #334155;
  padding-bottom: 1.5rem;
}

.header-content {
  display: flex;
  align-items: center;
  gap: 1.5rem;
}

.btn-back {
  background: #1e293b;
  border: 1px solid #475569;
  color: #94a3b8;
  width: 40px;
  height: 40px;
  border-radius: 50%;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.2s;
}

.btn-back:hover {
  background: #334155;
  color: #e2e8f0;
  border-color: #3b82f6;
}

h1 {
  font-size: 2rem;
  margin: 0;
}

.accent {
  color: #3b82f6;
}

.workspace-id {
  font-family: monospace;
  color: #94a3b8;
  font-size: 0.9rem;
  margin-top: 0.25rem;
}

.btn-outline {
  background: transparent;
  border: 1px solid #475569;
  color: #e2e8f0;
  padding: 0.5rem 1rem;
  border-radius: 0.4rem;
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.btn-outline:hover {
  background: rgba(255, 255, 255, 0.05);
  border-color: #3b82f6;
}

.registry-main {
  min-height: 400px;
}
</style>
