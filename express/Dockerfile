# base image
FROM node:14.5.0-alpine
WORKDIR /app
USER root
ENV DEBIAN_FRONTEND noninteractive
RUN apk add --update python make g++\
   && rm -rf /var/cache/apk/*

COPY package.json package-lock.json ./
RUN npm ci

RUN apk update && apk add bash
RUN adduser -u 1001 -s /bin/bash -D node1 -G root

# add node modules .bin to path
ENV PATH /node_modules/.bin:$PATH
EXPOSE 3000

# compile typescript
COPY . .
RUN npm run build

# add app
COPY . .
USER 1001
CMD npm run serve
