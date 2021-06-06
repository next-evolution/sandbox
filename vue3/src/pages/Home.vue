<template>
  <div id="mainContainer">
    <Navigator />
    <div class="container">
      <div v-show="!login">welcome Vue.js</div>
      <table class="table table-bordered" v-show="login">
        <tbody>
          <tr class="table-dark">
            <td colspan="3">◆ 経済指標 ◆</td>
          </tr>
          <tr>
            <td>
              <router-link to="/fx/economicIndicatorData">経済指標</router-link>
            </td>
            <td>
              <router-link to="/fx/economicIndicator">経済指標マスタ</router-link>
            </td>
            <td></td>
          </tr>
          <tr class="table-dark">
            <td colspan="3">◆ 足データ FX ◆</td>
          </tr>

          <tr>
            <td>
              <router-link to="/fx/bardata/search/1d">検索(日足)</router-link>
            </td>
            <td>
              <!-- <router-link to="/fx/bardata/search/4h">検索(4時間足)</router-link> -->
              <s>検索(4時間足)</s>
            </td>
            <td>
              <router-link to="/fx/bardata/search/1h">検索(時間足)</router-link>
            </td>
          </tr>
          <tr>
            <td>
              <router-link to="/fx/bardata/search/30m">検索(30分足)</router-link>
            </td>
            <td>
              <router-link to="/fx/bardata/upload">取込み(30分足)</router-link>
            </td>
            <td>
              <router-link to="/fx/bardata/generate">足データ作成</router-link>
            </td>
          </tr>

          <tr class="table-dark">
            <td colspan="3">◆ マスタ FX ◆</td>
          </tr>
          <tr>
            <td>
              <router-link to="/fx/currencyPair">通貨ペアマスタ</router-link>
            </td>
            <td>
              <router-link to="/fx/country">FX国マスタ</router-link>
            </td>
            <td></td>
          </tr>
          <tr>
            <td>市場マスタ</td>
            <td>
              <router-link to="/fx/summerTime">夏時間設定</router-link>
            </td>
            <td></td>
          </tr>
          <tr class="table-dark">
            <td colspan="3">◆ Generator ◆</td>
          </tr>
          <tr>
            <td>
              <router-link to="/generator">Generator</router-link>
            </td>
            <td></td>
            <td></td>
          </tr>
          <tr>
            <td><router-link to="/generator/import/db">DB定義</router-link></td>
            <td><router-link to="/generator/import/api">API一覧</router-link></td>
            <td><router-link to="/generator/import/interface">IF一覧</router-link></td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<style>
td {
  text-align: left;
}
</style>

<script>
import Navigator from '@/components/Navigator.vue'

export default {
  name: 'Home',
  components: { Navigator },
  data: function () {
    return {
      login: this.$store.state.authCode != null,
    }
  },
}
</script>
