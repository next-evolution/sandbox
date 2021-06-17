<template>
  <Navigator :back-page="backPage" :loading="isLoading" />
  <div class="container">
    <div class="row">
      <div class="col-auto">
        <h3>InterfaceExcel</h3>
      </div>
      <div class="col-auto">
        <label class="btn btn-primary"
          >Excel Upload
          <input type="file" multiple ref="uploadFiles" accept=".xlsx" @change="uploadExcel" style="display: none" />
        </label>
      </div>
      <div class="col-auto">{{ resultMessage }}</div>
    </div>

    <table class="table table-striped table-sm" style="max-height: 90%">
      <tbody>
        <tr>
          <th>Book</th>
          <th>ApiCode</th>
          <th>read</th>
          <th>import</th>
          <th>upload</th>
          <th>import</th>
          <th>message</th>
        </tr>
        <tr v-for="excelImportResult in excelImportResultList" :key="excelImportResult.bookName + excelImportResult.sheetName">
          <td>{{ excelImportResult.bookName }}</td>
          <td>{{ excelImportResult.sheetName }}</td>
          <td align="right">{{ excelImportResult.readCount }}</td>
          <td align="right">{{ excelImportResult.importCount }}</td>
          <td>{{ excelImportResult.uploadResult }}</td>
          <td>{{ excelImportResult.importResult }}</td>
          <td>{{ excelImportResult.message }}</td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<style scoped>
label {
  padding: 5px 5px;
  color: #fcfcfc;
  background-color: #333333;
}
th,
td {
  text-align: left;
}
</style>

<script>
import Navigator from '@/components/Navigator.vue'

export default {
  name: 'GeneratorImportInterface',
  components: {
    Navigator,
  },
  data: function () {
    return {
      backPage: '/generator',
      apiInfo: {
        apiCode: 'GE40010',
        url: this.getApiUrl('/generator/interface/upload'),
        messageCode: 'ZZ40010-I0',
      },
      apiInfoInitialize: {
        apiCode: 'GE40020',
        url: this.getApiUrl('/generator/interface/initialize'),
        messageCode: 'ZZ40020-I0',
      },
      apiInfoImport: {
        apiCode: 'GE40030',
        url: this.getApiUrl('/generator/interface/import'),
        messageCode: 'ZZ40030-I0',
      },

      isLoading: false,
      resultMessage: '',

      csrfToken: '',

      // upload
      uploadFileList: [],
      excelImportResultList: [],

      //   apiResponse: {
      //     apiCode: '',
      //     returnCode: 0,
      //     recordCount: 0,
      //     messageCode: '',
      //     messageText: ''
      //   }
    }
  },
  mounted: async function () {
    this.csrfToken = await this.getCsrfToken(this)
  },
  methods: {
    uploadExcel: async function (e) {
      console.log(e)
      this.uploadFileList = e.target.files
      var uploadFileCount = this.uploadFileList.length

      if (this.uploadFileList.length === 0) {
        return
      }
      var sMsg = uploadFileCount + ' ファイルをuploadしますか？'
      if (!confirm(sMsg)) {
        this.$refs.uploadFiles.value = null
        return
      }
      this.isLoading = true
      var vm = this
      const formData = new FormData()
      formData.append('apiCode', this.apiInfo.apiCode)
      formData.append('authCode', vm.$store.state.authCode)
      for (var i = 0; i < this.uploadFileList.length; i++) {
        formData.append('uploadFileList[]', this.uploadFileList[i])
      }
      await this.axios
        .post(this.apiInfo.url, formData, {
          headers: {
            'Content-Type': 'multipart/form-data',
            'X-CSRF-TOKEN': this.csrfToken,
            'X-ApiAuthCode': this.$store.state.authCode,
          },
          responseType: 'json',
        })
        .then(function (response) {
          console.log(response)
          // if (vm.apiInfo.messageCode === response.data.messageCode) {
          vm.apiResponse = response.data
          vm.excelImportResultList = response.data.body.excelImportResultList
          vm.resultMessage = vm.apiResponse.messageText
          // }
        })
        .catch(function (error) {
          console.log(error)
          vm.$router.push('/error')
        })
        .finally(function () {
          vm.isLoading = false
          vm.uploadFileList = null
        })
      if (vm.apiInfo.messageCode !== this.apiResponse.messageCode) {
        alert(this.apiResponse.messageText)
        return
      }

      var isError = false
      // initialize
      await this.axios
        .post(
          this.apiInfoInitialize.url,
          {
            apiCode: this.apiInfoInitialize.apiCode,
            authCode: vm.$store.state.authCode,
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
          if (vm.apiInfoInitialize.messageCode !== response.data.messageCode) {
            alert(response.data.messageText)
            isError = true
          }
        })
        .catch(function (error) {
          alert(error)
          console.log(error)
          isError = true
        })

      if (isError) {
        return
      }
      this.isLoading = true

      for (var idx = 0; idx < this.excelImportResultList.length; idx++) {
        vm = this
        await this.axios
          .post(
            this.apiInfoImport.url,
            {
              apiCode: this.apiInfoImport.apiCode,
              authCode: vm.$store.state.authCode,
              bookName: vm.excelImportResultList[idx].bookName,
              sheetName: vm.excelImportResultList[idx].sheetName,
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
            if (response.data.body.excelImportResult != null) {
              vm.excelImportResultList[idx] = response.data.body.excelImportResult
            } else {
              vm.excelImportResultList[idx].importResult = 'FATAL'
              vm.excelImportResultList[idx].message = response.data.messageText
            }
          })
          .catch(function (error) {
            alert(error)
            console.log(error)
            vm.excelImportResultList[idx].importResult = 'FATAL'
            vm.excelImportResultList[idx].message = error
            isError = true
          })
        if (isError) {
          break
        }
      }
      this.isLoading = false
      var okCount = 0
      for (var iListIndex = 0; iListIndex < this.excelImportResultList.length; iListIndex++) {
        if (this.excelImportResultList[iListIndex].importResult === 'OK') {
          okCount++
        }
      }
      if (this.excelImportResultList.length === okCount) {
        this.resultMessage = '( ^ ^ ) Excel Import Complete ( ^ ^ )'
      } else {
        this.resultMessage = '( - o - ) Excel Import Error. ( - o - )'
      }
      alert(this.resultMessage)
    },
  },
}
</script>
