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
      <div class="col-auto">経済指標マスタ登録フォーム</div>
    </div>
    <hr />
    <div class="row" align-v="center" align-h="start">
      <div class="col-sm-2">
        <label for="countryCode" class="control-label text-nowrap">国コード</label>
      </div>
      <div class="col-auto">
        <select v-model="economicIndicator.countryCode" ref="countryCode" class="form-select">
          <option v-for="country in countryList" v-bind:key="country.countryCode" v-bind:value="country.countryCode">
            {{ country.countryName }}
          </option>
        </select>
      </div>
    </div>
    <hr />
    <div class="row" align-v="center" align-h="start">
      <div class="col-sm-2">
        <label for="economicIndicatorName" class="control-label text-nowrap">経済指標名称</label>
      </div>
      <div class="col-sm-6">
        <input
          type="text"
          id="economicIndicatorName"
          ref="economicIndicatorName"
          v-model="economicIndicator.economicIndicatorName"
          class="form-control"
          maxlength="64"
        />
      </div>
    </div>
    <hr />
    <div class="row" align-v="center" align-h="start">
      <div class="col-sm-2">
        <label for="economicIndicatorDescription" class="control-label text-nowrap">詳細</label>
      </div>
      <div class="col-sm-6">
        <input
          type="text"
          id="economicIndicatorDescription"
          ref="economicIndicatorDescription"
          v-model="economicIndicator.economicIndicatorDescription"
          class="form-control"
          maxlength="64"
        />
      </div>
    </div>
    <hr />
    <div class="row" align-v="center" align-h="start">
      <div class="col-sm-2">
        <label for="unitOfValue" class="control-label text-nowrap">単位</label>
      </div>
      <div class="col-sm-2">
        <input type="text" id="unitOfValue" ref="unitOfValue" v-model="economicIndicator.unitOfValue" class="form-control" maxlength="16" />
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
  name: 'FxEconomicIndicatorAdd',
  components: {
    Navigator,
    ExecuteButton,
  },
  data: function () {
    return {
      backPage: '/fx/economicIndicator',
      apiInfo: {
        apiCode: 'FX18030',
        url: this.getApiUrl('/fx/economicIndicator/insert'),
        messageCode: 'ZZ20040-I0',
      },
      apiInfoSelect: {
        apiCode: 'FX18030',
        url: this.getApiUrl('/fx/economicIndicator/master'),
        messageCode: 'ZZ30010-I0',
      },

      isLoading: false,
      isInput: true,
      resultMessage: '',

      csrfToken: '',

      countryList: [],

      economicIndicator: {
        economicIndicatorId: '',
        countryCode: '',
        economicIndicatorName: '',
        economicIndicatorDescription: '',
        unitOfValue: '',
      },

      appResponse: {
        csrfToken: '',
        apiCode: '',
        returnCode: 0,
        recordCount: 0,
        messageCode: '',
        messageText: '',
      },
    }
  },
  mounted: function () {
    this.isLoading = true
    this.$refs.countryCode.focus()
    var vm = this
    this.axios
      .get(this.apiInfoSelect.url + '?apiCode=' + this.apiInfoSelect.apiCode, {
        headers: { 'X-ApiAuthCode': this.$store.state.authCode },
      })
      .then(function (response) {
        console.log(response)
        if (vm.apiInfoSelect.messageCode === response.data.messageCode) {
          vm.csrfToken = response.data.csrfToken
          vm.countryList = response.data.body.countryList
          vm.economicIndicator.countryCode = response.data.body.countryList[0].countryCode
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
    initialize: async function () {
      this.isLoading = true

      this.isInput = true
      this.economicIndicator.economicIndicatorName = ''
      this.economicIndicator.economicIndicatorDescription = ''
      this.economicIndicator.unitOfValue = ''
      this.economicIndicator.resultMessage = ''

      this.csrfToken = await this.getCsrfToken(this)
    },
    insertApi: function () {
      if (this.isEmpty(this.economicIndicator.countryCode)) {
        alert('select countryCode.')
        this.$refs.countryCode.focus()
        return
      }
      if (this.isEmpty(this.economicIndicator.economicIndicatorName)) {
        alert('input economicIndicatorName.')
        this.$refs.economicIndicatorName.focus()
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
            economicIndicator: this.economicIndicator,
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
            vm.resultMessage = vm.appResponse.messageText
            vm.isInput = false
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
