#!/bin/bash

set -e

helm uninstall $(helm ls | cut -d' ' -f1 | grep -v 'NAME' | grep -v 'my-nginx' | sed 's/cyfhir//')
