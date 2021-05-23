<template>
    <div id="executeButton">
      <Loading
        :active="loading"
        :can-cancel="true"
        :is-full-page="true"></Loading>
      <button
          type="submit"
          v-show="!loading"
          class="btn btn-primary"
          v-on:click="buttonClick">{{getLabel}}</button>
    </div>
</template>

<script>
import Loading from 'vue-loading-overlay'

export default {
  name: 'ExecuteButton',
  components: {
    Loading
  },
  data: function () {
    return {
      variant: '',
      label: ''
    }
  },
  props: ['type', 'loading'],
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
