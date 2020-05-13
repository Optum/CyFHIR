<template>
<div id="nav-manu" class="navbar navbar-default " role="navigation">
  <div class="container-fluid">
    <div class="navbar-header"><a class="navbar-brand" href="/">CyFHIR</a>
      <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-menubuilder">
        <span class="sr-only">Toggle navigation</span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
      </button>
    </div>
    <div class="collapse navbar-collapse navbar-menubuilder">
      <ul class="nav navbar-nav navbar-right">
        <li><a href="/concepts">Concepts</a>
        </li>
        <li><a href="setup">Setup</a>
        </li>
        <li><a href="guides">Guides</a>
        </li>
      </ul>
    </div>
  </div>
</div>
</template>

<script>
export default {
  name: 'Header',
  components: {},
  data() {
    return {};
  },
};
</script>

<style scoped lang="scss">
#nav-manu.navbar-default .navbar-brand {
    height: 48px;
    color: rgba(119, 119, 119, 1);
}
#nav-manu.navbar-default {
    font-size: 14px;
    background-color: rgba(248, 248, 248, 1);
    border-width: 1px;
    border-radius: 0;
}
#nav-manu.navbar-default .navbar-nav > li > a {
    color: rgba(119, 119, 119, 1);
    background-color: rgba(248, 248, 248, 0);
}
#nav-manu.navbar-default .navbar-nav > li > a:focus,
#nav-manu.navbar-default .navbar-nav > li > a:hover {
    color: rgba(51, 51, 51, 1);
    background-color: rgba(248, 248, 248, 0);
}
#nav-manu.navbar-default .navbar-nav > .active > a,
#nav-manu.navbar-default .navbar-nav > .active > a:focus,
#nav-manu.navbar-default .navbar-nav > .active > a:hover {
    color: rgba(85, 85, 85, 1);
    background-color: rgba(231, 231, 231, 1);
}
#nav-manu.navbar-default .navbar-toggle {
    border-color: #ddd;
}
#nav-manu.navbar-default .navbar-toggle:focus,
#nav-manu.navbar-default .navbar-toggle:hover {
    background-color: #ddd;
}
#nav-manu.navbar-default .navbar-toggle .icon-bar {
    background-color: #888;
}
#nav-manu.navbar-default .navbar-toggle:focus .icon-bar,
#nav-manu.navbar-default .navbar-toggle:hover .icon-bar {
    background-color: #888;
}
</style>
