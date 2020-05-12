<template>
<div class="home">
  <Jumbotron />
  <div class="container-fluid overlap">
    <div class="row center">
      <div v-for="(card, index) in cardData" v-bind:key="index" class="col-lg-4 col-md-12 col-sm-12 justify-content-center">
        <Card v-bind:card="card" />
      </div>
    </div>
  </div>
</div>
</template>

<script>
import Jumbotron from './Home/Jumbotron.vue';
import Card from './Home/Card.vue';
import cardData from './Home/data/cardData';

export default {
  name: 'Home',
  components: {
    Jumbotron,
    Card,
  },
  data() {
    return {
      test: 'TEST',
      cardData: null,
    };
  },
  created() {
    this.cardData = cardData;
  },
};
</script>

<style scoped lang="scss">
.home {
    height: 100vh;
    min-height: 100vh;
}
.overlap {
    transform: translateY(-10vh);
}
</style>
