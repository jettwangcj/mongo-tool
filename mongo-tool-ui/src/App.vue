<template>
  <div id="app">
    <div id="nav">
      <router-link to="/">Mongo连接</router-link>
      <router-link to="/editor">SQL编辑器</router-link>
      <router-link to="/commits">提交记录</router-link>
      <router-link to="/setting">设置</router-link>

    </div>
    <router-view/>
  </div>
</template>

<style lang="scss">
html,body {
  padding: 0;
  margin: 0;
  height: 100%;
}
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  // text-align: center;
  color: #2c3e50;
  height: 100%;
}

#nav {
  height: 50px;
  line-height: 50px;
  padding-left: 30px;
  background-color: #545c64;
  a {
    color: #fff;
    text-decoration: none;
    margin: 0px 15px;
    font-size: 14px;

    &.router-link-exact-active {
      color: #ffd04b;
    }
  }
}
</style>
