<template>
<div class="home">
  <HelloWorld msg="Welcome to Your Vue.js App" />
  <Jumbotron />
</div>
</template>

<script>
import HelloWorld from './Home/HelloWorld.vue';
import Jumbotron from './Home/Jumbotron.vue';

export default {
  name: 'Home',
  components: {
    HelloWorld,
    Jumbotron,
  },
};
</script>
