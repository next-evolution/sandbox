<template>
  <div id="mainContent">
    <Navigator :back-page="backPage" :loading="isLoading" />
    <div id="resultMessage">{{ resultMessage }}</div>
    <div v-show="!isInput">
      <router-link :to="backPage">戻る</router-link>
      <br />
      <button class="btn-primary" v-on:click="initialize">続けて登録</button>
    </div>
    <div class="container" v-show="isInput">
      <div class="row" align-v="center" align-h="start">
        <div class="col-sm-3">夏時間登録フォーム</div>
      </div>
      <hr />
      <div class="row" align-v="center" align-h="start">
        <div class="col-sm-2">
          <label for="targetYear" class="control-label text-nowrap">対象年度</label>
        </div>
        <div class="col-sm-2">
          <input type="tel" id="targetYear" ref="targetYear" v-model="summerTime.targetYear" class="form-control" maxlength="4" />
        </div>
      </div>
      <hr />
      <div class="row" align-v="center" align-h="start">
        <div class="col-sm-2">
          <label for="applyStart" class="control-label text-nowrap">夏時間</label>
        </div>
        <div class="col-sm-2">
          <input type="date" v-model="summerTime.applyStart" ref="applyStart" placeholder="yyyy-MM-dd" class="form-control" />
        </div>
        <div class="col-sm-1">~</div>
        <div class="col-sm-2">
          <input type="date" v-model="summerTime.applyEnd" ref="applyEnd" class="form-control" placeholder="yyyy-MM-dd" />
        </div>
      </div>
      <hr />
      <div class="row" align-v="center" align-h="start">
        <div class="col-sm-2"></div>
        <div class="col-sm-2">
          <ExecuteButton type="insert" @buttonClick="insertApi" />
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import Navigator from '@/components/Navigator.vue'
import ExecuteButton from '@/components/ExecuteButton.vue'

export default {
  name: 'FxSummerTimeAdd',
  components: {
    Navigator,
    ExecuteButton,
  },
  data: function () {
    return {
      backPage: '/fx/summerTime',
      apiInfo: {
        apiCode: 'FX14030',
        url: this.getApiUrl('/fx/summerTime/insert'),
        messageCode: 'ZZ20040-I0',
      },

      isLoading: false,
      isInput: true,
      resultMessage: '',

      csrfToken: '',

      summerTime: {
        targetYear: '',
        applyStart: '',
        applyEnd: '',
      },

      summerTimeData: {
        targetYear: '',
        applyStart: '',
        applyEnd: '',
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
    this.csrfToken = await this.getCsrfToken(this)
  },
  methods: {
    initialize: async function () {
      this.isLoading = true
      this.isInput = true
      this.summerTime.targetYear = ''
      this.summerTime.applyStart = ''
      this.summerTime.applyEnd = ''
      this.resultMessage = ''

      this.csrfToken = await this.getCsrfToken(this)
    },
    insertApi: function () {
      if (!this.isYear(this.summerTime.targetYear)) {
        alert('not year.')
        this.$refs.targetYear.focus()
        return
      }
      if (!this.isDate(this.summerTime.applyStart)) {
        alert('select applyStart.')
        this.$refs.applyStart.focus()
        return
      }
      if (!this.isDate(this.summerTime.applyEnd)) {
        alert('select applyEnd.')
        this.$refs.applyEnd.focus()
        return
      }

      this.summerTimeData.targetYear = this.summerTime.targetYear
      this.summerTimeData.applyStart = this.summerTime.applyStart.replace(/-/g, '')
      this.summerTimeData.applyEnd = this.summerTime.applyEnd.replace(/-/g, '')

      this.isLoading = true
      var vm = this
      this.axios
        .post(
          this.apiInfo.url,
          {
            apiCode: this.apiInfo.apiCode,
            authCode: this.$store.state.authCode,
            summerTime: this.summerTimeData,
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
          if (vm.apiInfo.messageCode === vm.apiResponse.messageCode) {
            vm.resultMessage = vm.apiResponse.messageText
            vm.isInput = false
          }
          alert(vm.apiResponse.messageText)
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
