<template>
  <nav class="navbar navbar-expand-lg navbar-dark bg-primary">
    <div class="container-fluid">
      <router-link class="navbar-brand" to="/">Core Vue</router-link>
      <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
      </button>
      <div class="collapse navbar-collapse" id="navbarSupportedContent">
        <ul v-show="login" class="navbar-nav me-auto mb-2 mb-lg-0">
          <li class="nav-item dropdown">
            <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button" data-bs-toggle="dropdown" aria-expanded="false">FX</a>
            <ul class="dropdown-menu" aria-labelledby="navbarDropdown">
              <li><router-link class="dropdown-item" to="/fx/economicIndicatorData">経済指標データ</router-link></li>
              <li><hr class="dropdown-divider"></li>
              <li><router-link class="dropdown-item" to="/fx/bardata/upload">30分足Upload</router-link></li>
              <li><router-link class="dropdown-item" to="/fx/bardata/generate">足データ作成</router-link></li>
            </ul>
          </li>

          <li class="nav-item dropdown">
            <a class="nav-link dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown" aria-expanded="false">FX Bardata</a>
            <ul class="dropdown-menu" aria-labelledby="navbarDropdown">
              <li><router-link class="dropdown-item" to="/fx/bardata/search/1d">日足</router-link></li>
              <li><router-link class="dropdown-item" to="/fx/bardata/search/1h">1時間足</router-link></li>
              <li><router-link class="dropdown-item" to="/fx/bardata/search/30m">30分足</router-link></li>
            </ul>
          </li>

          <li class="nav-item dropdown">
            <a class="nav-link dropdown-toggle" href="#" data-bs-toggle="dropdown">FX master</a>
            <ul class="dropdown-menu" aria-labelledby="navbarDropdown">
              <li><router-link class="dropdown-item" to="/fx/economicIndicator">経済指標</router-link></li>
              <li><router-link class="dropdown-item" to="/fx/currencyPair">通貨ペア</router-link></li>
              <li><router-link class="dropdown-item" to="/fx/country">国</router-link></li>
              <li><router-link class="dropdown-item" to="/fx/summerTime">夏時間</router-link></li>
            </ul>
          </li>

          <li class="nav-item">
            <router-link class="nav-link" to="/generator">Generator</router-link>
          </li>

          <li class="nav-item">
            <router-link class="nav-link" to="/account/passwd">パスワード変更</router-link>
          </li>

          <li class="nav-item">
            <router-link class="nav-link" to="/logout">LOGOUT</router-link>
          </li>
        </ul>
        <span v-show="login" class="navbar-text">User:{{getUserName()}}</span>

        <ul v-show="!login" class="navbar-nav me-auto mb-2 mb-lg-0">
          <li class="nav-item">
            <router-link class="nav-link" to="/login">ログイン</router-link>
          </li>
          <li class="nav-item">
            <router-link class="nav-link" to="/account/create">ユーザ登録</router-link>
          </li>
          <li class="nav-item">
            <router-link class="nav-link" to="/account/activation">Email認証</router-link>
          </li>
        </ul>
      </div>
      <Loading :active="loading" :can-cancel="true" :is-full-page="true"></Loading>
    </div>
  </nav>
  <div>
    <div v-if="hasBackPageUrl()" style="text-align: left;">
      <router-link :to="{path: backPage}"><button class="btn btn-dark btn-sm">BACK</button></router-link>
    </div>
    <div v-else>
      <br />
    </div>
  </div>
</template>

<script>
import Loading from 'vue-loading-overlay'

export default {
  name: 'Navigator',
  components: { Loading },
  data: function () {
    return {
      login: this.$store.state.authCode != null
    }
  },
  props: { 
    backPage: {
      type: String,
      default: '/'
    },
    loading: {
      type: Boolean,
      default: false
    } 
  },
  methods: {
    getUserName: function () {
      if (this.login) {
        return (
          this.$store.state.loginUser.familyName +
          ' ' +
          this.$store.state.loginUser.firstName
        )
      } else {
        return ''
      }
    },
    hasBackPageUrl : function(){
      if(this.$route.path === '/' || this.$route.path === '/login' || this.$route.path === '/logout'){
          return false;
      }
      if( this.isEmpty(this.backPage) ){
          this.$props.backPage = "/"
      }
      return true;
    }
  }
}
</script>
