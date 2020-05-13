#!/bin/bash

set -e

(
vue-cli-service build
node sitemapGenerator.js
cat 404.html > dist/404.html
gh-pages -d dist
)
