<template>
  <Navigator :back-page="backPage" :loading="isLoading" />
  <div id="resultMessage">{{ resultMessage }}</div>
  <div v-show="!isInput">
    <router-link :to="backPage">戻る</router-link>
    <br />
    <button class="btn-primary" v-on:click="initialize">続けて登録</button>
  </div>
  <div class="container" v-show="isInput">
    <div class="row" align-v="center" align-h="start">
      <div class="col">通貨ペア登録フォーム</div>
    </div>
    <hr />
    <div class="row" align-v="center" align-h="start">
      <div class="col-sm-2">
        <label for="currencyPairCode" class="control-label text-nowrap">通貨ペア</label>
      </div>
      <div class="col-sm-2">
        <input type="tel" id="currencyPairCode" ref="currencyPairCode" v-model="currencyPair.currencyPairCode" class="form-control" maxlength="8" />
      </div>
    </div>
    <hr />
    <div class="row" align-v="center" align-h="start">
      <div class="col-sm-2">
        <label for="currencyPairName" class="control-label text-nowrap">通貨ペア名称</label>
      </div>
      <div class="col-sm-3">
        <input type="text" id="currencyPairName" ref="currencyPairName" v-model="currencyPair.currencyPairName" class="form-control" />
      </div>
    </div>
    <hr />
    <div class="row" align-v="center" align-h="start">
      <div class="col-sm-2">
        <label for="profitVolatility" class="control-label text-nowrap">警告値幅</label>
      </div>
      <div class="col-sm-2">
        <input type="tel" id="profitVolatility" ref="profitVolatility" v-model="currencyPair.profitVolatility" class="form-control" />
      </div>
    </div>
    <hr />
    <div class="row" align-v="center" align-h="start">
      <div class="col-sm-2">
        <label for="sortOrder" class="control-label text-nowrap">SortOrder</label>
      </div>
      <div class="col-sm-2">
        <input type="tel" id="sortOrder" ref="sortOrder" v-model="currencyPair.sortOrder" class="form-control" maxlength="5" />
      </div>
    </div>
    <hr />
    <div class="row" align-v="center" align-h="start">
      <div class="col-sm-2"></div>
      <div class="col-auto">
        <ExecuteButton type="insert" @buttonClick="insertApi" />
      </div>
    </div>
  </div>
</template>

<script>
import Navigator from '@/components/Navigator.vue'
import ExecuteButton from '@/components/ExecuteButton.vue'

export default {
  name: 'FxCurrencyPairAdd',
  components: {
    Navigator,
    ExecuteButton,
  },
  data: function () {
    return {
      backPage: '/fx/currencyPair',
      apiInfo: {
        apiCode: 'FX10030',
        url: this.getApiUrl('/fx/currencyPair/insert'),
        messageCode: 'ZZ20040-I0',
      },

      isLoading: false,
      isInput: true,
      resultMessage: '',
      csrfToken: '',

      currencyPair: {
        currencyPairCode: '',
        currencyPairName: '',
        profitVolatility: '',
        sortOrder: '',
      },

      apiResponse: {
        csrfToken: '',
        apiCode: '',
        returnCode: 0,
        recordCount: 0,
        messageCode: '',
        messageText: '',
      },
    }
  },
  mounted: async function () {
    this.csrfToken = await this.getCsrfToken(this)
    this.$refs.currencyPairCode.focus()
  },
  methods: {
    initialize: async function () {
      this.isLoading = true

      this.isInput = true
      this.currencyPair.currencyPairCode = ''
      this.currencyPair.currencyPairName = ''
      this.currencyPair.profitVolatility = ''
      this.currencyPair.sortOrder = ''

      this.resultMessage = ''

      this.csrfToken = await this.getCsrfToken(this)
    },
    insertApi: function () {
      if (this.isEmpty(this.currencyPair.currencyPairCode)) {
        alert('input currencyPair.')
        this.$refs.currencyPairCode.focus()
        return
      }
      if (!this.isCurrencyPair(this.currencyPair.currencyPairCode)) {
        alert('not currencyPair.')
        this.$refs.currencyPairCode.focus()
        return
      }
      if (this.isEmpty(this.currencyPair.currencyPairName)) {
        alert('input currencyPairName.')
        this.$refs.currencyPairName.focus()
        return
      }
      if (!this.isPrice(this.currencyPair.profitVolatility)) {
        alert('volatility is not price.')
        this.$refs.profitVolatility.focus()
        return
      }
      if (!this.isNumber(this.currencyPair.sortOrder)) {
        alert('sortOrder is Number.')
        this.$refs.sortOrder.focus()
        return
      }
      this.isLoading = true
      var vm = this
      this.axios
        .post(
          this.apiInfo.url,
          {
            apiCode: this.apiInfo.apiCode,
            authCode: this.$store.state.authCode,
            currencyPair: this.currencyPair,
          },
          {
            headers: {
              'X-CSRF-TOKEN': this.csrfToken,
              'X-ApiAuthCode': this.$store.state.authCode,
            },
          }
        )
        .then(function (response) {
          console.log(response)
          vm.apiResponse = response.data
          if (vm.apiInfo.messageCode === response.data.messageCode) {
            vm.resultMessage = vm.apiResponse.messageText
            vm.isInput = false
          }
          vm.$nextTick(alert(response.data.messageText))
        })
        .catch(function (error) {
          console.log(error)
          vm.$router.push('/error')
        })
        .finally(function () {
          vm.isLoading = false
        })
    },
  },
}
</script>
