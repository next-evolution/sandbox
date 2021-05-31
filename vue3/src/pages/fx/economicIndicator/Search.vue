<template>
  <div id="mainContent">
    <Navigator :loading="isLoading" />
    <div class="container">
      <hr />
      <div class="row">
        <div class="col-auto align-self-center">経済指標マスタ：</div>
        <div class="col-auto">
          <select v-model="countryCode" ref="countryCode" v-on:change="countryCodeChange" class="form-select" style="width: auto">
            <option v-for="country in countryList" v-bind:key="country.countryCode" v-bind:value="country.countryCode">
              {{ country.countryName }}
            </option>
          </select>
        </div>
        <div class="col-auto">
          <select v-model="pageSize" ref="pageSize" v-on:change="pageSizeChange" class="form-select" style="width: auto">
            <option v-for="pageSize in pageSizeList" v-bind:key="pageSize" v-bind:value="pageSize">{{ pageSize }}</option>
          </select>
        </div>
        <div class="col-auto align-self-center">{{ apiResponse.messageText }}</div>
        <div class="col-md-auto align-self-center">
          <button v-if="pageNo === 1" class="btn btn-secondary" style="margin-left: 10px" disabled>&lt;</button>
          <button v-if="pageNo > 1" v-on:click="pageNoChange(-1)" class="btn btn-primary" style="margin-left: 10px">&lt;</button>
          <span style="margin-left: 5px; margin-right: 5px">[ {{ pageNo }} / {{ totalPageCount }} ]</span>
          <button v-if="totalPageCount <= pageNo" class="btn btn-secondary" style="margin-left: 1px" disabled>&gt;</button>
          <button v-if="totalPageCount > pageNo" v-on:click="pageNoChange(1)" class="btn btn-primary" style="margin-left: 1px">&gt;</button>
        </div>
        <div class="col-md-auto">
          <input type="text" size="20" v-model="economicIndicatorName" class="form-control" placeholder="LIKE name" />
        </div>
        <div class="col-auto">
          <button v-on:click="economicIndicatorNameChange" class="btn btn-primary form-control">Search</button>
        </div>
        <div class="col-auto">
          <router-link to="/fx/economicIndicator/add"><button class="btn btn-primary">追加</button></router-link>
        </div>
      </div>
      <hr />
      <table class="table table-hover">
        <thead>
          <tr>
            <th>id</th>
            <th>country</th>
            <th>name</th>
            <th>desc</th>
            <th>unit</th>
          </tr>
        </thead>
        <tbody>
          <tr
            v-for="economicIndicator in economicIndicatorList"
            v-bind:key="economicIndicator.economicIndicatorId"
            v-on:click="select(economicIndicator)"
          >
            <td>{{ economicIndicator.economicIndicatorId }}</td>
            <td>{{ economicIndicator.countryName }}</td>
            <td>{{ economicIndicator.economicIndicatorName }}</td>
            <td>{{ economicIndicator.economicIndicatorDescription }}</td>
            <td>{{ economicIndicator.unitOfValue }}</td>
          </tr>
        </tbody>
      </table>
    </div>
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
  name: 'FxEconomicIndicator',
  components: {
    Navigator,
  },
  data: function () {
    return {
      apiInfo: {
        apiCode: 'FX18010',
        url: this.getApiUrl('/fx/economicIndicator/search'),
        messageCode: 'ZZ20010-I0',
      },

      isLoading: false,

      pageSizeList: [5, 10, 15, 20, 25, 50],

      countryCode: '',
      economicIndicatorName: '',

      searchCount: 0,
      pageNo: 0,
      pageSize: 0,
      totalPageCount: 0,

      countryList: [
        {
          countryCode: '',
          countryName: '',
          countryNameEn: '',
          countryNameShort: '',
          sortOrder: 0,
        },
      ],
      economicIndicatorList: [
        {
          countryCode: '',
          countryName: '',
          countryNameShort: '',
          displayName: '',
          economicIndicatorDescription: '',
          economicIndicatorId: 0,
          economicIndicatorName: '',
          unitOfValue: '',
        },
      ],

      apiResponse: {
        apiCode: '',
        returnCode: 0,
        recordCount: 0,
        messageCode: '',
        messageText: '',
      },

      economicIndicatorFields: [
        {
          key: 'economicIndicatorId',
          label: 'id',
          sortable: false,
        },
        {
          key: 'countryCode',
          label: 'Country',
          sortable: true,
        },
        {
          key: 'economicIndicatorName',
          label: 'Name',
          sortable: false,
        },
        {
          key: 'economicIndicatorDescription',
          label: 'desc',
          sortable: false,
        },
        {
          key: 'unitOfValue',
          label: 'unit',
          sortable: false,
        },
      ],
    }
  },
  mounted: function () {
    this.searchApi()
  },
  methods: {
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
    economicIndicatorNameChange: function () {
      this.searchApi()
    },
    searchApi: function () {
      var vm = this
      this.isLoading = true
      this.axios
        .get(
          this.apiInfo.url +
            '?apiCode=' +
            this.apiInfo.apiCode +
            '&countryCode=' +
            this.countryCode +
            '&economicIndicatorName=' +
            this.economicIndicatorName +
            '&pageNo=' +
            this.pageNo +
            '&pageSize=' +
            this.pageSize,
          { headers: { 'X-ApiAuthCode': this.$store.state.authCode } }
        )
        .then(function (response) {
          console.log(response)
          vm.apiResponse = response.data
          if (vm.apiInfo.messageCode === response.data.messageCode) {
            vm.searchCount = response.data.body.searchCount
            vm.pageNo = response.data.body.pageNo
            vm.pageSize = response.data.body.pageSize
            vm.totalPageCount = response.data.body.totalPageCount
            vm.countryList = response.data.body.countryList
            vm.economicIndicatorList = response.data.body.economicIndicatorList
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
    select: function (e) {
      this.$router.push('/fx/economicIndicator/edit/' + e.economicIndicatorId + '/' + e.countryCode)
    },
  },
}
</script>
