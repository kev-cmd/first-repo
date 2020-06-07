# SFEats 

SFEats helps users to find food trucks close to them in San Fransisco Area. This project provides REST API ednpoints which can be integrated with web/mobile application in future. 

### REST API
The API module contains REST API endpoints for the SFEats application. 

### Project Structure: 
- Controller : The class is flagged as a @RestController, meaning it is ready for use by Spring MVC to handle web requests.
- Service : The service component class is interface.
- ServiceImpl : The class that implements the Service Interface with @Service annotation. These class files contains bussiness 		logic n a different layer, separated from @RestController class file.
- DTO : This object is created to be used as Data Transfer Objects for future integration with front-end consumer

### Technology Stack:
- Spring Boot for application configuration
- Gradle configuration for building, testing and running the application
- Mockito framework for JUnit Testing
- Project is intended to be source controlled within GitHub

### Developing Locally
 - Required : 
		* Git 
		* Any suitable IDE
 - Recommnded : 
		* IIntellij IDEA
		* VSCode

	1. Configure Git
	2. Pull down the code using git
		You can use the tool you’re most comfortable with 
		git clone https://github.com/kev-cmd/sfeats-svcs.git
	3. Import project dependency from gradle
	4. Run server locally
		./gradlew bootRun
		For local development, server can be started and hosted on 
		http://localhost:8080/

### Sample API

#### Request: 
	[food-trucks/location/{latitude}/{longitude}?radius={radius}]
                
#### Response: 
    [
        {
            "id": "1334734",
            "name": "Rita's Catering",
            "facilityType": "Truck",
            "foodItems": "Filipino Food",
            "address": "1028 MISSION ST",
            "block": "3703",
            "lot": "033",
            "locationDesc": "MISSION ST: 06TH ST to 07TH ST (1000 - 1099)",
            "distance": 0
        }
    ]
    
#### Status Codes
SFEats returns the following status codes in its API:

| Status Code   | Description           |
| ------------- |:---------------------:|
| 200           | OK                    |
| 201           | CREATED               |
| 400           | BAD REQUEST           |
| 404           | NOT FOUND             |
| 500           | INTERNAL SERVER ERROR |
