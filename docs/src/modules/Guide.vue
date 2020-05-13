<template>
<div class="home">
  <h1>Guide</h1>
  <router-link v-bind:to="{ path: '/'}" replace>Home</router-link>
</div>
</template>

<script>

export default {
  name: 'Guide',
  components: {
  },
  data() {
    return {
    };
  },
};
</script>

<style scoped lang="scss">
.home {
    height: 100vh;
    min-height: 100vh;
}
</style>
