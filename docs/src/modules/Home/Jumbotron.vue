<template>
<div class="jumbotron jumbotron-fluid" id="jumbotron-panel">
  <div class="container" id="jumbotron-img-container">
    <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 643.21 157.22">
      <path class="cls-1" d="M104.21,156.22c-.67-.64-101.62-76.88-102.59-77.66,3.7-2.79,102-77,102.59-77.56h32.6c-.65.64-101.5,76.7-102.68,77.6,1.1.85,102,77,102.68,77.62Z" />
      <path class="cls-1" d="M520.2,1q24.6,11.36,49.2,22.71c14.69,6.76,29.39,13.48,44.66,20.48L592.93,56.82l-53.88-26.4V34q0,59.46,0,118.92c0,1.11-.14,2.22-.21,3.33H519.68c0-.69-.1-1.37-.1-2.06V3.32c0-.77.07-1.55.1-2.32Z" />
      <path class="cls-1" d="M342.22,156.22c0-13.62-.08-27.23-.08-40.85q0-45.37,0-90.75V21.43L361.47,6.31V68.59H422.6V88.36H361.52V91.6q0,30.77,0,61.53c0,1-.1,2.06-.15,3.09Z" />
      <path class="cls-1" d="M246.5,156.22c0-.86-.11-130.91-.11-132.06h19.45v44.5h62.32V88.43H266.07c-.06,1.28-.35,66.68-.43,67.79Z" />
      <path class="cls-1" d="M609.71,156.22,547.44,97.15l-4.07-3.85,75.71-46.93,22,10L576.28,97.19l62.41,59Z" />
      <path class="cls-1" d="M360.32,1c-6.78,5.41-13.53,10.85-20.38,16.17-.69.53-2,.41-3,.41-29.23,0-89.74,0-91,0l-45.72,59L187.87,60.09Q209.73,30.48,231.49,1Z" />
      <path class="cls-1" d="M127.58,20.05l-2.49-3.36c5-3.82,9.9-7.54,15-11.4,7.35,9.69,57.28,76.06,57.28,77.21q.09,36.85,0,73.72H177.68c0-22.59.06-45.18,0-67.77Z" />
      <path class="cls-1" d="M493.3,1c0,9.38-.08,18.76-.08,28.15q0,61.86,0,123.72c0,1.11,0,2.23.08,3.35H473.64c0-15.24.07-30.49.07-45.73q0-45.73,0-91.46V15.54L492.78,1Z" />
      <path class="cls-1" d="M447.25,1q0,62,.05,124,0,15.63-.05,31.25H428.1c0-.68-.13-1.37-.13-2.05q0-68.38,0-136.77V15.54L446.73,1Z" />
      <path class="cls-1" d="M134.28,78.75a13,13,0,1,1-12.83-13.12A13,13,0,0,1,134.28,78.75Z" />
      <path class="cls-1" d="M315.53,43.77a13.06,13.06,0,0,1-12.94,13.06,13.23,13.23,0,0,1-13-13,13,13,0,0,1,13-13A12.87,12.87,0,0,1,315.53,43.77Z" />
      <path class="cls-1" d="M408.1,123.41A12.85,12.85,0,0,1,395,136.23a13,13,0,0,1-12.91-13.07,13.23,13.23,0,0,1,13.15-12.91A13.05,13.05,0,0,1,408.1,123.41Z" />
      <path class="cls-1" d="M630.54,99.5a13,13,0,0,1-12.89,13,13,13,0,1,1-.1-26A13,13,0,0,1,630.54,99.5Z" />
    </svg>
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

svg {
    filter: drop-shadow(-5px 5px 10px rgba(0,0,0,0.7));
    -webkit-filter: drop-shadow(-5px 10px 10px rgba(0,0,0,0.7));
    width: calc(90% - (20) * ((100vw - 300px) / (1600 - 300)));
    transition: transform 0.2s;
}

svg:hover {
    transform: scale(1.05);
}

.cls-1 {
    fill: #fff;
    stroke: #000;
    stroke-miterlimit: 10;
    stroke-width: 2px;
}

#cyfhir-logo {
    width: 75%;
    transition: transform 0.2s;
}

#jumbotron-panel {
    display: flex;
    align-items: center;
    justify-content: center;
    --heightA: 30vh;
    --heightB: calc(var(--heightA) + 20vw);
    --heightC: calc(10vh + var(--heightB));
    height: var(--heightC);
    background: url("~@/assets/background.svg") no-repeat center;
    background-size: cover;
    border-bottom: 2px solid grey;
    margin-bottom: 0;
}

#jumbo-logo {
    text-align: center;
    width: 45%;
    position: 50% 50%;
}

#jumbotron-img-container {
    text-align: center;
    margin: auto;
    display: block;
}
</style>
