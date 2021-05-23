<template>
  <div id="mainContainer">
    <Navigator />
    <Loading
      :active="isLoading"
      :can-cancel="true"
      :is-full-page="true"></Loading>
    <div v-show="!isLoading">
      <h3>ログアウトしました。</h3>
      <router-link to="/">HOME</router-link>
    </div>
  </div>
</template>

<script>
import Navigator from '@/components/Navigator.vue'
import Loading from 'vue-loading-overlay'

export default {
  name: 'Logout',
  components: { Navigator, Loading },
  data: function () {
    return {
      apiInfo: {
        apiCode: 'AA00099',
        url: this.getApiUrl('/logout'),
        messageCode: 'AA00099-I0'
      },
      isLoading: true
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
  }
}
</script>
