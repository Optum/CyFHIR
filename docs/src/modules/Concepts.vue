<template>
<div class="home">
  <h1>Concepts</h1>
  <router-link v-bind:to="{ path: '/'}" replace>Home</router-link>
</div>
</template>

<script>
export default {
  name: 'Concepts',
  components: {},
  data() {
    return {};
  },
};
</script>

<style scoped lang="scss">
.home {
    height: 100vh;
    min-height: 100vh;
}
</style>
