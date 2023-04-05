
# Rova Account Service Documentation

&nbsp;

### Language :

	Java 11

### Framework :

	Springboot 2.7.11

### Database :

    Mysql Database
&nbsp;

### How to run :

#### Option 1 (Run in docker container)

If you have docker installed, bring up your terminal, navigate to the root directory of the project and follow the steps below

* To build the code run the command below--

  docker build -t revotransactionservice .

* To run the code run the command below--

  docker run -it -p 1837:81 revotransactionservice

#### Option 2 (Run from IDE)

open the project in Intellij-IDEA and run or follow the guide in the link below for better explanation
https://www.youtube.com/watch?v=kQ6Zkb6s6mM

&nbsp;

### Link to Postman collection

https://we.tl/t-ZPdn9VyaQk

### Link to Swagger documentation
http://localhost:1837/swagger-ui.html#/


### Important points to note

* I added a create a user endpoint because a user needs to exist before a current account can be opened for such user.

* Userid of the existing user is the customer ID and this is what you when you want to push a create bank account request.

* When you create a new bank account and the initial credit is greater than zero, the bank account is created while the transaction is pushed to sqs.

* The transaction service listens for incomming messages. Once message is queud it picks it and saves the transaction attaches the transaction to the user.

* When you try to get details of a user user, the account  service sends a request to the transaction service, the transaction service responds with all the user transactions.

### Things I would have done better if I had more time

1. I would have written more test cases and ensured I get a test coverage of a minimum of 80 percent
2. I would have worked on a UML diagram and data flow diagram.