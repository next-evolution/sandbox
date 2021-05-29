<template>
  <button
      type="submit"
      class="btn btn-primary form-control"
      v-on:click="buttonClick">{{getLabel}}</button>
</template>

<script>

export default {
  name: 'ExecuteButton',
  data: function () {
    return {
      variant: '',
      label: ''
    }
  },
  props: ['type'],
  computed: {
    getLabel: function () {
      switch (this.type) {
        case 'insert':
          return '登録'
        case 'update':
          return '更新'
        case 'login':
          return 'ログイン'
        case 'search':
          return '検索'
        case 'execute':
          return 'Execute'
        default:
          return '検索'
      }
    },
    getVariant: function () {
      // https://bootstrap-vue.org/docs/components/button
      switch (this.type) {
        case 'insert':
          return 'primary'
        case 'update':
          return 'success'
        case 'login':
          return 'primary'
        default:
          return 'info'
      }
    }
  },
  methods: {
    buttonClick: function () {
      this.$emit('buttonClick')
    }
  }
}
</script>
