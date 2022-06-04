# Coding challenge Project to Develop REST APIs using Spring boot 

## The Challenge

Your task is to create a foreign exchange rate service as SpringBoot-based microservice. 

The exchange rates can be received from [2]. This is a public service provided by the German central bank.

As we are using user story format to specify our requirements, here are the user stories to implement:

- As a client, I want to get a list of all available currencies
- As a client, I want to get all EUR-FX exchange rates at all available dates as a collection
- As a client, I want to get the EUR-FX exchange rate at particular day
- As a client, I want to get a foreign exchange amount for a given currency converted to EUR on a particular day

If you think that your service would require storage, please use H2 for simplicity, even if this would not be your choice if 
you would implement an endpoint for real clients. 


## Setup
#### Requirements
- Java 11 (will run with OpenSDK 15 as well)
- Maven 3.x

#### Project
The project was generated through the Spring initializer [1] for Java
 11 with dev tools and Spring Web as dependencies. In order to build and 
 run it, you just need to click the green arrow in the Application class in your Intellij 
 CE IDE or run the following command from your project root und Linux or ios. 

````shell script
$ mvn spring-boot:run
````

After running, the project, switch to your browser and hit http://localhost:8080/api/currencies. You should see some 
demo output. 


[1] https://start.spring.io/

[2] [Bundesbank Daily Exchange Rates](https://www.bundesbank.de/dynamic/action/en/statistics/time-series-databases/time-series-databases/759784/759784?statisticType=BBK_ITS&listId=www_sdks_b01012_3&treeAnchor=WECHSELKURSE)


#### Features

The loading of the data into H2 database is done using RestTemplate API and used multithreading to speed up the loading. 
Controller, Service, Entity are developed to serve the below mentioned REST endpoints.
Test cases are written using junit and mockito.
Lombook used to minimize the boilerplate code.


#### Endpoints

Following endpoints are available in this project:
[a] http://localhost:8080/api/currencies

This end point returns the available list of unique currencies. 

[b] http://localhost:8080/api/exchangerate

This end point returns the list of all exchange rates available.

[c] 
http://localhost:8080/api/exchangerate?inputDate=2022-05-06
http://localhost:8080/api/exchangerate?inputDate=2022-05-05&currencyUnit=AUD

This end point returns the list exchange rates available filtered based on inputDate OR inputDate & currencyUnit.

[d] http://localhost:8080/api/currencyconversion

This end point converts the input amount to EUR based on CurrencyUnit(Code) and date. The input is provided using request body as shown below:

{
"date":"2022-05-06",
"currencyUnit":"AUD",
"inputAmount":2000
}

#### Configuration

[a] The URLs from [Bundesbank Daily Exchange Rates] should be mentioned in the exchangerate.properties files. When spring boot application starts it will load all the data into H2 memory based database. Sample URLs are already mentioned in the property file.

[b] Number of threads for loading the data can be configured in application.properties file under tag name "number-of-threads".


###Screen shots

Description: List of all currencies:

![image](https://user-images.githubusercontent.com/59464659/171994464-e89efc45-54f7-42ce-9229-aa4778182b07.png)

Description: List of all exchange rates:

![image](https://user-images.githubusercontent.com/59464659/171994507-42651109-6522-41c8-8c7f-44f26a8e8f8b.png)

Description: List of exchange rates for specific date of all currencies:

![image](https://user-images.githubusercontent.com/59464659/171994532-ba505c76-8799-4502-8099-a72f897b8200.png)

Description: List of exchange rates for specific date of specific currency:

![image](https://user-images.githubusercontent.com/59464659/171994547-e328fd93-3bcc-42b1-a1c3-2312f3c9d161.png)

Description: Converts the mentioned currency of input date to EUR:

![image](https://user-images.githubusercontent.com/59464659/171994569-3f244fc3-33ee-4407-92cb-8beaaca0d35d.png)


