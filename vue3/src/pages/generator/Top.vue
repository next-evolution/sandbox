<template>
  <Navigator :loading="isLoading" />

  <div class="container">
    <hr />
    <div class="row">
      <div class="col-auto">
        <select v-model="schemaName" ref="schemaName" v-on:change="schemaChange" class="form-select" style="width: auto">
          <option v-for="schema in schemaList" :key="schema" :value="schema">{{ schema }}</option>
        </select>
      </div>
      <div class="col-auto">
        <select v-model="tableName" ref="tableName" v-on:change="tableChange" class="form-select" style="width: auto">
          <option v-for="tableInfo in tableInfoList" :key="tableInfo.tableName" :value="tableInfo.tableName">
            {{ tableInfo.tableName }}
          </option>
        </select>
      </div>
      <div class="col-auto align-self-center">{{ resultMessage }}</div>
      <div class="col-auto">
        <button class="btn btn-primary btn-sm" @click="selectSql">SELECT</button>
        <button class="btn btn-success btn-sm" @click="insertSql">INSERT</button>
        <button class="btn btn-info btn-sm" @click="updateSql">UPDATE</button>
        <button class="btn btn-primary btn-sm" @click="deleteSql">DELETE</button>
        <button class="btn btn-success btn-sm" @click="memberSrc">member</button>
        <button class="btn btn-info btn-sm" @click="propertySrc">property</button>
        <ModalIndexInfo v-if="statisticsList.length > 0" :title="schemaName + '.' + tableName" :list="statisticsList" />
      </div>
    </div>

    <hr />

    <div class="row">
      <div class="col-sm-10">
        <input type="text" v-model="customTemplate" class="form-control" placeholder="custom code" />
      </div>
      <div class="col-auto">
        <ExecuteButton type="execute" @buttonClick="customSrc" />
      </div>
    </div>

    <hr />

    <table id="responseTable" class="table table-striped table-bordered">
      <thead class="table-dark">
        <tr>
          <th style="width: 1%; text-align: center">No</th>
          <th style="width: 1%; text-align: center">key</th>
          <th>Name</th>
          <th>Type</th>
          <th style="width: 1%; text-align: center">length</th>
          <th style="width: 1%; text-align: center">Null</th>
          <th>comment</th>
          <th>default</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="columnInfo in columnInfoList" :key="columnInfo.columnNo">
          <td style="text-align: right">{{ columnInfo.columnNo }}</td>
          <td style="text-align: center">{{ columnInfo.keyName }}</td>
          <td>{{ columnInfo.columnName }}</td>
          <td>{{ columnInfo.columnType }}</td>
          <td style="text-align: right">{{ columnInfo.columnLength }}</td>
          <td style="text-align: center">{{ columnInfo.required ? '' : '〇' }}</td>
          <td>{{ columnInfo.columnComment }}</td>
          <td>{{ columnInfo.columnDefault }}</td>
        </tr>
      </tbody>
    </table>
    <ModalResultCode v-if="isShowResultCode" :resultCode="resultCode" />
  </div>
  <div id="modalIndexInfo"></div>
  <div id="modalResultCode"></div>
</template>

<style>
th {
  text-align: left;
}
td {
  text-align: left;
}
</style>

<script>
import Navigator from '@/components/Navigator.vue'
import ExecuteButton from '@/components/ExecuteButton.vue'
import ModalIndexInfo from '@/components/ModalIndexInfo.vue'
import ModalResultCode from '@/components/ModalResultCode.vue'

export default {
  name: 'GeneratorTop',
  components: {
    Navigator,
    ExecuteButton,
    ModalIndexInfo,
    ModalResultCode,
  },
  data: function () {
    return {
      apiInfo: {
        apiCode: 'GE10010',
        url: this.getApiUrl('/generator/top'),
        messageCode: 'ZZ20010-I0',
        tableListEmptyCode: 'GE10010-E0',
      },
      apiInfoCustom: {
        apiCode: 'GE10020',
        url: this.getApiUrl('/generator/custom'),
        messageCode: 'ZZ99999-I0',
      },

      // apiCodeStatistics: "GE10040",
      // apiUrlStatistics: this.getApiUrl("/generator/statistics"),
      // apiSuccessCodeStatistics: "GE10040-I0",

      importDbUrl: '/generator/import/db',
      // importApiUrl: '/generator/import/api',
      // importInterfaceUrl: '/generator/import/interface',

      isLoading: false,
      isShowResultCode: false,

      resultMessage: '',

      schemaName: '',
      schemaList: [],

      tableName: '',
      tableInfoList: [],

      columnInfoList: [],
      columnKeyInfoList: [],

      statisticsList: [],

      srcType: '',
      customTemplate: '{No}/{Name}/{Type}/{Cmt}/{Mem}/{Prop}',

      resultCode: '',

      //   apiResponse: {
      //     apiCode: '',
      //     returnCode: 0,
      //     recordCount: 0,
      //     messageCode: '',
      //     messageText: '',
      //     resultCode: ''
      //   },

      columnInfoFields: [
        { key: 'columnNo', label: 'No', class: 'text-right' },
        { key: 'keyName', label: 'key', class: 'text-center' },
        { key: 'columnName', label: 'Name', isRowHeader: true, variant: 'primary' },
        { key: 'columnType', label: 'Type' },
        { key: 'columnLength', label: 'length', class: 'text-right' },
        { key: 'required', label: 'Null', class: 'text-center' },
        { key: 'columnComment', label: 'comment' },
        { key: 'columnDefault', label: 'default' },
      ],
      statisticsFields: [
        {
          key: 'indexName',
          label: 'Name',
          isRowHeader: true,
          variant: 'primary',
        },
        { key: 'seqInIndex', label: 'No', class: 'text-right' },
        { key: 'columnName', label: 'Column' },
        { key: 'nonUnique', label: 'UK', class: 'text-center' },
        { key: 'cardinality', label: 'cardinality', class: 'text-right' },
        { key: 'indexSchema', label: 'indexSchema' },
        { key: 'tableSchema', label: 'tableSchema' },
      ],
    }
  },
  mounted: function () {
    this.getTopData()
  },
  methods: {
    selectSql: function () {
      this.srcType = 'selectSql'
      this.getCustomCode()
    },
    insertSql: function () {
      this.srcType = 'insertSql'
      this.getCustomCode()
    },
    updateSql: function () {
      this.srcType = 'updateSql'
      this.getCustomCode()
    },
    deleteSql: function () {
      this.srcType = 'deleteSql'
      this.getCustomCode()
    },
    memberSrc: function () {
      this.srcType = 'memberSrc'
      this.getCustomCode()
    },
    propertySrc: function () {
      this.srcType = 'propertySrc'
      this.getCustomCode()
    },
    customSrc: function () {
      this.srcType = 'customSrc'
      this.getCustomCode()
    },
    schemaChange: function () {
      this.tableName = ''
      this.getTopData()
    },
    tableChange: function () {
      this.getTopData()
    },
    getTopData: function () {
      this.isLoading = true
      var vm = this
      this.axios
        .get(this.apiInfo.url + '?apiCode=' + this.apiInfo.apiCode + '&schemaName=' + this.schemaName + '&tableName=' + this.tableName, {
          headers: { 'X-ApiAuthCode': this.$store.state.authCode },
        })
        .then(function (response) {
          console.log(response)
          vm.resultMessage = response.data.messageText
          if (vm.apiInfo.messageCode === response.data.messageCode) {
            vm.csrfToken = response.data.csrfToken
            vm.schemaName = response.data.body.schemaName
            vm.schemaList = response.data.body.schemaList
            vm.tableName = response.data.body.tableName
            vm.tableInfoList = response.data.body.tableInfoList
            vm.columnInfoList = response.data.body.columnInfoList
            vm.columnKeyInfoList = response.data.body.columnKeyInfoList
            vm.statisticsList = response.data.body.statisticsList
            if (vm.statisticsList.length > 0) {
              vm.modalOpen = true
            }
          } else {
            if (vm.apiInfo.tableListEmptyCode === response.data.messageCode) {
              alert(response.data.messageText + '\n\n' + 'テーブル情報をimportしてください。')
              vm.$router.push(vm.importDbUrl)
            } else {
              alert(response.data.messageText)
            }
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
    getCustomCode: function () {
      var vm = this
      this.isShowResultCode = false
      this.isLoading = true
      this.axios
        .post(
          this.apiInfoCustom.url,
          {
            apiCode: this.apiInfoCustom.apiCode,
            schemaName: this.schemaName,
            tableName: this.tableName,
            srcType: this.srcType,
            customTemplate: this.customTemplate,
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
          if (vm.apiInfoCustom.messageCode === response.data.messageCode) {
            vm.resultCode = response.data.body.resultCode
          } else {
            vm.resultCode = response.data.messageText
            alert(response.data.messageText)
          }
          vm.isShowResultCode = true
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
