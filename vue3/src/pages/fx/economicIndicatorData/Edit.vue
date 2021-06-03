<template>
  <div id="mainContent">
    <Navigator :back-page="backPage" :loading="isLoading" />
    <div id="resultMessage">{{ resultMessage }}</div>
    <div v-show="!isInput">
      <router-link to="/fx/economicIndicatorData">戻る</router-link>
    </div>
    <div class="container" v-if="isInput">
      <div class="row" align-v="center" align-h="start">
        <div class="col">経済指標データ登録</div>
      </div>
      <hr />
      <div class="row" align-v="center" align-h="start">
        <div class="col-sm-2">
          <label for="countryCode" class="control-label text-nowrap">国コード</label>
        </div>
        <div class="col-auto">
          <select v-model="countryCode" ref="countryCode" v-on:change="changeCountryCode" class="form-select">
            <option v-for="country in countryList" :key="country.countryCode" :value="country.countryCode">{{ country.countryName }}</option>
          </select>
        </div>
      </div>
      <hr />
      <div class="row" align-v="center" align-h="start">
        <div class="col-sm-2">
          <label for="economicIndicatorId" class="control-label text-nowrap">経済指標</label>
        </div>
        <div class="col-auto">
          <select v-model="economicIndicatorData.economicIndicatorId" ref="economicIndicatorId" class="form-select">
            <option
              v-for="economicIndicator in economicIndicatorList"
              :key="economicIndicator.economicIndicatorId"
              :value="economicIndicator.economicIndicatorId"
            >
              {{ economicIndicator.economicIndicatorName }}
            </option>
          </select>
        </div>
      </div>
      <hr />
      <div class="row" align-v="center" align-h="start">
        <div class="col-sm-2">
          <label for="publicationDate" class="control-label text-nowrap">発表日時</label>
        </div>
        <div class="col-auto">
          <input type="date" v-model="economicIndicatorData.publicationDate" ref="publicationDate" placeholder="yyyyMMdd" class="form-control" />
        </div>
        <div class="col-auto">
          <input
            type="time"
            step="300"
            v-model="economicIndicatorData.publicationTime"
            ref="publicationTime"
            placeholder="HHmm"
            class="form-control"
          />
        </div>
      </div>
      <hr />
      <div class="row" align-v="center" align-h="start">
        <div class="col-sm-2">
          <label for="subTitle" class="control-label text-nowrap">サブタイトル</label>
        </div>
        <div class="col-sm-3">
          <input type="text" v-model="economicIndicatorData.subTitle" placeholder="subtitle" class="form-control" />
        </div>
      </div>
      <hr />
      <div class="row" align-v="center" align-h="start">
        <div class="col-sm-2">
          <label for="resultValue" class="control-label text-nowrap">発表値</label>
        </div>
        <div class="col-sm-2">
          <input type="text" v-model="economicIndicatorData.resultValue" ref="resultValue" placeholder="発表値" class="form-control" />
        </div>
        <div class="col-sm-2">
          <input type="text" v-model="economicIndicatorData.forecastValue" placeholder="予想" class="form-control" />
        </div>
        <div class="col-sm-2">
          <input type="text" v-model="economicIndicatorData.previousValue" placeholder="前回" class="form-control" />
        </div>
      </div>
      <hr />
      <div class="row" align-v="center" align-h="start">
        <div class="col-sm-2">
          <label for="memo" class="control-label text-nowrap">メモ</label>
        </div>
        <div class="col-auto">
          <textarea v-model="economicIndicatorData.memo" placeholder="memo..." cols="80" rows="3" class="form-control"></textarea>
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
  </div>
</template>

<script>
import Navigator from '@/components/Navigator.vue'
import ExecuteButton from '@/components/ExecuteButton.vue'

export default {
  name: 'economicIndicatorDataEdit',
  components: {
    Navigator,
    ExecuteButton,
  },
  data: function () {
    return {
      backPage: '/fx/economicIndicatorData',
      apiInfo: {
        apiCode: 'FX30040',
        url: this.getApiUrl('/fx/economicIndicatorData/update'),
        messageCode: 'ZZ20050-I0',
      },
      apiInfoSelect: {
        apiCode: 'FX30020',
        url: this.getApiUrl('/fx/economicIndicatorData/select'),
        messageCode: 'ZZ20020-I0',
      },
      apiInfoMaster: {
        apiCode: 'FX30030',
        url: this.getApiUrl('/fx/economicIndicatorData/master'),
        messageCode: 'ZZ30010-I0',
      },

      isLoading: true,
      isInput: true,
      resultMessage: '',

      csrfToken: '',

      countryCode: '',
      countryCodeBase: '',
      countryList: [],
      economicIndicatorList: [],

      economicIndicatorId: 0,
      publication: '',
      economicIndicatorData: {
        economicIndicatorId: 0,
        publicationDate: '',
        publicationTime: '',
        subTitle: '',
        resultValue: '',
        forecastValue: '',
        previousValue: '',
        memo: '',
      },

      //   apiResponse: {
      //     apiCode: '',
      //     returnCode: 0,
      //     recordCount: 0,
      //     messageCode: '',
      //     messageText: ''
      //   }
    }
  },
  created: function () {
    this.economicIndicatorId = this.$route.params.economicIndicatorId
    this.publication = this.$route.params.publicationDate + this.$route.params.publicationTime

    this.isLoading = true
    var vm = this
    this.axios
      .get(
        this.apiInfoSelect.url +
          '?apiCode=' +
          this.apiInfoSelect.apiCode +
          '&economicIndicatorId=' +
          this.economicIndicatorId +
          '&publication=' +
          this.publication,
        { headers: { 'X-ApiAuthCode': this.$store.state.authCode } }
      )
      .then(function (response) {
        console.log(response)
        if (vm.apiInfoSelect.messageCode === response.data.messageCode) {
          vm.csrfToken = response.data.csrfToken
          // countryCodeBaseは最初に選択した経済指標の国コード
          vm.countryCodeBase = response.data.body.countryCode
          vm.countryCode = response.data.body.countryCode
          vm.countryList = response.data.body.countryList
          vm.economicIndicatorList = response.data.body.economicIndicatorList
          vm.economicIndicatorData = response.data.body.economicIndicatorData
          vm.economicIndicatorData.publicationDate = vm.economicIndicatorData.publicationDate.replace(/\//g, '-')
        } else {
          vm.resultMessage = response.data.messageText
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
    changeCountryCode: function () {
      this.isLoading = true
      var vm = this
      this.axios
        .get(
          this.apiInfoMaster.url +
            '?apiCode=' +
            this.apiInfoMaster.apiCode +
            '&countryCode=' +
            this.countryCode +
            '&economicIndicatorId=' +
            this.economicIndicatorId,
          { headers: { 'X-ApiAuthCode': this.$store.state.authCode } }
        )
        .then(function (response) {
          console.log(response)

          if (vm.apiInfoMaster.messageCode === response.data.messageCode) {
            vm.csrfToken = response.data.csrfToken
            vm.economicIndicatorData.countryCode = response.data.body.countryCode
            vm.countryList = response.data.body.countryList
            vm.economicIndicatorList = response.data.body.economicIndicatorList

            if (response.data.economicIndicatorList != null && response.data.body.economicIndicatorList.length > 0) {
              vm.economicIndicatorData.economicIndicatorId = response.data.body.economicIndicatorList[0].economicIndicatorId
              if (vm.countryCodeBase === vm.economicIndicatorData.body.countryCode) {
                if (vm.economicIndicatorId > 0) {
                  vm.economicIndicatorData.economicIndicatorId = vm.economicIndicatorId
                }
              }
            }
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
    updateApi: function () {
      if (this.isEmpty(this.economicIndicatorData.countryCode)) {
        alert('select countryCode.')
        this.$refs.countryCode.focus()
        return
      }
      if (this.economicIndicatorData.economicIndicatorId < 1) {
        alert('select economicIndicator.')
        this.$refs.economicIndicatorId.focus()
        return
      }
      if (this.isEmpty(this.economicIndicatorData.publicationDate)) {
        alert('select publicationDate.')
        this.$refs.publicationDate.focus()
        return
      }
      if (!this.isDate(this.economicIndicatorData.publicationDate)) {
        alert('select publicationDate.')
        this.$refs.publicationDate.focus()
        return
      }
      if (!this.isTime(this.economicIndicatorData.publicationTime + ':00')) {
        alert('select publicationTime.')
        this.$refs.publicationTime.focus()
        return
      }
      if (this.isEmpty(this.economicIndicatorData.resultValue)) {
        alert('input resultValue.')
        this.$refs.resultValue.focus()
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
            economicIndicatorId: this.economicIndicatorId,
            publication: this.publication,
            economicIndicatorData: this.economicIndicatorData,
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
            vm.isInput = false
            vm.resultMessage = response.data.messageText
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
