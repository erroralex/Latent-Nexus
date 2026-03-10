# Latent Nexus

![Java](https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.2-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
![Vue.js](https://img.shields.io/badge/Vue.js-3-4FC08D?style=for-the-badge&logo=vue.js&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-336791?style=for-the-badge&logo=postgresql&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-24-2496ED?style=for-the-badge&logo=docker&logoColor=white)
![Gemini](https://img.shields.io/badge/Google%20Gemini-1.5-8E75B2?style=for-the-badge&logo=googlebard&logoColor=white)

A robust, high-performance **Enterprise AI Asset Registry** designed for the secure storage and semantic retrieval of high-fidelity AI media. It unifies metadata parsing, provides **Gemini-powered semantic enrichment**, **multi-tenant workspaces**, and **S3-backed storage** in a modern, cyber-enterprise interface.

---

## 📸 Interface

<p align="center">
  <img src="docs/screenshots/hero_view.png" width="800" alt="Main Vault and Metadata Sidebar">
  <br>
  <i>Unified masonry vault with semantic search and dynamic metadata parsing.</i>
</p>

### Intelligent Ingestion & Analysis

<p align="center">
  <img src="docs/screenshots/metadata_extraction.png" width="800" alt="Metadata Extraction">
  <br>
  <i><b>DNA Extraction:</b> Automatically parses prompts, seeds, and sampler settings from uploaded assets.</i>
</p>

<p align="center">
  <img src="docs/screenshots/semantic_search.png" width="800" alt="Semantic Search">
  <br>
  <i><b>Semantic Search:</b> Find images using natural language queries powered by Gemini embeddings.</i>
</p>

<details>
<summary><b>View More Features (Workspaces, Security & Storage)</b></summary>
<br>

<p align="center">
  <img src="docs/screenshots/workspaces.png" width="800" alt="Multi-Tenancy">
  <br>
  <i><b>Secure Workspaces:</b> Strict data isolation for multi-user environments.</i>
</p>

<p align="center">
  <img src="docs/screenshots/gemini_enrichment.png" width="800" alt="AI Enrichment">
  <br>
  <i><b>Gemini Enrichment:</b> Auto-generated descriptions and tags for every asset.</i>
</p>

</details>

---

## 🔐 Enterprise-Grade Security & Scalability

Designed for scalability and strict data isolation.

*   **Multi-Tenancy:** All data is strictly isolated by `WorkspaceID`.
*   **RBAC Security:** Integrated with **Keycloak** (OAuth2/OIDC) for robust `USER` and `ADMIN` role management.
*   **S3-Compatible Storage:** Built to scale with **MinIO** or AWS S3, keeping binary data separate from metadata.
*   **Stateless Architecture:** Fully containerized and ready for serverless deployment (Railway/Render).
*   **Secrets Management:** Zero hardcoded credentials; fully environment-variable driven.

---

## ✨ Key Features

*   **Universal Metadata Engine:** Advanced parsing strategies for the AI ecosystem.
    *   **Extraction:** Parses binary chunks from PNG/JPEG to identify prompts, seeds, samplers, CFG scales, and steps.
    *   **Support:** Compatible with Stable Diffusion (A1111, ComfyUI) and Midjourney assets.
*   **Gemini Intelligence:** Integrated **Google Gemini 1.5 Flash** orchestration.
    *   **Auto-Captioning:** Generates natural language descriptions for visual assets.
    *   **Semantic Embeddings:** Vectorizes content for "vibe-based" similarity search.
*   **Vault Management:**
    *   **Workspaces:** Create distinct environments for different projects or teams.
    *   **Cursor Pagination:** Optimized for browsing massive libraries without performance degradation.
    *   **Virtual Scrolling:** Frontend optimized for rendering thousands of assets.
*   **Performance:**
    *   **JSONB Indexing:** PostgreSQL GIN indexing for high-speed metadata queries.
    *   **Virtual Threads:** Leverages Java 21 Project Loom for high-concurrency I/O (S3 uploads, Gemini API calls).
    *   **Reactive-Ready:** Built with modern Spring Boot 3.2 patterns.

---

## 💻 System Requirements

*   **Runtime:** Java 21 (LTS), Docker (for containerized deployment).
*   **Database:** PostgreSQL 16+ (with `pgvector` extension recommended).
*   **Storage:** MinIO or S3 bucket.
*   **Auth:** Keycloak instance.
*   **AI API:** Google Gemini API Key.

---

## 🛠️ Technical Architecture

The application is built as a cloud-native microservice, combining a resilient Spring Boot backend with a modern Vue.js frontend.

*   **Backend (Java 21 + Spring Boot 3.2):**
    *   **PostgreSQL + JSONB:** Flexible metadata storage with GIN indexing.
    *   **Virtual Threads:** Optimized for heavy I/O tasks.
    *   **Spring Security:** OAuth2 resource server configuration.
    *   **Flyway:** Automated database schema migrations.

*   **Frontend (Vue 3 + PrimeVue):**
    *   **Composition API:** Modern, reactive UI logic.
    *   **PrimeVue:** Enterprise-grade UI component library.
    *   **Vite:** Blazing fast build pipeline.

*   **Infrastructure:**
    *   **Docker:** Full stack containerization.
    *   **CI/CD:** Ready for GitHub Actions pipelines.

---

## 🚀 Getting Started

1.  **Clone the Repository**
    ```bash
    git clone https://github.com/yourusername/latent-nexus.git
    cd latent-nexus
    ```

2.  **Configure Environment**
    Create a `.env` file with your credentials:
    ```properties
    GEMINI_API_KEY=your_key_here
    DB_PASSWORD=your_db_password
    MINIO_ACCESS_KEY=your_minio_key
    MINIO_SECRET_KEY=your_minio_secret
    ```

3.  **Start Services (Docker)**
    ```bash
    docker-compose up -d
    ```

4.  **Access the Application**
    *   Frontend: `http://localhost:5173`
    *   Backend API: `http://localhost:8080`
    *   API Docs: `http://localhost:8080/swagger-ui.html`

---

## 📜 License

Distributed under the **MIT License**.

---

<p align="center">
  <b>Architected by</b><br>
  <img src="frontend/src/assets/logo.png" width="120" alt="Logo"><br>
  Copyright (c) 2026 Latent Nexus Team
</p>
