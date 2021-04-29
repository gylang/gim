<template>

  <RouterView/>

</template>
<script>


import Home from "@/views/Home";
import ChatIndex from "@/views/ChatIndex";
import Test from "@/views/Test";

export default {
  name: 'App',

  components: {Test, ChatIndex, Home},
  data() {
    return {
      active: 0
    }
  }
}

</script>
<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}

#nav {
  padding: 30px;
}

#nav a {
  font-weight: bold;
  color: #2c3e50;
}

#nav a.router-link-exact-active {
  color: #42b983;
}
</style>
