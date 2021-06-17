<template>
  <Navigator :back-page="backPage" :loading="isLoading" />
  <div id="resultMessage">{{ resultMessage }}</div>
  <div v-show="!isInput">
    <router-link :to="backPage">戻る</router-link>
  </div>
  <div class="container" v-show="isInput">
    <div class="row" align-v="center" align-h="start">
      <div class="col">経済指標マスタ編集フォーム</div>
    </div>
    <hr />
    <div class="row" align-v="center" align-h="start">
      <div class="col-sm-2">
        <label for="countryCode" class="control-label text-nowrap">国コード</label>
      </div>
      <div class="col-auto">
        <select v-model="countryCode" id="countryCode" ref="countryCode" v-on:change="countryCodeChange" class="form-select" style="width: auto">
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
          size="50"
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
        <input type="text" id="unitOfValue" ref="unitOfValue" v-model="economicIndicator.unitOfValue" class="form-control" size="16" maxlength="16" />
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
  name: 'FxEconomicIndicatorEdit',
  components: {
    Navigator,
    ExecuteButton,
  },
  data: function () {
    return {
      backPage: '/fx/economicIndicator',
      apiInfo: {
        apiCode: 'FX18040',
        url: this.getApiUrl('/fx/economicIndicator/update'),
        messageCode: 'ZZ20050-I0',
      },

      apiInfoSelect: {
        apiCode: 'FX18020',
        url: this.getApiUrl('/fx/economicIndicator/select'),
        messageCode: 'ZZ20020-I0',
      },
      isLoading: true,
      isInput: true,
      resultMessage: '',

      countryList: [],

      countryCode: '',
      economicIndicatorId: 0,

      csrfToken: '',

      economicIndicator: {
        economicIndicatorId: 0,
        countryCode: '',
        economicIndicatorName: '',
        economicIndicatorDescription: '',
        unitOfValue: '',
      },

      apiResponse: {
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
      .get(
        this.apiInfoSelect.url +
          '?apiCode=' +
          this.apiInfoSelect.apiCode +
          '&economicIndicatorId=' +
          this.$route.params.economicIndicatorId +
          '&countryCode=' +
          this.$route.params.countryCode,
        { headers: { 'X-ApiAuthCode': this.$store.state.authCode } }
      )
      .then(function (response) {
        console.log(response)
        if (vm.apiInfoSelect.messageCode === response.data.messageCode) {
          vm.csrfToken = response.data.csrfToken
          vm.countryCode = response.data.body.economicIndicator.countryCode
          vm.economicIndicator = response.data.body.economicIndicator
          vm.countryList = response.data.body.countryList
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
            economicIndicatorId: this.$route.params.economicIndicatorId,
            countryCode: this.countryCode,
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
            vm.resultMessage = response.data.messageText
            vm.isInput = false
            vm.$router.push(vm.backPage)
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
