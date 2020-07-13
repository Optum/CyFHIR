build:
	bash bin/maven-build.sh

server:
	(cd express && open http://localhost:3000/docs && npm start)

neo4j:
	(bash bin/maven-build.sh) && docker-compose -f docker-compose/docker-compose.neo4j.yaml up --build

stack:
	(bash bin/maven-build.sh) && docker-compose -f docker-compose/docker-compose.yaml up --build

backend:
	(bash bin/maven-build.sh) && docker-compose -f docker-compose/docker-compose.backend.yaml up --build

halt:
	docker-compose -f docker-compose/docker-compose.yaml down
