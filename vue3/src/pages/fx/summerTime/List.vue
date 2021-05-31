<template>
  <div id="mainContent">
    <Navigator :loading="isLoading" />
    <div class="container">
      <div style="text-align: left">
        夏時間設定：<span style="padding-right: 30px">{{ apiResponse.messageText }}</span>
        <router-link :to="{ name: 'FxSummerTimeAdd' }">
          <button class="btn btn-primary" size="sm">夏時間追加</button>
        </router-link>
      </div>
      <table class="table table-hover">
        <thead>
          <tr>
            <th>targetYear</th>
            <th>applyStart</th>
            <th>applyEnd</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="summerTime in summerTimeList" v-bind:key="summerTime.targetYear" v-on:click="select(summerTime.targetYear)">
            <td>{{ summerTime.targetYear }}</td>
            <td>{{ summerTime.applyStart }}</td>
            <td>{{ summerTime.applyEnd }}</td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<style>
.table th,
.table td {
  text-align: left;
  vertical-align: middle;
}
</style>

<script>
import Navigator from '@/components/Navigator.vue'

export default {
  name: 'FxSummerTime',
  components: {
    Navigator,
  },
  data: function () {
    return {
      apiInfo: {
        apiCode: 'FX14010',
        url: this.getApiUrl('/fx/summerTime/list'),
        messageCode: 'ZZ20010-I0',
      },

      isLoading: false,

      summerTime: {
        targetYear: null,
        applyStart: null,
        applyEnd: null,
      },
      summerTimeList: [],
      apiResponse: {
        apiCode: '',
        returnCode: 0,
        recordCount: 0,
        messageCode: '',
        messageText: '',
      },
      // summerTimeFields: [
      //   {
      //     key: 'targetYear',
      //     label: 'year'
      //   },
      //   {
      //     key: 'applyStart',
      //     label: 'start',
      //     sortable: false
      //   },
      //   {
      //     key: 'applyEnd',
      //     label: 'end',
      //     sortable: false
      //   }
      // ]
    }
  },
  mounted: function () {
    var vm = this
    this.isLoading = true
    this.axios
      .get(this.apiInfo.url + '?apiCode=' + this.apiInfo.apiCode, {
        headers: { 'X-ApiAuthCode': this.$store.state.authCode },
      })
      .then(function (response) {
        console.log(response)
        vm.apiResponse = response.data
        vm.summerTimeList = response.data.body.summerTimeList
      })
      .catch(function (error) {
        console.log(error)
        vm.$router.push('/error')
      })
      .finally(function () {
        vm.isLoading = false
      })
  },
  methods: {
    select: function (e) {
      this.$router.push('/fx/summerTime/edit/' + e)
    },
  },
}
</script>
