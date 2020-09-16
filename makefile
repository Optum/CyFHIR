build:
	bash bin/build-cyfhir.sh

neo4j:
	(bash bin/build-cyfhir.sh) && docker-compose -f docker-compose/docker-compose.neo4j.yaml up --build

test:
	bash bin/build-cyfhir.sh && bash bin/get-plugins.sh && (cd cyfhir && mvn clean test)

server:
	(cd express && open http://localhost:3000/docs && npm start)

backend:
	(bash bin/build-cyfhir.sh) && docker-compose -f docker-compose/docker-compose.yaml up --build

halt:
	docker-compose -f docker-compose/docker-compose.yaml down

clean:
	docker system prune -af --volumes
