<template>
  <div id="mainContent">
    <Navigator :loading="isLoading" />
    <div class="container">
      <div style="text-align: left;">
        通貨ペアマスタ：
        <span style="padding-right: 30px;">{{apiResponse.messageText}}</span>
        <router-link :to="{name: 'FxCurrencyPairAdd'}">
        <button class="btn btn-primary" size="sm">通貨ペア追加</button>
        </router-link>
      </div>
      <table class="table table-hover">
      <thead>
        <tr>
          <th>Code</th>
          <th>Name</th>
          <th>Volatility</th>
          <th>Order</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="currencyPair in currencyPairList" v-bind:key="currencyPair.currencyPairCode" v-on:click="select(currencyPair.currencyPairCode)">
          <!-- <td><router-link :to="`/fx/country/edit/${country.countryCode}`">{{country.countryCode}}</router-link></td> -->
          <td>{{currencyPair.currencyPairCode}}</td>
          <td>{{currencyPair.currencyPairName}}</td>
          <td>{{currencyPair.profitVolatility}}</td>
          <td>{{currencyPair.sortOrder}}</td>
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
  name: 'FxCurrencyPair',
  components: {
    Navigator
  },
  data: function () {
    return {
      apiInfo: {
        apiCode: 'FX10010',
        url: this.getApiUrl('/fx/currencyPair/list'),
        messageCode: 'ZZ20010-I0'
      },

      isLoading: false,

      currencyPairList: [
        {
          currencyPairCode: '',
          currencyPairName: '',
          profitVolatility: 0,
          sortOrder: 0
        }
      ],

      apiResponse: {
        csrfToken: '',
        apiCode: '',
        returnCode: 0,
        recordCount: 0,
        messageCode: '',
        messageText: ''
      },
      // currencyPairListFields: [
      //   {
      //     key: 'currencyPairCode',
      //     label: 'Code',
      //     sortable: true
      //   },
      //   {
      //     key: 'currencyPairName',
      //     label: 'Name',
      //     sortable: false
      //   },
      //   {
      //     key: 'profitVolatility',
      //     label: 'Volatility',
      //     sortable: false
      //   },
      //   {
      //     key: 'sortOrder',
      //     label: 'Sort',
      //     sortable: true
      //   }
      // ]
    }
  },
  mounted: function () {
    var vm = this
    this.isLoading = true
    this.axios
      .get(this.apiInfo.url + '?apiCode=' + this.apiInfo.apiCode, {
        headers: { 'X-ApiAuthCode': this.$store.state.authCode }
      })
      .then(function (response) {
        console.log(response)
        vm.apiResponse = response.data
        vm.currencyPairList = response.data.body.currencyPairList
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
    select : function(e){
      this.$router.push('/fx/currencyPair/edit/' + e)
    }
  }
}
</script>
