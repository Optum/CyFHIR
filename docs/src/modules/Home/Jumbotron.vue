<template>
<div class="jumbotron" id="jumbotron-panel">
  <div class="container">
    <div id="jumbotron-img-container">
      <img src="@/assets/logo.svg" class="img-responsive" alt="cyfhir-logo" />
    </div>
  </div>
</div>
</template>

<script>
export default {
  name: 'Jumbotron',
  components: {},
  props: {},
};
</script>

<style scoped lang="scss">
h1 {
    margin: 40px 0 0;
}

#jumbotron-panel {
    --heightA: 15vh;
    --heightB: calc(7vw - var(--heightA));
    --heightC: calc(70vh + var(--heightB));
    height: var(--heightC);
    background: url("~@/assets/background.svg") no-repeat center;
    background-size: cover;
    border-bottom: 2px solid grey;
}

#jumbo-logo {
    width: 45%;
}

#jumbotron-img-container {
    margin-top: 10%;
    margin-bottom: 5%;
}
</style>
