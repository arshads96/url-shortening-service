#Readme


About the project

This project is a Spring Boot application for shortening long urls.

APIs:

    1. Create short url  ("/shorten")
    2. Redirect to long url using short code ("/{shortCode}")
    3. Management ports for metrics ("/metrics/top-shortened-domains")
    4. Use the postman collection present in code for accessing the apis ("url-shortening-service.postman_collection.json")

Ports:

    1. 8080 for API and metrics

Assumptions/pre-requisites:

    1. Docker installed on machine
    2. Postman
    3. Java 17 or above
    4. Maven 3.8 or above

A. Clean Build project and create jar file using command: 
        
        mvn clean install

B. For Creating and deploying docker image of application follow below steps:

    1. Open terminal and navigate to url-shortening-service directory, Dockerfile is present there.
    2. Run command: docker build -t url-shortening-service:latest .
    3. Run command: docker run -p 8080:8080 -p 8090:8090 url-shortening-service:latest -d
