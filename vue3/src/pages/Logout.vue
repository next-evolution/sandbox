<template>
  <Navigator :loading="isLoading" />
  <div class="container" v-show="!isLoading">
    <h3>ログアウトしました。</h3>
    <router-link to="/">HOME</router-link>
  </div>
</template>

<script>
import Navigator from '@/components/Navigator.vue'

export default {
  name: 'Logout',
  components: { Navigator },
  data: function () {
    return {
      apiInfo: {
        apiCode: 'AA00099',
        url: this.getApiUrl('/logout'),
        messageCode: 'AA00099-I0',
      },
      isLoading: true,
    }
  },
  created: function () {
    this.$store.commit('logout')

    this.isLoading = true
    var vm = this
    this.axios
      .get(this.apiInfo.url + '?apiCode=' + this.apiInfo.apiCode)
      .then(function (response) {
        console.log(response)
      })
      .catch(function (error) {
        console.log(error)
      })
      .finally(function () {
        vm.$store.commit('logout')
        vm.isLoading = false
      })
  },
}
</script>
