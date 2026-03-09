<!--
  Root component of the Latent Nexus frontend application.

  This component serves as the main entry point for the Vue.js application's UI.
  It defines the top-level layout structure, including the global navigation area
  and the primary router view where different pages (views) are rendered based
  on the current URL.
-->
<script setup>
import { RouterView } from 'vue-router'
</script>

<template>
  <header>
    <div class="wrapper">
      <nav>
      </nav>
    </div>
  </header>

  <RouterView />
</template>

<style scoped>
</style>
