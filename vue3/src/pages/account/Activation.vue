<template>
  <div id="mainContent">
    <Navigator :loading="isLoading" />
    <div id="resultMessage">{{ resultMessage }}</div>
    <div v-show="!isInput">
      <router-link to="/">戻る</router-link>
    </div>
    <div class="container" v-show="isInput">
      <div class="row" align-v="center" align-h="start">
        <div class="col">ユーザ認証フォーム</div>
      </div>
      <hr />
      <div class="row" align-v="center" align-h="start">
        <div class="col-sm-2">
          <label for="emailAddress" class="control-label text-nowrap">Email</label>
        </div>
        <div class="col-sm-6">
          <input type="email" id="emailAddress" ref="emailAddress" v-model="emailAddress" v-focus class="form-control" maxlength="128" />
        </div>
      </div>
      <hr />
      <div class="row" align-v="center" align-h="start">
        <div class="col-sm-2">
          <label for="activationCode" class="control-label text-nowrap">認証コード</label>
        </div>
        <div class="col-sm-3">
          <input type="tel" id="activationCode" ref="activationCode" v-model="activationCode" class="form-control" maxlength="64" />
        </div>
      </div>
      <hr />
      <div class="row" align-v="center" align-h="start">
        <div class="col-sm-2"></div>
        <div class="col-auto">
          <ExecuteButton type="insert" @buttonClick="insertApi" />
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import Navigator from '@/components/Navigator.vue'
import ExecuteButton from '@/components/ExecuteButton.vue'

export default {
  name: 'AccountActivation',
  components: {
    Navigator,
    ExecuteButton,
  },
  data: function () {
    return {
      apiInfo: {
        apiCode: 'AA10020',
        url: this.getApiUrl('/account/activation'),
        messageCode: 'AA10020-I0',
      },

      isLoading: false,
      isInput: true,
      resultMessage: '',

      csrfToken: '',

      emailAddress: '',
      activationCode: '',

      apiResponse: {
        apiCode: '',
        returnCode: 0,
        recordCount: 0,
        messageCode: '',
        messageText: '',
      },
    }
  },
  mounted: async function () {
    this.csrfToken = await this.getCsrfToken(this)
  },
  methods: {
    insertApi: function () {
      if (!this.isMailAddress(this.emailAddress)) {
        alert('email address error.')
        this.$refs.emailAddress.focus()
        return
      }
      if (this.isEmpty(this.activationCode)) {
        alert('activationCode is empty.')
        this.$refs.activationCode.focus()
        return
      }
      if (!confirm('ユーザ認証を実行しますか？')) {
        return
      }
      this.isLoading = true
      var vm = this
      this.axios
        .post(
          this.apiInfo.url,
          {
            apiCode: this.apiInfo.apiCode,
            emailAddress: this.emailAddress,
            activationCode: this.activationCode,
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
          if (vm.apiInfo.messageCode === response.data.messageCode) {
            vm.isInput = false
            vm.$router.push('/login')
          }
          vm.resultMessage = response.data.messageText
          alert(response.data.messageText)
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
