<template>
  <Navigator :loading="isLoading" />
  <div class="container">
    <hr />
    <div v-if="!isLoading" class="row">
      <div class="col-auto align-self-center">フロント日：</div>
      <div class="col-auto">
        <select v-show="!isLoading" v-model="frontDate" ref="frontDate" class="form-select">
          <option v-for="frontDate in frontDateList" :key="frontDate">{{ frontDate }}</option>
        </select>
      </div>
      <div class="col-sm-2">
        <input type="text" v-model="frontDateManual" class="form-control" placeholder="fron date" />
      </div>
      <div class="col-auto">
        <button class="btn btn-primary" v-on:click="generateApi">Generate</button>
      </div>
    </div>
    <hr />
    <table class="table table-striped">
      <thead>
        <th>currencyPair</th>
        <th>30m</th>
        <th>Hour</th>
        <th>Count</th>
        <th>Day</th>
        <th>Count</th>
        <th>Status</th>
      </thead>
      <tbody>
        <tr v-for="generateResult in generateResultList" :key="generateResult.currencyPair">
          <td>{{ generateResult.currencyPairCode }}</td>
          <td>{{ generateResult.latestFrontDate30m }}</td>
          <td>{{ generateResult.latestFrontDateHour }}</td>
          <td>{{ generateResult.generateCountHour }}</td>
          <td>{{ generateResult.latestFrontDateDay }}</td>
          <td>{{ generateResult.generateCountDay }}</td>
          <td>{{ generateResult.generateStatus }}</td>
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
</style>

<script>
import Navigator from '@/components/Navigator.vue'

export default {
  name: 'FxBarDataGenerate',
  components: {
    Navigator,
  },
  data: function () {
    return {
      apiInfo: {
        apiCode: 'FX29010',
        url: this.getApiUrl('/fx/bardata/generate'),
        messageCode: 'ZZ20010-I0',
      },

      apiInfoGenerate: {
        apiCode: 'FX29020',
        url: this.getApiUrl('/fx/bardata/generate'),
        messageCode: 'ZZ99999-I0',
      },

      isLoading: false,
      isGenerate: false,
      csrfToken: '',

      frontDate: '',
      frontDateManual: '',
      frontDateValue: '',
      frontDateList: [],

      generateResultList: [],
      generateResult: {
        currencyPairCode: '',
        latestFrontDate30m: '',
        latestFrontDate1h: '',
        latestFrontDate1d: '',
      },
    }
  },
  mounted: function () {
    this.isLoading = true
    var vm = this
    this.axios
      .get(this.apiInfo.url + '?apiCode=' + this.apiInfo.apiCode, {
        headers: { 'X-ApiAuthCode': this.$store.state.authCode },
      })
      .then(function (response) {
        console.log(response)
        if (vm.apiInfo.messageCode === response.data.messageCode) {
          vm.csrfToken = response.data.csrfToken
          vm.frontDateList = response.data.body.frontDateList
          vm.generateResultList = response.data.body.barDataGenerateResultList
        }
      })
      .catch(function (error) {
        console.log(error)
        vm.$router.push('/error')
      })
      .finally(function () {
        vm.isLoading = false
        vm.$nextTick(() => vm.$refs.frontDate.focus())
      })
  },
  methods: {
    generateApi: async function () {
      if (!this.isEmpty(this.frontDateManual)) {
        this.frontDateValue = this.frontDateManual
      } else {
        this.frontDateValue = this.frontDate
      }
      if (!this.isDate(this.frontDateValue)) {
        alert('frontDate is not yyyyMMdd.')
        return
      }
      this.isLoading = true
      this.isGenerate = true

      for (var idx in this.generateResultList) {
        var vm = this
        await this.axios
          .post(
            this.apiInfoGenerate.url,
            {
              apiCode: this.apiInfoGenerate.apiCode,
              authCode: this.$store.state.authCode,
              frontDate: this.frontDateValue,
              currencyPairCode: vm.generateResultList[idx].currencyPairCode,
            },
            {
              headers: {
                'Content-Type': 'application/json',
                'X-CSRF-TOKEN': this.csrfToken,
                'X-ApiAuthCode': this.$store.state.authCode,
              },
              responseType: 'json',
            }
          )
          .then(function (response) {
            console.log(response)
            if (vm.apiInfoGenerate.messageCode === response.data.messageCode) {
              vm.generateResultList[idx] = response.data.body.barDataGenerateResult
            }
          })
          .catch(function (error) {
            console.log(error)
            vm.$router.push('/error')
          })
      }
      this.isLoading = false
      this.isGenerate = false
      var okCount = 0
      for (var iListIndex = 0; iListIndex < this.generateResultList.length; iListIndex++) {
        if (this.generateResultList[iListIndex].generateStatus === 'OK') {
          okCount++
        }
      }
      this.isComplete = this.generateResultList.length = okCount
      if (this.isComplete) {
        this.resultMessage = '( ^ ^ ) Csv Upload Complete ( ^ ^ )'
      } else {
        this.resultMessage = '( - o - ) Csv Upload Error. ( - o - )'
      }
      vm.$nextTick(() => alert(this.resultMessage))
    },
  },
}
</script>
