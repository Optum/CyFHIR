<template>
<div class="card topic-card">
  <div class="card-body">
    <img v-bind:src="require(`@/assets/${card.image}`)" class="card-icon" />
    <h2 class="card-title">
      {{ card.title }}
    </h2>
    <div class="container">
      <p class="card-text custom-text">{{ card.description }}</p>
    </div>
  </div>
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
    width: var(--cardB);
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
    width: 5vw;
    height: auto;
    margin-bottom: 2%;
}

.custom-text {
    font-size: 20px;
}
</style>
