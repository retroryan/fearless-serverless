# fearless-serverless

# Setup PG

Install PG
```shell script
    brew install postgresql
```

Overview of PG Commands - init DB 

```shell script
    initdb -D /usr/local/var/postgres
    pg_ctl -D /usr/local/var/postgres -l logfile start
    createuser spaceman
    createdb space_widgets -O spaceman
    psql space_widgets -U spaceman -h localhost
```

psql commands - be sure to import the schema!

```shell script
    \i schema/schema-postgres.txt
    \dt
    \du
    select * from space_widgets ;
```

Clean up commands
```shell script
    dropdb spaceman
    pg_ctl -D /usr/local/var/postgres stop
```

# Testing Locally with httpie

[Install httpie](https://httpie.org/)

```html
 http post :8080/add id="3425-PART-A" description="Space Shield"
```
 
 # Building and pushing the docker image
 
 ```shell script
    sbt docker:publishLocal 
    docker tag fearless-serverless:0.3 gcr.io/none-219021/fearless-service:0.3
    docker push gcr.io/none-219021/fearless-service:0.3
```

Verify the image is in gcr:

```shell script
    gcloud container images list | grep fearless
    gcloud container images list-tags gcr.io/none-219021/fearless-service
```

 # Deploying to Kubernetes and Knative
 
The deploy steps assume you have a working knowledge of kubernetes and knative.  The deployment was done with GCP and a cloud shell but can be easily done on any Kubernetes cluster with Istio and Knative.
 
The [Portable Serverless Workshop](https://docs.google.com/document/d/1bWAxf5dXgPYWKkrRussz5h8qfCQU7vSpFIpYEBPZGP8/edit#) shows how to setup a Kubernetes cluster with Istio and Knative.  
  
In this simple tutorial we run the kubernetes pods and the knative service deployments scripts on the cloud console.  To get the scripts in the console git clone this repo inside the cloud console.
 
In the cloud console or the server with access to kubernetes run the following command to get the source yaml scripts for deployments:
  
 ```shell script
    git clone https://github.com/retroryan/fearless-serverless
    cd fearless-serverless/knative
```

## Deploying a Microservice to Kubernetes

 ```shell script
    kubectl apply -f fearless-deployment-v1.yaml

    kubectl port-forward fearless-6447f6588f-8wkpb 8080:8080
```

## Deploying a Microservice to Knative

The [Portable Serverless Workshop](https://docs.google.com/document/d/1bWAxf5dXgPYWKkrRussz5h8qfCQU7vSpFIpYEBPZGP8/edit#) uses a Tekton Pipeline to compile the app from the github repo, create the container image, push it to GCR, and deploy the image in Knative Serving.

And then deploy the knative service with:

 ```shell script
    kubectl apply -f knative/fearless-serverless-v1.yaml
```


 # Building and pushing the docker image

Get the IP and hostname for the service:

```shell script
export IP_ADDRESS=$(kubectl get svc istio-ingressgateway -n istio-system \
  -o 'jsonpath={.status.loadBalancer.ingress[0].ip}')
echo $IP_ADDRESS


export HOST_NAME=$(kubectl get route fearless-service \
  -o 'jsonpath={.status.url}' | cut -c8-)
echo $HOST_NAME
```

Curl the service:

```shell script
    curl -w'\n' -H "Host: $HOST_NAME" http://${IP_ADDRESS}/widgets
```

Create a custom domain

```shell script
  sed -i -e "s/{IP_ADDRESS}/$IP_ADDRESS/"  knative/custom-domain.yaml
  kubectl apply -f custom-domain.yaml
  echo "http://fearless-service.$IP_ADDRESS.nip.io"
```

```shell script
http post http://fearless-service.default.35.239.253.188.nip.io/add id="3425-PART-A" description="Space Shield"
```

# Clean up or retry

```shell script
  kubectl delete -f knative/fearless-service-v1.yaml
```


