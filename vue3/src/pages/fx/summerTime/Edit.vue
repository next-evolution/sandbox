<template>
  <div id="mainContent">
    <Navigator :back-page="backPage" :loading="isLoading" />
    <div id="resultMessage">{{ resultMessage }}</div>
    <div v-show="!isInput">
      <router-link :to="backPage">戻る</router-link>
    </div>
    <div class="container" v-show="isInput">
      <div class="row" align-v="center" align-h="start">
        <div class="col-sm-3">夏時間編集フォーム</div>
      </div>
      <hr />
      <div class="row" align-v="center" align-h="start">
        <div class="col-sm-2">
          <label for="targetYear" class="control-label text-nowrap">対象年度</label>
        </div>
        <div class="col-sm-2">{{ summerTime.targetYear }}</div>
      </div>
      <hr />
      <div class="row" align-v="center" align-h="start">
        <div class="col-sm-2">
          <label for="applyStart" class="control-label text-nowrap">夏時間</label>
        </div>
        <div class="col-sm-3">
          <input type="date" v-model="summerTime.applyStart" ref="applyStart" class="form-control" placeholder="yyyy-MM-dd" />
        </div>
        <div class="col-sm-1">~</div>
        <div class="col-sm-3">
          <input type="date" v-model="summerTime.applyEnd" ref="applyEnd" class="form-control" placeholder="yyyy-MM-dd" />
        </div>
      </div>
      <hr />
      <div class="row" align-v="center" align-h="start">
        <div class="col-sm-2"></div>
        <div class="col-sm-2">
          <ExecuteButton type="update" @buttonClick="updateApi" />
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import Navigator from '@/components/Navigator.vue'
import ExecuteButton from '@/components/ExecuteButton.vue'

export default {
  name: 'FxSummerTimeEdit',
  components: {
    Navigator,
    ExecuteButton,
  },
  data: function () {
    return {
      backPage: '/fx/summerTime',
      apiInfo: {
        apiCode: 'FX14040',
        url: this.getApiUrl('/fx/summerTime/update'),
        messageCode: 'ZZ20050-I0',
      },
      apiInfoSelect: {
        apiCode: 'FX14020',
        url: this.getApiUrl('/fx/summerTime/select'),
        messageCode: 'ZZ20020-I0',
      },

      isLoading: false,
      isInput: true,
      resultMessage: '',

      csrfToken: '',

      targetYear: '',
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
  mounted: function () {
    this.isLoading = true
    this.$refs.applyStart.focus()
    var vm = this
    this.axios
      .get(this.apiInfoSelect.url + '?apiCode=' + this.apiInfoSelect.apiCode + '&targetYear=' + this.$route.params.targetYear, {
        headers: { 'X-ApiAuthCode': this.$store.state.authCode },
      })
      .then(function (response) {
        console.log(response)
        vm.apiResponse = response.data
        vm.csrfToken = response.data.csrfToken
        if (vm.apiInfoSelect.messageCode === vm.apiResponse.messageCode) {
          vm.summerTime = response.data.body.summerTime
          vm.targetYear = response.data.body.summerTime.targetYear
        } else {
          alert(vm.apiResponse.messageText)
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
            targetYear: this.targetYear,
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
          alert(vm.apiResponse.messageText)
          if (vm.apiInfo.messageCode === vm.apiResponse.messageCode) {
            vm.resultMessage = vm.apiResponse.messageText
            vm.isInput = false
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
  },
}
</script>
