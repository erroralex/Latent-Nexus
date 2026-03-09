import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import WorkspaceSelectionView from '../views/WorkspaceSelectionView.vue'
import AssetRegistryView from '../views/AssetRegistryView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView
    },
    {
      path: '/workspaces',
      name: 'workspace-selection',
      component: WorkspaceSelectionView
    },
    {
      path: '/workspaces/:workspaceId',
      name: 'asset-registry',
      component: AssetRegistryView
    }
  ]
})

export default router