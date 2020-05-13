<template>
<div class="card topic-card">
  <router-link v-bind:to="{ path: card.link}" tag="div">
    <div class="card-body">
      <div class="container">
        <img v-bind:src="require(`@/assets/${card.image}`)" v-bind:alt="card.alt" class="card-icon" />
      </div>
      <h2 class="card-title">
        {{ card.title }}
      </h2>
      <div class="container">
        <p class="card-text custom-text">{{ card.description }}</p>
      </div>
    </div>
  </router-link>
</div>
</template>

<script>
export default {
  name: 'Card',
  components: {},
  props: {
    card: Object,
  },
  methods: {},
};
</script>

<style lang="scss">
.topic-card {
    --cardA: calc(5vw);
    --cardB: calc(65% + var(--cardA));
    width: calc(90% - (5vw + 10px));
    margin: 0 auto 5%;
    box-shadow: -5px 5px 10px rgba(0,0,0,0.5);
    min-height: 30vh;
    transition: transform 0.2s;
    cursor: pointer;
}

.topic-card:hover {
    transform: scale(1.05);
}

.card-icon {
    width: calc(30% - (5vw / 100));
    height: auto;
    margin-bottom: 2%;
}

.custom-text {
    font-size: 20px;
}
</style>
