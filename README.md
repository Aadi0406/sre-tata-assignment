# sre-tata-assignment

1. Application code <br />
App code is present in app.js, a simple node js application with required data i.e. to print “Welcome to 2022” and user agent info. <br />

Steps if want to run it separate : <br />
a. npm install <br />
b. node app.js (it will run locally and accessible via localhost:3000) <br />

2. Build image and push it to container registry <br />

Pushing image to GAR as having free-tire access of GCP <br />
a. Build image : <br />
docker build -t asia-south1-docker.pkg.dev/august-monolith-418704/app-image/hello-world-image . <br />

b. Tagged image : <br />
docker tag asia-south1-docker.pkg.dev/august-monolith-418704/app-image/hello-world-image:latest <br />

c. Pushing to GAR : <br />
docker push asia-south1-docker.pkg.dev/august-monolith-418704/app-image/hello-world-image:latest <br />

![image](https://github.com/Aadi0406/sre-tata-assignment/assets/89530763/b0d844e4-df7d-4631-bcee-ff9f4b3bc014)

3. Create Infra on GCP via Terraform <br />
Create VPC for networking & GKE for deploying node js application. <br />

Terraform code resides in "infra-terraform" folder under main.tf. <br />

To deploy infra on GCP, can follow below commands - <br />
a. gcloud auth application-default login <br />
b. Terraform init <br />
c. Terraform apply (once plan is avail it will ask for approval to apply the changes, just check the plan and provide input 'yes' if plan looks good.) <br />

VPC -
![image](https://github.com/Aadi0406/sre-tata-assignment/assets/89530763/9ddaa130-ac3b-4d67-a60c-9497c87adc60)


GKE CLUSTER -
![image](https://github.com/Aadi0406/sre-tata-assignment/assets/89530763/fa7e3311-7e25-46a2-bcd4-0d737013dde9)


Now Infra is ready, let's deploy the application on GKE. <br />

4. Deploy application on GKE <br />

deployment.yaml & service.yaml are present to deploy applicaiton on GKE <br />

a. gcloud container clusters get-credentials gke-cluster --zone asia-south1-a --project august-monolith-418704 <br />
b. kubectl create -f deployment.yaml <br />
c. kubectl create -f service.yaml <br />

![image](https://github.com/Aadi0406/sre-tata-assignment/assets/89530763/1496c04b-9831-4529-ab57-681a8256d4c6) <br />

#Endpoint to access application - http://35.200.222.48:3000/
