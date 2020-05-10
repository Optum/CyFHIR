const Sitemap = require('vue-router-sitemap').default;

const router = {
  options: {
    routes: [],
  },
};

router.options.routes.push({
  path: '/',
  name: 'Home',
});

router.options.routes.push({
  path: '/about',
  name: 'About',
});

function generateSitemap(_router) {
  _router.options.routes = _router.options.routes.map((route) => {
    if (route.path.length > 1) {
      route.path = `/#${route.path}`;
    }
    return route;
  });

  return new Sitemap(_router)
    .build('https://optum.github.io/CyFHIR')
    .save('./dist/sitemap.xml');
}

generateSitemap(router);
