<template>
  <div id="mainContent">
    <Navigator :loading="isLoading" />
    <div class="container">
      <div class="mb-3 row"><h3>LOGIN</h3></div>
      <hr />
      <div class="mb-3 row">
        <label for="emailAddress" class="col-sm-2 col-form-label text-nowrap">Emailアドレス</label>
        <div class="col-sm-6">
          <input
            type="email"
            ref="emailAddress"
            v-model="emailAddress"
            v-focus
            class="form-control"
            maxlength="128"/>
        </div>
        <div class="col-sm-4"></div>
      </div>
      <div class="mb-3 row">
        <label for="loginPassword" class="col-sm-2 col-form-label text-nowrap">パスワード</label>
        <div class="col-sm-4">
          <input
            type="password"
            ref="loginPassword"
            v-model="loginPassword"
            class="form-control"
            maxlength="128"/>
        </div>
        <div class="col-sm-6"></div>
      </div>
      <hr />
      <div class="mb-3 row">
        <div class="col-sm-2"></div>
        <div class="col-sm-2"><ExecuteButton type="login" @buttonClick="loginApi" /></div>
      </div>
      <div class="row">
        <div class="col-sm-2"></div>
        <div class="col-sm-8">
          <textarea cols="80" rows="5" v-model="resultText" class="form-control"></textarea>
        </div>
        <div class="col-sm-2"></div>
      </div>
    </div>
  </div>
</template>

<script>
import Navigator from '@/components/Navigator.vue'
import ExecuteButton from '@/components/ExecuteButton.vue'

export default {
  name: 'Login',
  components: {
    Navigator, ExecuteButton
  },
  data: function () {
    return {
      apiInfo: {
        apiCode: 'AA00010',
        url: this.getApiUrl('/login'),
        messageCode: 'AA00010-I0'
      },

      isLoading: false,
      csrfToken: '',

      emailAddress: 'test01@next-evolution.co.jp',
      loginPassword: 'testtest',

      resultText: ''
    }
  },
  mounted: async function () {
    this.csrfToken = await this.getCsrfToken(this)
  },
  methods: {
    loginApi: function () {
      if (!this.isMailAddress(this.emailAddress)) {
        alert('email address error.')
        this.$refs.emailAddress.focus()
        return
      }
      if (this.isEmpty(this.loginPassword)) {
        alert('password is empty.')
        this.$refs.loginPassword.focus()
        return
      }
      this.isLoading = true
      const vm = this
      this.axios
        .post(
          this.apiInfo.url,
          {
            apiCode: this.apiInfo.apiCode,
            emailAddress: this.emailAddress,
            loginPassword: this.loginPassword
          },
          { headers: { 'X-CSRF-TOKEN': this.csrfToken } }
        )
        .then(function (response) {
          console.log(response)
          if (vm.apiInfo.messageCode === response.data.messageCode) {
            vm.$store.commit('authCode', response.data.body.authCode)
            vm.$store.commit('loginUser', response.data.body.loginUser)
            if (vm.isEmpty(vm.$route.query.ref)) {
              vm.$router.push('/')
            } else {
              vm.$router.push({ path: vm.$route.query.ref })
            }
          } else {
            vm.resultText = JSON.stringify(response.data)
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
    }
  }
}
</script>
