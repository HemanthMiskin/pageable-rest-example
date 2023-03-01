-This project contains example RESTful API
-This API connects to MySQL database.
-Setup MySQL DB using mysql docker image. 
-Please visit mysql official image page https://hub.docker.com/_/mysql for more details.

-Run below command in terminal to bring-up the mysql container.
docker run --name mysql -p 3306:3306 --restart always -e MYSQL_ROOT_PASSWORD=payconiq -d mysql
-Access the container bash
docker exec -it mysql bash
-Connect to MySQL DB (password is 'payconiq' )
mysql -h 127.0.0.1 -P 3306 --protocol=tcp -p
-Create database “stocks” 
create database stocks;

Now clone this repository and import project in any IDE.
Launch the application by running src/main/java/com/payconiq/rest/StocksApplication.java
You can find JUNIT tests at src/test/java/com/payconiq/rest/RestApplicationTests.java

-API Documentation link:
http://localhost:8080/swagger-ui/index.html