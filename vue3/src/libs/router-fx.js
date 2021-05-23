export default [
  // currencyPair
  {
    path: '/fx/currencyPair',
    name: 'FxCurrencyPair',
    component: () => import('../pages/fx/currencyPair/List.vue')
  },
  {
    path: '/fx/currencyPair/add',
    name: 'FxCurrencyPairAdd',
    component: () => import('../pages/fx/currencyPair/Add.vue')
  },
  {
    path: '/fx/currencyPair/edit/:currencyPairCode',
    name: 'FxCurrencyPairEdit',
    component: () => import('../pages/fx/currencyPair/Edit.vue')
  },

  // country
  {
    path: '/fx/country',
    name: 'FxCountry',
    component: () => import('../pages/fx/country/List.vue')
  },
  {
    path: '/fx/country/add',
    name: 'FxCountryAdd',
    component: () => import('../pages/fx/country/Add.vue')
  },
  {
    path: '/fx/country/edit/:countryCode',
    name: 'FxCountryEdit',
    component: () => import('../pages/fx/country/Edit.vue')
  },

  // summerTime
  {
    path: '/fx/summerTime',
    name: 'FxSummerTime',
    component: () => import('../pages/fx/summerTime/List.vue')
  },
  {
    path: '/fx/summerTime/add',
    name: 'FxSummerTimeAdd',
    component: () => import('../pages/fx/summerTime/Add.vue')
  },
  {
    path: '/fx/summerTime/edit/:targetYear',
    name: 'FxSummerTimeEdit',
    component: () => import('../pages/fx/summerTime/Edit.vue')
  },

  // economicIndicator
  {
    path: '/fx/economicIndicator',
    name: 'FxEconomicIndicator',
    component: () => import('../pages/fx/economicIndicator/Search.vue')
  },
  {
    path: '/fx/economicIndicator/add',
    name: 'FxEconomicIndicatorAdd',
    component: () => import('../pages/fx/economicIndicator/Add.vue')
  },
  {
    path: '/fx/economicIndicator/edit/:economicIndicatorId/:countryCode',
    name: 'FxEconomicIndicatorEdit',
    component: () => import('../pages/fx/economicIndicator/Edit.vue')
  },

  // economicIndicatorData
  {
    path: '/fx/economicIndicatorData',
    name: 'FxEconomicIndicatorData',
    component: () => import('../pages/fx/economicIndicatorData/Search.vue')
  },
  {
    path: '/fx/economicIndicatorData/add',
    name: 'FxEconomicIndicatorDataAdd',
    component: () => import('../pages/fx/economicIndicatorData/Add.vue')
  },
  {
    path: '/fx/economicIndicatorData/edit/:economicIndicatorId/:publicationDate/:publicationTime',
    name: 'FxEconomicIndicatorDataEdit',
    component: () => import('../pages/fx/economicIndicatorData/Edit.vue')
  },

  // barData
  {
    path: '/fx/bardata/upload',
    name: 'FxBarDataUpload',
    component: () => import('../pages/fx/bardata/Upload.vue')
  },
  {
    path: '/fx/bardata/search/:barType',
    name: 'FxBarDataSearch',
    component: () => import('../pages/fx/bardata/Search.vue')
  },
  {
    path: '/fx/bardata/generate',
    name: 'FxBarDataGenerate',
    component: () => import('../pages/fx/bardata/Generate.vue')
  }
]
