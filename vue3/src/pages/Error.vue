<template>
  <Navigator />
  <h1>Error</h1>
  <router-link to="/">HOME</router-link>
</template>

<script>
import Navigator from '@/components/Navigator.vue'

export default {
  name: 'Error',
  components: { Navigator },
}
</script>
