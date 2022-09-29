all: docker_run

docker_build:
	@docker build -t transfer-api .

docker_build_run: docker_build
	@docker run -d -p 8080:8080 --name transfer-api transfer-api

docker_run:
	@docker run -d -p 8080:8080 --name transfer-api transfer-api

docker_stop:
	@docker stop transfer-api

