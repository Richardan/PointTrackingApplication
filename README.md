# PointTrackingApplication
This project contains the code for the points tracking service for fetch reward interview

## Requirements

* Java 11
* Maven

### Local Run
Step 1: pull the code and cd into the root directory with Command line

Step 2: run command 
  $mvn package 
to build the jar file to the /target folder, you should be able to see the BUILD SUCCESS message.

Step 3: run the application with command
  $java -jar *absolute-path-on-your-laptop*\SampleApplication-0.0.1-SNAPSHOT.jar

Step 4: once the application is running, it is accessable at paht: http://localhost at port:8080
run following cmds to test it:
  $ curl --location --request GET 'http://localhost:8080/awesome_service/health_check'   should return 200OK to indicting application is up and running
  
Step 5: run follow curl command for adding data into the "DB" and test the addPoint feature:
  $ curl --location --request POST 'http://localhost:8080/awesome_service/1/points' \
--header 'Content-Type: application/json' \
--data-raw '{
    "payerId": "tester3 inc.",
    "points": 100000,
    "timeStamp": "05/31 09AM"
}'
  
  $ curl --location --request POST 'http://localhost:8080/awesome_service/1/points' \
--header 'Content-Type: application/json' \
--data-raw '{
    "payerId": "Jan_tester LLC",
    "points": 10000,
    "timeStamp": "01/31 09AM"
}'

$ curl --location --request POST 'http://localhost:8080/awesome_service/1/points' \
--header 'Content-Type: application/json' \
--data-raw '{
    "payerId": "tester company",
    "points": 400,
    "timeStamp": "12/31 09PM"
}'

$ curl --location --request POST 'http://localhost:8080/awesome_service/1/points' \
--header 'Content-Type: application/json' \
--data-raw '{
    "payerId": "tester company",
    "points": 500,
    "timeStamp": "11/31 09PM"
}'

$ curl --location --request POST 'http://localhost:8080/awesome_service/1/points' \
--header 'Content-Type: application/json' \
--data-raw '{
    "payerId": "tester company",
    "points": -600,
    "timeStamp": "12/31 09PM"
}'

$ curl --location --request GET 'http://localhost:8080/awesome_service/1/points' 
should return points balance for each payer

$ curl --location --request POST 'http://localhost:8080/awesome_service/1/points/deduct' \
--header 'Content-Type: application/json' \
--data-raw '{
    "amount": 30000
}'
should return the expected transaction. You may run the GET request again to check the accuracy of balances after deduct.

