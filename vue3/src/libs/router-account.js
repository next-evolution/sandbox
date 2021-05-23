export default [
  {
    path: '/account/create',
    name: 'AccountCreate',
    component: () => import('../pages/account/Create.vue') 
  },
  {
    path: '/account/activation',
    name: 'AccountActivation',
    component: () => import('../pages/account/Activation.vue') 
  },
  {
    path: '/account/passwd',
    name: 'AccountPassword',
    component: () => import('../pages/account/Passwd.vue') 
  }
]