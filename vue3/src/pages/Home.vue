<template>
  <div id="mainContainer">
    <Navigator />
    <div v-show="!login">welcome Vue.js</div>
    <table class="table container" v-show="login">
      <tbody>
      <tr>
        <td>◆ 経済指標 ◆</td>
        <td colspan="2"></td>
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
      <tr>
        <td>◆ 足データ FX ◆</td>
        <td colspan="2"></td>
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

      <tr>
        <td>◆ マスタ FX ◆</td>
        <td colspan="2"></td>
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
        <td>
          市場マスタ
        </td>
        <td>
          <router-link to="/fx/summerTime">夏時間設定</router-link>
        </td>
        <td></td>
      </tr>
      </tbody>
    </table>
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
      login: this.$store.state.authCode != null
    }
  },
}
</script>
