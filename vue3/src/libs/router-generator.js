export default [
  {
    path: '/generator',
    name: 'GeneratorTop',
    component: () => import('../pages/generator/Top.vue')
  },
  {
    path: '/generator/import/db',
    name: 'GeneratorImportDb',
    component: () => import('../pages/generator/ImportDb.vue')
  },
  {
    path: '/generator/import/api',
    name: 'GeneratorImportApi',
    component: () => import('../pages/generator/ImportApi.vue')
  },
  {
    path: '/generator/import/interface',
    name: 'GeneratorImportInterface',
    component: () => import('../pages/generator/ImportInterface.vue')
  }
]
