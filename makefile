build:
	bash bin/build-cyfhir.sh

test:
	bash bin/build-cyfhir.sh && bash bin/get-apoc.sh && (cd cyfhir && mvn clean test)

server:
	(cd express && open http://localhost:3000/docs && npm start)

neo4j:
	(bash bin/build-cyfhir.sh) && docker-compose -f docker-compose/docker-compose.neo4j.yaml up --build

stack:
	(bash bin/build-cyfhir.sh) && docker-compose -f docker-compose/docker-compose.yaml up --build

backend:
	(bash bin/build-cyfhir.sh) && docker-compose -f docker-compose/docker-compose.backend.yaml up --build

halt:
	docker-compose -f docker-compose/docker-compose.yaml down

clean:
	docker system prune -a -f && docker volume prune -f

