<template>
  <teleport to="#modalResultCode">
    <div v-if="isModalShow" class="modalResultCode">
      <div>
        <textarea ref="resultCodeText" v-model="resultCodeText" @click="$refs.resultCodeText.select()"></textarea>
      </div>
    </div>
  </teleport>
</template>

<style>
.modalResultCode {
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

.modalResultCode div {
  /* display: flex;
  flex-direction: column;
  justify-content: top; */
  /* display: flex; */
  align-items: center;
  justify-content: center;
  background-color: #fcfcfc;
  width: 60%;
  height: 80%;
}

.modalResultCode div textarea {
  font-family: 'ＭＳ ゴシック', 'Monaco';
  resize: none;
  width: 100%;
  height: 100%;
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
      resultCodeText: this.resultCode,
    }
  },
  mounted: function () {
    document.onkeydown = (evt) => {
      evt = evt || window.event
      if (evt.keyCode == 27) {
        this.isModalShow = false
      }
    }
  },
}
</script>
