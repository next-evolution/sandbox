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
      <div class="col">国マスタ登録フォーム</div>
    </div>
    <hr />
    <div class="row" align-v="center" align-h="start">
      <div class="col-sm-2">
        <label for="countryCode" class="control-label text-nowrap">国コード</label>
      </div>
      <div class="col-sm-2">
        <input type="tel" id="countryCode" ref="countryCode" v-model="country.countryCode" class="form-control" maxlength="8" />
      </div>
    </div>
    <hr />
    <div class="row" align-v="center" align-h="start">
      <div class="col-sm-2">
        <label for="countryName" class="control-label text-nowrap">国名称</label>
      </div>
      <div class="col-sm-4">
        <input type="text" id="countryName" ref="countryName" v-model="country.countryName" class="form-control" maxlength="64" />
      </div>
    </div>
    <hr />
    <div class="row" align-v="center" align-h="start">
      <div class="col-sm-2">
        <label for="countryNameEn" class="control-label text-nowrap">英語表記</label>
      </div>
      <div class="col-sm-4">
        <input type="tel" id="countryNameEn" ref="countryNameEn" v-model="country.countryNameEn" class="form-control" maxlength="64" />
      </div>
    </div>
    <hr />
    <div class="row" align-v="center" align-h="start">
      <div class="col-sm-2">
        <label for="countryNameShort" class="control-label text-nowrap">略称</label>
      </div>
      <div class="col-sm-2">
        <input type="text" id="countryNameShort" ref="countryNameShort" v-model="country.countryNameShort" class="form-control" maxlength="2" />
      </div>
    </div>
    <hr />
    <div class="row" align-v="center" align-h="start">
      <div class="col-sm-2">
        <label for="sortOrder" class="control-label text-nowrap">SortOrder</label>
      </div>
      <div class="col-sm-2">
        <input type="tel" id="sortOrder" ref="sortOrder" v-model="country.sortOrder" class="form-control" maxlength="5" />
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
  name: 'FxCountryAdd',
  components: {
    Navigator,
    ExecuteButton,
  },
  data: function () {
    return {
      backPage: '/fx/country',
      apiInfo: {
        apiCode: 'FX16030',
        url: this.getApiUrl('/fx/country/insert'),
        messageCode: 'ZZ20040-I0',
      },

      isLoading: false,
      isInput: true,
      resultMessage: '',

      csrfToken: '',

      country: {
        countryCode: '',
        countryName: '',
        countryNameEn: '',
        countryNameShort: '',
        sortOrder: '',
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
  mounted: async function () {
    this.$refs.countryCode.focus()
    this.csrfToken = await this.getCsrfToken(this)
  },
  methods: {
    initialize: async function () {
      this.isLoading = true
      this.isInput = true

      this.country.countryCode = ''
      this.country.countryName = ''
      this.country.countryNameEn = ''
      this.country.countryNameShort = ''
      this.country.sortOrder = ''

      this.resultMessage = ''
      this.$refs.countryCode.focus()
      this.csrfToken = await this.getCsrfToken(this)
    },
    insertApi: function () {
      if (this.isEmpty(this.country.countryCode)) {
        alert('input countryCode.')
        this.$refs.countryCode.focus()
        return
      }
      if (this.isEmpty(this.country.countryName)) {
        alert('input countryName.')
        this.$refs.countryName.focus()
        return
      }
      if (this.isEmpty(this.country.countryNameEn)) {
        alert('input countryNameEn.')
        this.$refs.countryNameEn.focus()
        return
      }
      if (this.isEmpty(this.country.countryNameShort)) {
        alert('input countryNameShort.')
        this.$refs.countryNameShort.focus()
        return
      }
      if (!this.isNumber(this.country.sortOrder)) {
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
            country: this.country,
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
