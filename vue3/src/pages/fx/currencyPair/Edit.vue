<template>
  <Navigator :back-page="backPage" :loading="isLoading" />
  <div id="resultMessage">{{ resultMessage }}</div>
  <div v-show="!isInput">
    <router-link :to="backPage">戻る</router-link>
  </div>
  <div class="container" v-show="isInput">
    <div class="row" align-v="center" align-h="start">
      <div class="col">通貨ペア編集フォーム</div>
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
        <input type="tel" id="profitVolatility" ref="profitVolatility" v-model="currencyPair.profitVolatility" class="form-control" maxlength="12" />
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
        <ExecuteButton type="update" @buttonClick="updateApi" />
      </div>
    </div>
  </div>
</template>

<script>
import Navigator from '@/components/Navigator.vue'
import ExecuteButton from '@/components/ExecuteButton.vue'

export default {
  name: 'FxCurrencyPairEdit',
  components: {
    Navigator,
    ExecuteButton,
  },
  data: function () {
    return {
      backPage: '/fx/currencyPair',
      apiInfo: {
        apiCode: 'FX10040',
        url: this.getApiUrl('/fx/currencyPair/update'),
        messageCode: 'ZZ20050-I0',
      },

      apiInfoSelect: {
        apiCode: 'FX10020',
        url: this.getApiUrl('/fx/currencyPair/select'),
        messageCode: 'ZZ20020-I0',
      },

      isLoading: false,
      isInput: true,
      resultMessage: '',

      csrfToken: '',
      currencyPairCode: '',

      currencyPair: {
        currencyPairCode: '',
        currencyPairName: '',
        profitVolatility: '',
        sortOrder: '',
      },

      //   apiResponse: {
      //     csrfToken: '',
      //     apiCode: '',
      //     returnCode: 0,
      //     recordCount: 0,
      //     messageCode: '',
      //     messageText: ''
      //   }
    }
  },
  mounted: function () {
    this.isLoading = true
    this.$refs.currencyPairCode.focus()
    var vm = this
    this.axios
      .get(this.apiInfoSelect.url + '?apiCode=' + this.apiInfoSelect.apiCode + '&currencyPairCode=' + this.$route.params.currencyPairCode, {
        headers: { 'X-ApiAuthCode': this.$store.state.authCode },
      })
      .then(function (response) {
        console.log(response)
        if (vm.apiInfoSelect.messageCode === response.data.messageCode) {
          vm.apiResponse = response.data
          vm.csrfToken = response.data.csrfToken
          vm.currencyPairCode = response.data.body.currencyPair.currencyPairCode
          vm.currencyPair = response.data.body.currencyPair
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
  methods: {
    updateApi: function () {
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
            currencyPairCode: this.currencyPairCode,
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
          if (vm.apiInfo.messageCode === response.data.messageCode) {
            vm.resultMessage = response.data.messageText
            vm.isInput = false
            vm.$router.push(vm.backPage)
          }
          alert(response.data.messageText)
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
