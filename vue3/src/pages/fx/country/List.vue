<template>
  <div id="mainContent">
    <Navigator :loading="isLoading" />
    <div class="container">
      <div style="text-align: left">
        国マスタ：<span style="padding-right: 30px">{{ apiResponse.messageText }}</span>
        <router-link :to="{ name: 'FxCountryAdd' }">
          <button class="btn btn-primary" size="sm">国マスタ追加</button>
        </router-link>
      </div>
      <table class="table table-hover">
        <thead>
          <tr>
            <th>Code</th>
            <th>Name</th>
            <th>NameEn</th>
            <th>NameShort</th>
            <th>Order</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="country in countryList" v-bind:key="country.countryCode" v-on:click="select(country.countryCode)">
            <td>{{ country.countryCode }}</td>
            <td>{{ country.countryName }}</td>
            <td>{{ country.countryNameEn }}</td>
            <td>{{ country.countryNameShort }}</td>
            <td>{{ country.sortOrder }}</td>
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
import Navigator from '@/components/Navigator.vue';

export default {
  name: 'FxCountry',
  components: {
    Navigator,
  },
  data: function () {
    return {
      apiInfo: {
        apiCode: 'FX16010',
        url: this.getApiUrl('/fx/country/list'),
        messageCode: 'ZZ20010-I0',
      },

      isLoading: false,

      countryList: [
        {
          countryCode: '',
          countryName: '',
          countryNameEn: '',
          countryNameShort: '',
          sortOrder: 0,
        },
      ],

      apiResponse: {
        csrfToken: '',
        apiCode: '',
        returnCode: 0,
        recordCount: 0,
        messageCode: '',
        messageText: '',
      },
    };
  },
  mounted: function () {
    var vm = this;
    this.isLoading = true;
    this.axios
      .get(this.apiInfo.url + '?apiCode=' + this.apiInfo.apiCode, {
        headers: { 'X-ApiAuthCode': this.$store.state.authCode },
      })
      .then(function (response) {
        console.log(response);
        vm.apiResponse = response.data;
        if (vm.apiInfo.messageCode === response.data.messageCode) {
          vm.countryList = response.data.body.countryList;
        }
      })
      .catch(function (error) {
        console.log(error);
        vm.$router.push('/error');
      })
      .finally(function () {
        vm.isLoading = false;
      });
  },
  methods: {
    select: function (e) {
      this.$router.push('/fx/country/edit/' + e);
    },
  },
};
</script>
