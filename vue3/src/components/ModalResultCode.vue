<template>
  <teleport to="#modalBox">
    <div v-if="isModalShow" class="modal">
      <div>
        <textarea cols="80" rows="15" ref="resultCode" v-model="resultCodeText" style="font-family: 'ＭＳ ゴシック', 'Monaco'; width: 100%; height: 500px"></textarea>
      </div>
    </div>
  </teleport>
</template>

<style>
.modal {
  position: absolute;
  top: 0;
  right: 0;
  bottom: 0;
  left: 0;
  background-color: rgba(0, 0, 0, 0.7);
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 100%;
  height: 100%;
  /* justify-content: center; */
}

.modal div {
  /* display: flex;
  flex-direction: column;
  justify-content: top; */
  /* display: flex; */
  align-items: center;
  justify-content: center;
  background-color: #fcfcfc;
  width: 90%;
  height: 80%;
}
</style>

<script>
export default {
  name: 'ModalResultCode',
  props: {
    resultCode: {
      type: String,
      default: '',
    },
  },
  data: function () {
    return {
      isModalShow: true,
      resultCodeText: this.resultCode
    }
  },
  mounted: function () {
    this.$refs.resultCode.select()
    document.onkeydown = (evt) => {
      evt = evt || window.event
      if (evt.keyCode == 27) {
        this.isModalShow = false
      }
    }
  },
}
</script>
