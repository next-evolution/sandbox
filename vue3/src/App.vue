<template>
  <div id="app">
    <router-view />
    <footer id="footer">Copyright &copy; 2021 NextEvolution Co.Ltd</footer>
  </div>
</template>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  /* margin-top: 10px;
  margin-bottom: 10px; */
  min-height: 100vh;
  /*←相対位置*/
  position: relative;
  /*←footerの高さ*/
  padding-bottom: 50px;
  /*←全て含めてmin-height:100vhに*/
  box-sizing: border-box;
}

footer {
  font-size: small;
  color: #ffffff;
  background-color: #9999ff;
  height: 30px;
  margin-top: -30px;
  width: 100%;
  padding: 10px 0;
  /*←絶対位置*/
  position: absolute;
  /*下に固定*/
  bottom: 0;
}
</style>
