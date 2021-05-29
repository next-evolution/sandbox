<template>
  <div id="mainContent">
    <Navigator :loading="isLoading" />
    <div class="container">
    <div id="resultMessage">{{resultMessage}}</div>
    <div v-show="!isInput">
      <router-link to="/">戻る</router-link>
    </div>
    <div v-show="isInput">
      <div class="row" align-v="center" align-h="start">
        <div>
          ユーザ新規登録フォーム
          <div style="width: 1px; height: 1px; overflow: hidden;">
            <input type="text" id="dummyInput" name="dummyInput" />
            <input type="password" id="dummyPasswd" name="dummyPasswd" />
          </div>
        </div>
      </div>
      <hr />
      <div class="row" align-v="center" align-h="start">
        <div class="col-sm-2">
          <label for="emailAddress" class="control-label text-nowrap">Email</label>
        </div>
        <div class="col-sm-8">
          <input
            type="email"
            class="form-control"
            id="emailAddress"
            ref="emailAddress"
            v-model="emailAddress"
            v-focus
            maxlength="128"/>
        </div>
        <div class="col-sm-2"></div>
      </div>
      <hr />
      <div class="row" align-v="center" align-h="start">
        <div class="col-sm-2">
          <label for="userName" class="control-label text-nowrap">氏名</label>
        </div>
        <div class="col-sm-3">
          <input
            type="text"
            class="form-control"
            id="familyName"
            ref="familyName"
            v-model="familyName"
            maxlength="64"/>
        </div>
        <div class="col-sm-3">
          <input
            type="text"
            class="form-control"
            id="firstName"
            ref="firstName"
            v-model="firstName"
            maxlength="64"/>
        </div>
        <div class="col-sm-4" style="text-align: left;">例）在日 悪子</div>
      </div>
      <hr />
      <div class="row" align-v="center" align-h="start">
        <div class="col-sm-2">
          <label for="userNameEn" class="control-label text-nowrap">英字</label>
        </div>
        <div class="col-sm-3">
          <input
            type="text"
            class="form-control"
            id="familyNameEn"
            ref="familyNameEn"
            v-model="familyNameEn"
            maxlength="64"/>
        </div>
        <div class="col-sm-3">
          <input
            type="text"
            class="form-control"
            id="firstNameEn"
            ref="firstNameEn"
            v-model="firstNameEn"
            maxlength="64"/>
        </div>
        <div class="col-sm-4" style="text-align: left;">例）zainichi waruko</div>
      </div>
      <hr />
      <div class="row" align-v="center" align-h="start">
        <div class="col-sm-2">
          <label for="sexFlag" class="control-label text-nowrap">性別</label>
        </div>
        <div class="col-sm-2">
          <input type="radio" v-model="sexFlag" name="sexFlag" ref="sexFlag" value="1" />男性
        </div>
        <div class="col-sm-2">
          <input type="radio" v-model="sexFlag" name="sexFlag" value="2" />女性
        </div>
        <div class="col-sm-6"></div>
      </div>
      <hr />
      <div class="row" align-v="center" align-h="start">
        <div class="col-sm-2">
          <label for="birthday" class="control-label text-nowrap">生年月日</label>
        </div>
        <div class="col-sm-3">
          <input type="date" class="form-control" id="birthday" ref="birthday" v-model="birthdayInput" />
        </div>
        <div class="col-sm-7"></div>
      </div>
      <hr />
      <div class="row" align-v="center" align-h="start">
        <div class="col-sm-2">
          <label for="loginPassword" class="control-label text-nowrap">パスワード</label>
        </div>
        <div class="col-sm-6">
          <input
            type="password"
            class="form-control"
            id="loginPassword"
            ref="loginPassword"
            v-model="loginPassword"
            maxlength="64"/>
          <input
            type="password"
            class="form-control"
            id="checkPassword"
            ref="checkPassword"
            v-model="checkPassword"
            maxlength="64"/>
        </div>
        <div class="col-sm-4" style="text-align: left;">
          <br />
          <br />(確認用)
        </div>
      </div>
      <hr />
      <div class="row" align-v="center" align-h="start">
        <div class="col-sm-2"></div>
        <div class="col-sm-2">
          <ExecuteButton type="insert" @buttonClick="insertApi" />
        </div>
      </div>
    </div>
    </div>
  </div>
</template>

<script>
import Navigator from '@/components/Navigator.vue'
import ExecuteButton from '@/components/ExecuteButton.vue'

export default {
  name: 'AccountCreate',
  components: {
    Navigator,
    ExecuteButton
  },
  data: function () {
    return {
      apiInfo: {
        apiCode: 'AA10010',
        url: this.getApiUrl('/account/registration'),
        messageCode: 'AA10010-I0'
      },

      isLoading: false,
      isInput: true,
      resultMessage: '',

      birthdayInput: '',

      csrfToken: '',
      emailAddress: '',
      familyName: '',
      firstName: '',
      familyNameEn: '',
      firstNameEn: '',
      sexFlag: '',
      birthday: '',
      loginPassword: '',
      checkPassword: '',

      apiResponse: {
        apiCode: '',
        returnCode: 0,
        recordCount: 0,
        messageCode: '',
        messageText: ''
      }
    }
  },
  mounted: async function () {
    this.birthdayInput = this.dateFormat(new Date(1985, 0, 1), 'yyyy-MM-dd')
    this.csrfToken = await this.getCsrfToken(this)
  },
  methods: {
    insertApi: function () {
      if (!this.isMailAddress(this.emailAddress)) {
        alert('email address error.')
        this.$refs.emailAddress.focus()
        return
      }
      if (this.isEmpty(this.familyName)) {
        alert('familyName is empty.')
        this.$refs.familyName.focus()
        return
      }
      if (this.isEmpty(this.firstName)) {
        alert('firstName is empty.')
        this.$refs.firstName.focus()
        return
      }
      if (this.isEmpty(this.familyNameEn)) {
        alert('familyNameEn is empty.')
        this.$refs.familyNameEn.focus()
        return
      }
      if (this.isEmpty(this.firstNameEn)) {
        alert('firstNameEn is empty.')
        this.$refs.firstNameEn.focus()
        return
      }
      if (this.isEmpty(this.sexFlag)) {
        alert('sex flag not selected.')
        this.$refs.sexFlag.focus()
        return
      }
      if (!this.isDate(this.birthdayInput)) {
        alert('birthday error.')
        this.$refs.birthday.focus()
        return
      }
      this.birthday = this.birthdayInput.replace(/-/g, '')

      if (this.isEmpty(this.loginPassword)) {
        alert('loginPassword is empty.')
        this.$refs.loginPassword.focus()
        return
      }
      if (this.isEmpty(this.checkPassword)) {
        alert('checkPassword is empty.')
        this.$refs.checkPassword.focus()
        return
      }
      if (this.loginPassword !== this.checkPassword) {
        alert('difference password.')
        this.checkPassword = ''
        this.$refs.checkPassword.focus()
        return
      }
      if (!confirm('登録を実行しますか？')) {
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
            familyName: this.familyName,
            firstName: this.firstName,
            familyNameEn: this.familyNameEn,
            firstNameEn: this.firstNameEn,
            sexFlag: this.sexFlag,
            birthday: this.birthday,
            loginPassword: this.loginPassword
          },
          {
            headers: {
              'X-CSRF-TOKEN': this.csrfToken,
              'X-ApiAuthCode': this.$store.state.authCode
            }
          }
        )
        .then(function (response) {
          console.log(response)
          if (vm.apiInfo.messageCode === response.data.messageCode) {
            vm.isInput = false
            vm.$router.push('/account/activation')
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
    }
  }
}
</script>
