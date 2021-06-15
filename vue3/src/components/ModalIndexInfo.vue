<template>
  <button v-if="list.length > 0" class="btn btn-dark btn-sm" @click="isModalShow = true">INDEX</button>
  <teleport to="#modalBox">
    <div v-if="isModalShow" class="modal">
      <div>
        <div class="row">
          <div class="col-sm-10 text-left">Index Information</div>
          <div class="col-sm-2 text-right">
            <button ref="btnClose" class="btn btn-dark btn-sm" @click="isModalShow = false">×</button>
          </div>
        </div>
        <div class="row"><hr/></div>
        <div class="row">
          <div style="align: left">
            <h3>{{ title }}</h3>
          </div>
        </div>
        <div class="row">
          <table class="table table-striped table-bordered">
            <thead>
              <tr>
                <th>Name</th>
                <th>No</th>
                <th>Column</th>
                <th>UK</th>
                <th>cardinality</th>
                <th>indexSchema</th>
                <th>tableSchema</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="indexInfo in list" :key="indexInfo.No">
                <td>{{ indexInfo.indexName }}</td>
                <td>{{ indexInfo.seqInIndex }}</td>
                <td>{{ indexInfo.columnName }}</td>
                <td>{{ indexInfo.nonUnique ? '' : 'UK' }}</td>
                <td>{{ indexInfo.cardinality }}</td>
                <td>{{ indexInfo.indexSchema }}</td>
                <td>{{ indexInfo.tableSchema }}</td>
              </tr>
            </tbody>
          </table>
        </div>
        <div class="row"><hr/></div>
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
  /* width: 90%;
  height: 80%; */
}
</style>

<script>
export default {
  name: 'ModalIndexInfo',
  props: {
    title: {
      type: String,
      default: '',
    },
    list: {
      type: Object,
      default: null,
    },
  },
  data: function () {
    return {
      isModalShow: false,
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
