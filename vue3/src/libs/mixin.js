export default {
  // directives: {
  //   focus: {
  //     // directive definition
  //     inserted: function (el) {
  //       el.focus()
  //     }
  //   }
  // },
  methods: {
    getApiUrl: function (path) {
      // vue.config.js#devServer:proxyの値と同一にする
      return '/core/api' + path
    },
    getCsrfToken: async function (component) {
      var resultToken = ''
      component.isLoading = true
      await this.axios
        .get(this.getApiUrl('/token/' + component.apiInfo.apiCode))
        .then(function (response) {
          console.log('apiCode=' + component.apiCode + '/csrfToken=' + response.data)
          resultToken = response.data
        })
        .catch(function (error) {
          console.log(error)
          resultToken = ''
        })
        .finally(function () {
          component.isLoading = false
        })
      return resultToken
    },
    isEmpty: function (value) {
      var bEmpty = true
      if (value != null && value.trim().length > 0) {
        bEmpty = false
      }
      return bEmpty
    },
    isMailAddress: function (value) {
      const patternEmail = /^([a-zA-Z0-9])+([a-zA-Z0-9._-])*@([a-zA-Z0-9_-])+([a-zA-Z0-9._-]*)([.]+)+([a-zA-Z0-9._-]+)+$/
      if (this.isEmpty(value)) {
        return false
      } else {
        return patternEmail.test(value)
      }
    },
    isYear: function (sYear) {
      if (!/^\d{4}$/.test(sYear)) {
        return false
      }
      var dt = new Date(Number(sYear), 1, 1)
      return dt.getFullYear() === Number(sYear)
    },
    isDate: function (yyyyMMdd) {
      var checkValue = yyyyMMdd.replace(/\//g, '').replace(/-/g, '')
      if (!/^\d{8}$/.test(checkValue)) {
        return false
      }
      var yyyy = Number(checkValue.substr(0, 4))
      var mm = Number(checkValue.substr(4, 2)) - 1 // Javascriptは、0-11で表現
      var dd = Number(checkValue.substr(6, 2))
      var dt = new Date(yyyy, mm, dd)
      return dt.getFullYear() === yyyy && dt.getMonth() === mm && dt.getDate() === dd
    },
    isTime: function (sTime) {
      if (!/^\d{2}:\d{2}:\d{2}$/.test(sTime)) {
        return false
      }
      var hh = Number(sTime.substr(0, 2))
      var mm = Number(sTime.substr(3, 2))
      var ss = Number(sTime.substr(6, 2))
      var dt = new Date(2020, 1, 1, hh, mm, ss)
      return dt.getHours() === hh && dt.getMinutes() === mm && dt.getSeconds() === ss
    },
    dateFormat: function (date, format) {
      var result = ''
      result = format
        .replace(/yyyy/g, date.getFullYear())
        .replace(/MM/g, ('0' + (date.getMonth() + 1)).slice(-2))
        .replace(/dd/g, ('0' + date.getDate()).slice(-2))
        .replace(/HH/g, ('0' + date.getHours()).slice(-2))
        .replace(/mm/g, ('0' + date.getMinutes()).slice(-2))
        .replace(/ss/g, ('0' + date.getSeconds()).slice(-2))
      return result
    },
    isCurrencyPair: function (value) {
      return /^[A-Z][A-Z0-9]+$/.test(value)
    },
    isNumber: function (value) {
      return /^[0-9]+$/.test(value)
    },
    isPrice: function (value) {
      return /^[0-9].[0-9]+$/.test(value)
    }
  }
}
