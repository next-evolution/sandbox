<template>
  <div id="mainContent">
    <Navigator :loading="isLoading" />
    <div class="row" style="margin-left: 15px">
      <div class="col-auto align-self-center">経済指標データ：{{ resultMessage }}</div>
      <div class="col-auto">
        <select v-model="pageSize" ref="pageSize" v-on:change="pageSizeChange" class="form-select">
          <option v-for="pageSize in pageSizeList" :key="pageSize">{{ pageSize }}</option>
        </select>
      </div>
      <div class="col-auto align-self-center">
        <button v-if="pageNo === 1" class="btn btn-secondary" style="margin-left: 10px" disabled>&lt;</button>
        <button v-if="pageNo > 1" v-on:click="pageNoChange(-1)" class="btn btn-primary" style="margin-left: 10px">&lt;</button>
        <span style="margin-left: 5px; margin-right: 5px">[ {{ pageNo }} / {{ totalPageCount }} ]</span>
        <button v-if="totalPageCount <= pageNo" class="btn btn-secondary" style="margin-left: 1px" disabled>&gt;</button>
        <button v-if="totalPageCount > pageNo" v-on:click="pageNoChange(1)" class="btn btn-primary" style="margin-left: 1px">&gt;</button>
      </div>
      <div class="col-auto">
        <select v-model="countryCode" ref="countryCode" v-on:change="countryCodeChange">
          <option v-for="country in countryList" :key="country.countryCode" :value="country.countryCode">{{ country.countryName }}</option>
        </select>
      </div>
      <div class="col-auto">
        <select v-model="economicIndicatorId" ref="economicIndicatorId" v-on:change="searchApi">
          <option
            v-for="economicIndicator in economicIndicatorList"
            :key="economicIndicator.economicIndicatorId"
            :value="economicIndicator.economicIndicatorId"
          >
            {{ economicIndicator.economicIndicatorName }}
          </option>
        </select>
      </div>
      <!-- 年月日と指標名の検索はレイアウトの関係でコメントアウト
      <b-form-input type="date" v-model="publicationDateInput" placeholder="yyyyMMdd"></b-form-input>
      <b-form-input size="10" v-model="economicIndicatorName" placeholder="LIKE name"></b-form-input> -->
      <div class="col-auto">
        <router-link :to="makeAddPath()"> <button class="btn btn-primary">追加</button></router-link>
      </div>

      <table class="table table-striped table-hover">
        <thead>
          <tr>
            <th>DateTime</th>
            <th style="text-align: left">Name</th>
            <th>発表値</th>
            <th>予想値</th>
            <th>前回値</th>
            <th>memo</th>
          </tr>
        </thead>
        <tbody>
          <tr
            v-for="economicIndicatorData in economicIndicatorDataList"
            :key="economicIndicatorData.economicIndicatorId"
            v-on:click="select(economicIndicatorData)"
          >
            <td style="text-align: center">{{ economicIndicatorData.publicationDate }} {{ economicIndicatorData.publicationTime }}</td>
            <td>
              ( {{ economicIndicatorData.countryNameShort }} ) {{ economicIndicatorData.subTitle }} {{ economicIndicatorData.economicIndicatorName }}
            </td>
            <td style="text-align: right">{{ economicIndicatorData.resultValue }} {{ economicIndicatorData.unitOfValue }}</td>
            <td style="text-align: right">{{ economicIndicatorData.forecastValue }} {{ economicIndicatorData.unitOfValue }}</td>
            <td style="text-align: right">{{ economicIndicatorData.previousValue }} {{ economicIndicatorData.unitOfValue }}</td>
            <td>{{ economicIndicatorData.memo }}</td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<style>
.table th {
  text-align: center;
  vertical-align: middle;
}
.table td {
  text-align: left;
  vertical-align: middle;
}
select {
  height: 35px;
}
</style>

<script>
import Navigator from '@/components/Navigator.vue'

export default {
  name: 'FxEconomicIndicatorData',
  components: {
    Navigator,
  },
  data: function () {
    return {
      apiInfo: {
        apiCode: 'FX30010',
        url: this.getApiUrl('/fx/economicIndicatorData/search'),
        messageCode: 'ZZ20010-I0',
      },

      urlAdd: '/fx/economicIndicatorData/add',
      urlEdit: '/fx/economicIndicatorData/edit/',

      isLoading: false,

      searchCount: 0,
      pageNo: 1,
      pageSize: 15,
      totalPageCount: 0,

      resultMessage: '',

      pageSizeList: [5, 10, 15, 20, 25, 50],

      countryList: [],
      economicIndicatorList: [],
      economicIndicatorDataList: [],

      economicIndicatorId: 0,
      countryCode: '',

      //   appResponse: {
      //     apiCode: '',
      //     returnCode: 0,
      //     recordCount: 0,
      //     messageCode: '',
      //     messageText: ''
      //   },

      economicIndicatorDataFields: [
        {
          key: 'publicationDate',
          label: 'DateTime',
          sortable: true,
        },
        {
          key: 'economicIndicatorName',
          label: 'Name',
          sortable: false,
        },
        {
          key: 'resultValue',
          label: '発表値',
          sortable: true,
        },
        {
          key: 'forecastValue',
          label: '予想値',
          sortable: false,
        },
        {
          key: 'previousValue',
          label: '前回値',
          sortable: false,
        },
        {
          key: 'memo',
          label: 'memo',
          sortable: false,
        },
      ],
    }
  },
  created: function () {
    this.searchApi()
  },
  methods: {
    makeAddPath: function () {
      return this.urlAdd + '?countryCode=' + this.countryCode + '&economicIndicatorId=' + this.economicIndicatorId
    },
    makeEditPath: function (item) {
      var publicationDate = item.publicationDate.replace(/\//g, '')
      var publicationTime = item.publicationTime.replace(/:/g, '')
      return this.urlEdit + item.economicIndicatorId + '/' + publicationDate + '/' + publicationTime
    },
    pageSizeChange: function () {
      this.searchApi()
    },
    pageNoChange: function (value) {
      this.pageNo = this.pageNo + value
      this.searchApi()
    },
    countryCodeChange: function () {
      this.pageNo = 1
      this.searchApi()
    },
    economicIndicatorChange: function () {},
    economicIndicatorNameChange: function () {
      this.searchApi()
    },
    searchApi: function () {
      if (!this.isEmpty(this.publicationDateInput)) {
        this.publicationDate = this.publicationDateInput.replace(/-/g, '')
      }
      var vm = this
      this.isLoading = true
      this.axios
        .get(
          this.apiInfo.url +
            '?apiCode=' +
            this.apiInfo.apiCode +
            '&countryCode=' +
            this.countryCode +
            '&economicIndicatorId=' +
            this.economicIndicatorId +
            '&pageNo=' +
            this.pageNo +
            '&pageSize=' +
            this.pageSize,
          { headers: { 'X-ApiAuthCode': this.$store.state.authCode } }
        )
        .then(function (response) {
          console.log(response)
          if (vm.apiInfo.messageCode === response.data.messageCode) {
            vm.searchCount = response.data.body.searchCount
            vm.pageNo = response.data.body.pageNo
            vm.pageSize = response.data.body.pageSize
            vm.totalPageCount = response.data.body.totalPageCount
            vm.countryList = response.data.body.countryList
            vm.economicIndicatorList = response.data.body.economicIndicatorList
            vm.economicIndicatorDataList = response.data.body.economicIndicatorDataList
          } else {
            alert(response.data.messageText)
          }
          vm.resultMessage = response.data.messageText
        })
        .catch(function (error) {
          console.log(error)
          vm.$router.push('/error')
        })
        .finally(function () {
          vm.isLoading = false
        })
    },
    select: function (e) {
      this.$router.push(this.makeEditPath(e))
    },
  },
}
</script>
