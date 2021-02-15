# PointTrackingApplication
This project contains the code for the points tracking service for fetch reward interview

## Requirements

* Java 11
  https://www.oracle.com/java/technologies/javase-jdk11-downloads.html
* Maven
  install instruction here(https://maven.apache.org/install.html)

### Local Run
Step 1: pull the code and cd into the root directory with Command line

Step 2: run command 
  $mvn package 
to build the jar file to the /target folder, you should be able to see the BUILD SUCCESS message.

Step 3: run the application with command
  $java -jar *absolute-path-on-your-laptop*\SampleApplication-0.0.1-SNAPSHOT.jar

Step 4: once the application is running, it is accessable at path: http://localhost at port:8080
you may run following cmds to test its feature:
  $ curl --location --request GET 'http://localhost:8080/awesome_service/health_check'   
  should return 200OK indicting application is up and running
  
Step 5: run follow curl command for adding data into the "DB" and test the addPoint feature:
  $ curl --location --request POST 'http://localhost:8080/awesome_service/1/points' \
--header 'Content-Type: application/json' \
--data-raw '{
    "payerId": "tester3 inc.",
    "points": 100000,
    "timeStamp": "05/31 10AM"
}'
  
  $ curl --location --request POST 'http://localhost:8080/awesome_service/1/points' \
--header 'Content-Type: application/json' \
--data-raw '{
    "payerId": "Jan_tester LLC",
    "points": 10000,
    "timeStamp": "01/31 08AM"
}'

$ curl --location --request POST 'http://localhost:8080/awesome_service/1/points' \
--header 'Content-Type: application/json' \
--data-raw '{
    "payerId": "tester company",
    "points": 400,
    "timeStamp": "01/31 09PM"
}'

$ curl --location --request POST 'http://localhost:8080/awesome_service/1/points' \
--header 'Content-Type: application/json' \
--data-raw '{
    "payerId": "tester company",
    "points": 500,
    "timeStamp": "01/31 09PM"
}'

$ curl --location --request POST 'http://localhost:8080/awesome_service/1/points' \
--header 'Content-Type: application/json' \
--data-raw '{
    "payerId": "tester company",
    "points": -600,
    "timeStamp": "01/31 10PM"
}'

$ curl --location --request GET 'http://localhost:8080/awesome_service/1/points' 
should return points balance for each payer

$ curl --location --request POST 'http://localhost:8080/awesome_service/1/points/deduct' \
--header 'Content-Type: application/json' \
--data-raw '{
    "amount": 30000
}'
should return the expected transaction. You may run the GET request again to check the accuracy of balances after deduct.

# API doc
POST 'http://localhost:8080/awesome_service/1/points'
{
    "payerId": "String",
    "points": Long,
    "timeStamp": "String"
}
POST to /points will adding points, all fields are required. When points is negative not enough points for that payer will result in 403.
Timestamp is in the format given by the question doc(MM/DD HHaa) assuming the year it current year, and will be parsed into long and used to sort.
(For this excerse, service is not using a real database, a priority queue is used to mimick the sorting and select from DB)

Transactions will be used in the sorted manner to encounter for deduction.

GET http://localhost:8080/awesome_service/1/points
Will query the current points balance with each payer for the user. 
(For this excerse, service is not using a real database, a HashMap is used to store the key-value for payerId - balance data)
Not like most real datastorage, HashMap is not thread-safe, so this application with current setup cannot deal with multithread scenario. 

POST 'http://localhost:8080/awesome_service/1/points/deduct'
{
    "amount": Long
}
Will deduct x amount of points from the user, will start with oldest transactions. Response will include all affected transactions.
