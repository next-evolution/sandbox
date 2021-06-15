<template>
  <Navigator :loading="isLoading" />
  <div class="container">
    <div v-if="!isLoading" align="left">
      <label v-if="viewMode === 1 || isComplete" class="btn btn-primary">
        CSV upload
        <input type="file" multiple ref="uploadFiles" accept=".csv" @change="uploadCsv" style="display: none" />
      </label>
      <span style="margin-left: 20px">{{ resultMessage }}</span>
    </div>

    <table v-if="viewMode === 1" class="table table-striped">
      <tbody>
        <tr>
          <th style="width: 10%">currencyPair</th>
          <th style="width: 90%; text-align: left">barDateTime</th>
        </tr>
        <tr v-for="barDataStatus in barDataStatusList" :key="barDataStatus.currencyPairCode">
          <td>{{ barDataStatus.currencyPairCode }}</td>
          <td style="text-align: left">{{ barDataStatus.barDateTime }}</td>
        </tr>
      </tbody>
    </table>
    <table v-if="viewMode === 2" class="table table-striped">
      <tbody>
        <tr>
          <th>currencyPair</th>
          <th>barDateTimeForTable</th>
          <th>fileName</th>
          <th>fileSize</th>
          <th>barDateTime</th>
          <th>Result</th>
          <th>read</th>
          <th>exists</th>
          <th>insert</th>
          <th>diff</th>
          <th>message</th>
        </tr>
        <tr v-for="barDataCsvInfo in barDataCsvInfoList" :key="barDataCsvInfo.currencyPairCode">
          <td style="text-align: center">{{ barDataCsvInfo.currencyPairCode }}</td>
          <td style="text-align: center">{{ barDataCsvInfo.barDateTimeForTable }}</td>
          <td style="text-align: center">{{ barDataCsvInfo.fileName }}</td>
          <td style="text-align: right">{{ barDataCsvInfo.fileSize }}</td>
          <td style="text-align: center">{{ barDataCsvInfo.barDateTime }}</td>
          <td style="text-align: center">{{ barDataCsvInfo.uploadResult }}</td>
          <td style="text-align: right">{{ barDataCsvInfo.readCount }}</td>
          <td style="text-align: right">{{ barDataCsvInfo.existsCount }}</td>
          <td style="text-align: right">{{ barDataCsvInfo.insertCount }}</td>
          <td style="text-align: right">{{ barDataCsvInfo.differenceCount }}</td>
          <td style="text-align: left">{{ barDataCsvInfo.message }}</td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script>
import Navigator from '@/components/Navigator.vue'

export default {
  name: 'FxBarDataUpload',
  components: {
    Navigator,
  },
  data: function () {
    return {
      apiInfo: {
        apiCode: 'FX26050',
        url: this.getApiUrl('/fx/bardata/upload'),
        messageCode: 'ZZ20010-I0',
      },
      apiInfoUpload: {
        apiCode: 'FX26060',
        url: this.getApiUrl('/fx/bardata/upload'),
        messageCode: 'FX26060-I0',
      },
      apiInfoImport: {
        apiCode: 'FX26080',
        url: this.getApiUrl('/fx/bardata/import'),
        messageCode: 'FX26080-I0',
      },

      isLoading: false,
      viewMode: 1,
      isComplete: false,
      resultMessage: '',

      // status
      csrfToken: '',

      barDataStatusList: [],
      barDataStatus: {
        currencyPairCode: '',
        barDateTime: '',
      },

      // upload
      uploadFileList: [],

      currencyPairCode: '',
      //
      barDataCsvInfoList: [],

      barDataCsvInfo: {
        currencyPairCode: '',
        barDateTimeForTable: '',
        fileName: '',
        fileSize: 0,
        barDateTime: '',
        uploadResult: '',
        readCount: 0,
        existsCount: 0,
        insertCount: 0,
        differenceCount: 0,
        message: '',
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
          vm.barDataStatusList = response.data.body.barDataStatusList
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
    uploadCsv: async function (e) {
      console.log(e)
      this.uploadFileList = e.target.files
      var uploadFileCount = this.uploadFileList.length

      if (this.uploadFileList.length > 0) {
        var sMsg = uploadFileCount + ' ファイルをuploadしますか？'
        if (!confirm(sMsg)) {
          this.$refs.uploadFiles.value = null
          return
        }
        this.isUpload = true
        this.isLoading = true
        var vm = this
        const formData = new FormData()
        formData.append('apiCode', this.apiInfoUpload.apiCode)
        formData.append('authCode', vm.$store.state.authCode)
        for (var i = 0; i < this.uploadFileList.length; i++) {
          formData.append('uploadFileList[]', this.uploadFileList[i])
        }
        await this.axios
          .post(this.apiInfoUpload.url, formData, {
            headers: {
              'Content-Type': 'multipart/form-data',
              'X-CSRF-TOKEN': this.csrfToken,
              'X-ApiAuthCode': this.$store.state.authCode,
            },
            responseType: 'json',
          })
          .then(function (response) {
            console.log(response)
            // if (vm.apiInfoUpload.messageCode === response.data.messageCode) {
            vm.apiResponse = response.data
            vm.barDataCsvInfoList = response.data.body.barDataCsvInfoList
            // }
            vm.resultMessage = response.data.messageText
            vm.viewMode = 2
          })
          .catch(function (error) {
            console.log(error)
            vm.$router.push('/error')
          })
          .finally(function () {
            vm.isLoading = false
            vm.uploadFileList = null
          })
      }
      if (vm.apiInfoUpload.messageCode !== this.apiResponse.messageCode) {
        this.resultMessage = 'upload error.'
        alert(this.apiResponse.messageText)
        return
      }
      this.resultMessage = 'upload complete. -> start loading.'
      this.isLoading = true
      for (var idx = 0; idx < this.barDataCsvInfoList.length; idx++) {
        vm = this
        await this.axios
          .post(
            this.apiInfoImport.url,
            {
              apiCode: this.apiInfoImport.apiCode,
              authCode: vm.$store.state.authCode,
              currencyPairCode: vm.barDataCsvInfoList[idx].currencyPairCode,
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
            if (vm.apiInfoImport.messageCode === response.data.messageCode) {
              vm.barDataCsvInfoList[idx] = response.data.body.barDataCsvInfo
            } else {
              vm.barDataCsvInfoList[idx].uploadResult = 'ERROR'
              vm.barDataCsvInfoList[idx].message = response.data.messageText
            }
          })
          .catch(function (error) {
            console.log(error)
            //vm.$router.push('/error')
            vm.barDataCsvInfoList[idx].uploadResult = 'FATAL'
            vm.barDataCsvInfoList[idx].message = error
          })
      }
      this.isLoading = false
      var okCount = 0
      for (var iListIndex = 0; iListIndex < this.barDataCsvInfoList.length; iListIndex++) {
        if (this.barDataCsvInfoList[iListIndex].uploadResult === 'OK') {
          okCount++
        }
      }
      this.isComplete = this.barDataCsvInfoList.length === okCount
      if (this.isComplete) {
        this.resultMessage = '( ^ ^ ) Csv Upload Complete ( ^ ^ )'
        alert('Csv Upload Complete')
      } else {
        this.resultMessage = '( - o - ) Csv Upload Error. ( - o - )'
        alert('Csv Upload Error')
      }
    },
    reload: function () {
      this.$router.go({ path: this.$router.currentRoute.path, force: true })
    },
  },
}
</script>
