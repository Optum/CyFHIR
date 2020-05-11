<template>
<div class="home">
  <Jumbotron />
  <Card />
</div>
</template>

<script>
import Jumbotron from './Home/Jumbotron.vue';
import Card from './Home/Card.vue';


export default {
  name: 'Home',
  components: {
    Jumbotron,
    Card,
  },
};
</script>

<style scoped lang="scss">
.home {
    min-height: 100vh;
}
</style>
