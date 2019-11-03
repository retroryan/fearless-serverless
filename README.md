# fearless-serverless



# Testing Locally with httpie

[Install httpie](https://httpie.org/)

```html
 http post :8080/add id="3425-PART-A" description="Space Shield"
```
 
 # Building and pushing the docker image
 
 ```shell script
    sbt docker:publishLocal 
    docker tag fearless-serverless:0.1 gcr.io/none-219021/fearless-service:0.1
    docker push gcr.io/none-219021/fearless-service:0.1
```

Verify the image is in gcr:

```shell script
    gcloud container images list | grep fearless
    gcloud container images list-tags gcr.io/none-219021/fearless-service
```

 # Deploying
 
The deploy steps assume you have a working knowledge of kubernetes and knative.  

The deployment was done with GCP and a cloud shell but can be easily done on any kubernetes cluster with Istio and Knative.
 
The [Portable Serverless Workshop](https://docs.google.com/document/d/1bWAxf5dXgPYWKkrRussz5h8qfCQU7vSpFIpYEBPZGP8/edit#) shows how to setup a kubernetes cluster with knative.  
 
The workshop uses a Tekton Pipeline to compile the app from the github repo, create the container image, push it to GCR, and deploy the image in Knative Serving.

In this simple tutorial we are just going to run the the knative service deployment scripts to the machine. You can either copy and paste to a file on the server or git clone this repo.
 
In the cloud console or the server with kubernetes run the following command to get the source yaml scripts for deployments:
  
 ```shell script
    git clone https://github.com/retroryan/fearless-serverless
    cd fearless-serverless
```

And then deploy the knative service with:

 ```shell script
    kubectl apply -f knative/fearless-serverless-v1.yaml
```


 # Building and pushing the docker image


export IP_ADDRESS=$(kubectl get svc istio-ingressgateway -n istio-system \
  -o 'jsonpath={.status.loadBalancer.ingress[0].ip}')
echo $IP_ADDRESS

Get the hostname for the service:

export HOST_NAME=$(kubectl get route kafka-to-web \
  -o 'jsonpath={.status.url}' | cut -c8-)
echo $HOST_NAME

Curl the service:

curl -w'\n' -H "Host: $HOST_NAME" http://${IP_ADDRESS}


export IP_ADDRESS=$(kubectl get svc istio-ingressgateway -n istio-system \
  -o 'jsonpath={.status.loadBalancer.ingress[0].ip}')
echo $IP_ADDRESS

custom domain


kubectl apply -f custom-domain.yaml


echo "http://kafka-to-web.default.$IP_ADDRESS.nip.io"


