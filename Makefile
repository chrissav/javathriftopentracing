registry := my_registry
tag := latest


build:
	gradle shadowJar
	docker build -t javaopenthrift .
	docker tag javaopenthrift ${registry}:${tag}
	docker push ${registry}:${tag}

deploy:
	kubectl apply -f deployment.yaml
