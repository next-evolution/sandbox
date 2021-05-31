<template>
  <div id="mainContent">
    <Navigator :loading="isLoading" />
    <div class="container">
      <hr />
      <div class="row">
        <div class="col-auto align-self-center">足データ：{{ resultMessage }}</div>
        <div class="col-auto">
          <select v-model="currencyPairCode" ref="currencyPairCode" v-on:change="currencyPairChange" class="form-select" style="width: auto">
            <option v-for="currencyPair in currencyPairList" v-bind:key="currencyPair.currencyPairCode" v-bind:value="currencyPair.currencyPairCode">
              {{ currencyPair.currencyPairCode }}
            </option>
          </select>
        </div>
        <div class="col-auto">
          <select v-model="pageSize" ref="pageSize" v-on:change="pageSizeChange" class="form-select" style="width: auto">
            <option v-for="pageSize in pageSizeList" v-bind:key="pageSize" v-bind:value="pageSize">{{ pageSize }}</option>
          </select>
        </div>
        <div class="col-md-auto align-self-center">
          <button v-if="pageNo === 1" class="btn btn-secondary" style="margin-left: 10px" disabled>&lt;</button>
          <button v-if="pageNo > 1" v-on:click="pageNoChange(-1)" class="btn btn-primary" style="margin-left: 10px">&lt;</button>
          <span style="margin-left: 5px; margin-right: 5px">[ {{ pageNo }} / {{ totalPageCount }} ]</span>
          <button v-if="totalPageCount <= pageNo" class="btn btn-secondary" style="margin-left: 1px" disabled>&gt;</button>
          <button v-if="totalPageCount > pageNo" v-on:click="pageNoChange(1)" class="btn btn-primary" style="margin-left: 1px">&gt;</button>
        </div>
        <div class="col-auto">
          <input v-model="frontDateBegin" placeholder="begin" size="10" maxlength="8" class="form-control" />
        </div>
        <div class="col-auto">
          <input v-model="frontDateEnd" placeholder="end" size="10" maxlength="8" class="form-control" />
        </div>
        <div class="col-auto">
          <button class="btn btn-primary" v-on:click="frontDateChange">Search</button>
        </div>
      </div>
      <hr />
    </div>
    <table class="table table-striped container">
      <thead>
        <tr>
          <th>FrontDate</th>
          <th>BarTime</th>
          <th>Range Profit</th>
          <th>Close Profit</th>
          <th>High Profit</th>
          <th>Low Profit</th>
          <th>Open Price</th>
          <th>High Price</th>
          <th>Low Price</th>
          <th>Close Price</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="barData in barDataList" v-bind:key="barData.barDateTime">
          <td>{{ barData.frontDate }}({{ barData.dayOfWeek }})</td>
          <td>{{ barData.barDateTime.replace(/-/g, '/').replace(/T/g, ' ') }}</td>
          <td class="price">{{ barData.rangeProfit.toFixed(5) }}</td>
          <td class="price">{{ barData.closeProfit.toFixed(5) }}</td>
          <td class="price">{{ barData.highProfit.toFixed(5) }}</td>
          <td class="price">{{ barData.lowProfit.toFixed(5) }}</td>
          <td class="price">{{ barData.openPrice.toFixed(5) }}</td>
          <td class="price">{{ barData.highPrice.toFixed(5) }}</td>
          <td class="price">{{ barData.lowPrice.toFixed(5) }}</td>
          <td class="price">{{ barData.closePrice.toFixed(5) }}</td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<style>
.table th,
.table td {
  text-align: left;
  vertical-align: middle;
}

td.price {
  text-align: right;
  vertical-align: middle;
}
</style>

<script>
import Navigator from '@/components/Navigator.vue'

export default {
  name: 'FxBarDataSearch',
  components: {
    Navigator,
  },
  data: function () {
    return {
      apiInfo: {
        apiCode: '',
        url: this.getApiUrl('/fx/bardata/search'),
        messageCode: 'ZZ20010-I0',
      },

      barType: '',
      barTypeName: '',

      isLoading: false,
      resultMessage: '',

      barDataList: [],
      currencyPairCode: '',
      currencyPairList: [],
      frontDateBegin: '',
      frontDateEnd: '',

      searchCount: 0,
      pageNo: 1,
      pageSize: 15,
      totalPageCount: 0,

      pageSizeList: [5, 10, 15, 20, 25, 50],

      //   apiResponse: {
      //     apiCode: '',
      //     returnCode: 0,
      //     recordCount: 0,
      //     messageCode: '',
      //     messageText: ''
      //   },
      barDataListFields: [
        {
          key: 'frontDate',
          label: 'FrontDate',
          sortable: false,
        },
        {
          key: 'barDateTime',
          label: 'BarTime',
          sortable: false,
        },
        {
          key: 'rangeProfit',
          sortable: false,
        },
        {
          key: 'closeProfit',
          sortable: false,
        },
        {
          key: 'highProfit',
          sortable: false,
        },
        {
          key: 'lowProfit',
          sortable: false,
        },
        {
          key: 'openPrice',
          sortable: false,
        },
        {
          key: 'highPrice',
          sortable: false,
        },
        {
          key: 'lowPrice',
          sortable: false,
        },
        {
          key: 'closePrice',
          sortable: false,
        },
      ],
    }
  },
  mounted: function () {
    this.currencyPairCode = 'USDJPY'
    this.setBarType()
    this.searchBarData()
  },
  updated: function () {
    if (this.$route.params.barType != this.barType) {
      if (this.isEmpty(this.currencyPairCode)) {
        this.currencyPairCode = 'USDJPY'
      }
      this.setBarType()
      this.searchBarData()
    }
  },
  methods: {
    pageNoChange: function (value) {
      this.pageNo = this.pageNo + value
      this.searchBarData()
    },
    currencyPairChange: function () {
      this.pageNo = 1
      this.frontDateBegin = ''
      this.frontDateEnd = ''
      this.searchBarData()
    },
    pageSizeChange: function () {
      this.pageNo = 1
      this.searchBarData()
    },
    frontDateChange: function () {
      this.pageNo = 1
      this.searchBarData()
    },
    searchBarData: function () {
      this.isLoading = true
      var vm = this
      this.axios
        .get(
          this.apiInfo.url +
            this.barType +
            '?apiCode=' +
            this.apiInfo.apiCode +
            '&currencyPairCode=' +
            this.currencyPairCode +
            '&frontDateBegin=' +
            this.frontDateBegin +
            '&frontDateEnd=' +
            this.frontDateEnd +
            '&pageNo=' +
            this.pageNo +
            '&pageSize=' +
            this.pageSize,
          { headers: { 'X-ApiAuthCode': this.$store.state.authCode } }
        )
        .then(function (response) {
          console.log(response)
          if (vm.apiInfo.messageCode === response.data.messageCode) {
            vm.searchCount = response.data.body.searchCount
            vm.pageNo = response.data.body.pageNo
            vm.pageSize = response.data.body.pageSize
            vm.totalPageCount = response.data.body.totalPageCount

            vm.csrfToken = response.data.csrfToken
            vm.currencyPairCode = response.data.body.currencyPairCode
            vm.currencyPairList = response.data.body.currencyPairList

            vm.frontDateBegin = response.data.body.frontDateBegin
            vm.frontDateEnd = response.data.body.frontDateEnd

            vm.barDataList = response.data.body.barDataList

            vm.resultMessage = response.data.messageText
          } else {
            alert(response.data.messageText)
          }
        })
        .catch(function (error) {
          console.log(error)
          vm.$router.push('/error')
        })
        .finally(function () {
          vm.isLoading = false
        })
    },
    setBarType: function () {
      var param = this.$route.params.barType
      if (this.isEmpty(param)) {
        this.barType = '30m'
        this.barTypeName = '30分足'
        this.apiInfo.apiCode = 'FX26010'
        return
      }
      if (param === '30m') {
        this.barType = '30m'
        this.barTypeName = '30分足'
        this.apiInfo.apiCode = 'FX26010'
        return
      }
      if (param === '1h') {
        this.barType = '1h'
        this.barTypeName = '時間足'
        this.apiInfo.apiCode = 'FX24010'
        return
      }
      this.barType = '1d'
      this.barTypeName = '日足'
      this.apiInfo.apiCode = 'FX20010'
    },
  },
}
</script>
