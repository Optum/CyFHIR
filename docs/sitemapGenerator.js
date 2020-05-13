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
  path: '/concepts',
  name: 'Concepts',
});

router.options.routes.push({
  path: '/setup',
  name: 'Setup',
});

router.options.routes.push({
  path: '/guide',
  name: 'Guide',
});

function generateSitemap(_router) {

  return new Sitemap(_router)
    .build('https://optum.github.io/CyFHIR')
    .save('./dist/sitemap.xml');
}

generateSitemap(router);
