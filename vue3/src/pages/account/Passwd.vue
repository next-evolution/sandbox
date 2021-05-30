<template>
  <div id="mainContent">
    <Navigator :loading="isLoading" />
    <div id="resultMessage">{{ resultMessage }}</div>
    <div v-show="!isInput">
      <router-link to="/">戻る</router-link>
    </div>
    <div class="container" v-show="isInput">
      <div class="row" align-v="center" align-h="start">
        <div class="col">
          ◆ パスワード変更フォーム ◆
          <!-- <div style="display: none; width: 1px; height: 1px; overflow: hidden;">
            <input type="text" id="dummyInput" name="dummyInput" />
            <input type="password" id="dummyPasswd" name="dummyPasswd" />
          </div> -->
        </div>
      </div>
      <hr />
      <div class="row" align-v="center" align-h="start">
        <div class="col-sm-2">
          <label for="loginPassword" class="control-label text-nowrap">パスワード</label>
        </div>
        <div class="col-sm-5">
          <input type="password" id="loginPassword" ref="loginPassword" v-model="loginPassword" class="form-control" maxlength="64" />
        </div>
        <div></div>
      </div>
      <hr />
      <div class="row" align-v="center" align-h="start">
        <div class="col-sm-2">
          <label for="loginPassword" class="control-label text-nowrap">新しいパスワード</label>
        </div>
        <div class="col-sm-5">
          <input type="password" id="loginPasswordNew" ref="loginPasswordNew" v-model="loginPasswordNew" class="form-control" maxlength="64" />
          <input type="password" id="checkPassword" ref="checkPassword" v-model="checkPassword" class="form-control" maxlength="64" />
        </div>
        <div class="col" style="text-align: left">
          <br />
          <br />(確認用)
        </div>
      </div>
      <hr />
      <div class="row" align-v="center" align-h="start">
        <div class="col-sm-2"></div>
        <div class="col-auto">
          <ExecuteButton type="update" @buttonClick="updateApi" />
        </div>
        <div></div>
      </div>
    </div>
  </div>
</template>

<script>
import Navigator from '@/components/Navigator.vue';
import ExecuteButton from '@/components/ExecuteButton.vue';

export default {
  name: 'AccountCreate',
  components: {
    Navigator,
    ExecuteButton,
  },
  data: function () {
    return {
      apiInfo: {
        apiCode: 'AA10040',
        url: this.getApiUrl('/account/password'),
        messageCode: 'AA10040-I0',
      },

      isLoading: false,
      isInput: true,
      resultMessage: '',

      csrfToken: '',

      authCode: '',
      accountId: '',
      loginPassword: '',
      loginPasswordNew: '',
      checkPassword: '',

      //   apiResponse: {
      //     apiCode: '',
      //     returnCode: 0,
      //     recordCount: 0,
      //     messageCode: '',
      //     messageText: ''
      //   }
    };
  },
  mounted: async function () {
    this.csrfToken = await this.getCsrfToken(this);
  },
  methods: {
    updateApi: function () {
      if (this.isEmpty(this.loginPassword)) {
        alert('loginPassword is empty.');
        this.$refs.loginPassword.focus();
        return;
      }
      if (this.isEmpty(this.loginPasswordNew)) {
        alert('loginPasswordNew is empty.');
        this.$refs.loginPasswordNew.focus();
        return;
      }
      if (this.loginPassword === this.loginPasswordNew) {
        alert('is the same password.');
        this.loginPasswordNew = '';
        this.$refs.loginPasswordNew.focus();
        return;
      }
      if (this.isEmpty(this.checkPassword)) {
        alert('checkPassword is empty.');
        this.$refs.checkPassword.focus();
        return;
      }
      if (this.loginPasswordNew !== this.checkPassword) {
        alert('difference password.');
        this.checkPassword = '';
        this.$refs.checkPassword.focus();
        return;
      }
      if (!confirm('パスワード変更を実行しますか？')) {
        return;
      }
      this.isLoading = true;
      var vm = this;
      this.axios
        .post(
          this.apiInfo.url,
          {
            apiCode: this.apiInfo.apiCode,
            loginPassword: this.loginPassword,
            loginPasswordNew: this.loginPasswordNew,
            accountId: this.$store.state.loginUser.accountId,
            authCode: this.$store.state.authCode,
          },
          {
            headers: {
              'X-CSRF-TOKEN': this.csrfToken,
              'X-ApiAuthCode': this.$store.state.authCode,
            },
          }
        )
        .then(function (response) {
          console.log(response);
          if (vm.apiInfo.messageCode === response.data.messageCode) {
            vm.isInput = false;
          }
          vm.resultMessage = response.data.messageText;
          alert(response.data.messageText);
        })
        .catch(function (error) {
          console.log(error);
          vm.$router.push('/error');
        })
        .finally(function () {
          vm.isLoading = false;
        });
    },
  },
};
</script>
