<!--
  Home view component for the Latent Nexus application.

  This component serves as the landing page for authenticated users. It displays
  a hero section with the application's branding, a welcome message for the
  logged-in user, and navigation buttons to enter the asset registry or sign out.
  It also highlights the key features of the platform, such as the secure vault,
  metadata indexing, and multi-tenancy support.
-->
<script setup>
import {useAuthStore} from '@/stores/authStore';
import {useRouter} from 'vue-router';

const authStore = useAuthStore();
const router = useRouter();

const navigateToWorkspaces = () => {
  router.push('/workspaces');
};
</script>

<template>
  <div class="home-container">
    <header class="hero-section">
      <div class="hero-content">
        <h1>Latent <span class="accent">Nexus</span></h1>
        <p class="subtitle">Enterprise Secure AI Asset Registry</p>

        <div v-if="authStore.authenticated" class="welcome-box">
          <p>Welcome back, <strong>{{ authStore.username }}</strong></p>
          <div class="action-buttons">
            <button @click="navigateToWorkspaces" class="btn-primary">
              Enter Registry
            </button>
            <button @click="authStore.logout()" class="btn-secondary">
              Sign Out
            </button>
          </div>
        </div>
      </div>
    </header>

    <section class="features-grid">
      <div class="feature-card">
        <i class="pi pi-shield"></i>
        <h3>Secure Vault</h3>
        <p>AES-256 encrypted storage for your proprietary AI generations.</p>
      </div>
      <div class="feature-card">
        <i class="pi pi-search"></i>
        <h3>Metadata Indexing</h3>
        <p>Instant search across ComfyUI, Auto1111, and SwarmUI workflows.</p>
      </div>
      <div class="feature-card">
        <i class="pi pi-users"></i>
        <h3>Multi-Tenancy</h3>
        <p>Isolated workspaces managed via Keycloak IAM.</p>
      </div>
    </section>
  </div>
</template>

<style scoped>
.home-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 2rem;
  color: #e2e8f0;
}

.hero-section {
  text-align: center;
  padding: 4rem 0;
  background: linear-gradient(135deg, #1e293b 0%, #0f172a 100%);
  border-radius: 1rem;
  box-shadow: 0 10px 25px -5px rgba(0, 0, 0, 0.3);
  margin-bottom: 3rem;
}

h1 {
  font-size: 4rem;
  margin-bottom: 0.5rem;
  letter-spacing: -1px;
}

.accent {
  color: #3b82f6;
}

.subtitle {
  font-size: 1.25rem;
  color: #94a3b8;
  margin-bottom: 2rem;
}

.welcome-box {
  background: rgba(255, 255, 255, 0.05);
  display: inline-block;
  padding: 1.5rem 3rem;
  border-radius: 0.5rem;
  border: 1px solid rgba(255, 255, 255, 0.1);
}

.action-buttons {
  margin-top: 1rem;
  display: flex;
  gap: 1rem;
  justify-content: center;
}

button {
  padding: 0.75rem 1.5rem;
  border-radius: 0.4rem;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.2s;
  border: none;
}

.btn-primary {
  background: #3b82f6;
  color: white;
}

.btn-primary:hover {
  background: #2563eb;
  transform: translateY(-2px);
}

.btn-secondary {
  background: transparent;
  color: #94a3b8;
  border: 1px solid #475569;
}

.btn-secondary:hover {
  background: rgba(255, 255, 255, 0.05);
  color: white;
}

.features-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 2rem;
}

.feature-card {
  background: #1e293b;
  padding: 2rem;
  border-radius: 0.75rem;
  border: 1px solid #334155;
}

.feature-card h3 {
  color: #3b82f6;
  margin-bottom: 0.5rem;
}

.feature-card p {
  color: #94a3b8;
  line-height: 1.6;
}
</style>
