<!--
  Masonry Gallery component for displaying AI-generated assets.

  This component provides a responsive, masonry-style grid for viewing assets
  within a workspace. It includes a drag-and-drop upload zone for indexing
  new images, handles the loading state during asset retrieval, and displays
  individual asset cards with basic metadata (e.g., sampler name). It
  interacts with the `AssetStore` to manage data and perform uploads.
-->
<script setup>
import {onMounted, ref} from 'vue';
import {useAssetStore} from '@/stores/assetStore';

const props = defineProps({
  workspaceId: {
    type: String,
    required: true
  }
});

const assetStore = useAssetStore();
const isDragging = ref(false);

onMounted(() => {
  assetStore.fetchAssets(props.workspaceId);
});

const getAssetUrl = (asset) => {
  return `/api/v1/assets/${props.workspaceId}/${asset.id}/image`;
};

const handleFileUpload = async (event) => {
  const files = event.target.files || event.dataTransfer.files;
  for (const file of files) {
    try {
      await assetStore.uploadAsset(props.workspaceId, file);
    } catch (err) {
      console.error("Upload failed for:", file.name);
    }
  }
};

const onDragOver = () => {
  isDragging.value = true;
};
const onDragLeave = () => {
  isDragging.value = false;
};
const onDrop = (e) => {
  isDragging.value = false;
  handleFileUpload(e);
};
</script>

<template>
  <div class="gallery-wrapper">
    <div
        class="drop-zone"
        :class="{ 'dragging': isDragging }"
        @dragover.prevent="onDragOver"
        @dragleave.prevent="onDragLeave"
        @drop.prevent="onDrop"
    >
      <i class="pi pi-cloud-upload"></i>
      <p v-if="!assetStore.uploading">Drag & Drop AI Images to Index</p>
      <p v-else>Processing Metadata...</p>
      <input type="file" multiple @change="handleFileUpload" class="file-input"/>
    </div>

    <div v-if="assetStore.loading" class="loader">
      <i class="pi pi-spin pi-spinner"></i> Scanning Vault...
    </div>

    <div v-else class="masonry-grid">
      <div v-for="asset in assetStore.assets" :key="asset.id" class="asset-card">
        <img :src="getAssetUrl(asset)" :alt="asset.filename" loading="lazy"/>
        <div class="asset-info">
          <span class="badge">{{ asset.generationMetadata?.Sampler || 'Unknown' }}</span>
          <p class="filename">{{ asset.filename }}</p>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.gallery-wrapper {
  padding: 1rem;
}

.drop-zone {
  border: 2px dashed #334155;
  background: #1e293b;
  border-radius: 1rem;
  padding: 3rem;
  text-align: center;
  margin-bottom: 2rem;
  transition: all 0.3s ease;
  position: relative;
  cursor: pointer;
}

.drop-zone.dragging {
  border-color: #3b82f6;
  background: rgba(59, 130, 246, 0.1);
}

.file-input {
  position: absolute;
  inset: 0;
  opacity: 0;
  cursor: pointer;
}

.masonry-grid {
  columns: 4 250px;
  column-gap: 1rem;
}

.asset-card {
  break-inside: avoid;
  margin-bottom: 1rem;
  background: #1e293b;
  border-radius: 0.5rem;
  overflow: hidden;
  border: 1px solid #334155;
  transition: transform 0.2s;
}

.asset-card:hover {
  transform: scale(1.02);
  border-color: #3b82f6;
}

.asset-card img {
  width: 100%;
  display: block;
}

.asset-info {
  padding: 0.75rem;
}

.badge {
  background: #3b82f6;
  font-size: 0.7rem;
  padding: 0.2rem 0.5rem;
  border-radius: 1rem;
  text-transform: uppercase;
  font-weight: bold;
}

.filename {
  font-size: 0.8rem;
  color: #94a3b8;
  margin-top: 0.5rem;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
</style>
