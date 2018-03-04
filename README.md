#plese run below command under /qiusheng-friend-json-service directory.
#System are using gradle to build&&deplayment, plese make sure system have installed #gradle before run below command
#System are using embed Tomcat, you can change the port in /qiusheng-friend-json-service/src/main/resources/application.properties

./gradlew clean build && java -jar build/libs/friend-rest-service-0.1.0.jar


#
#Below are some curl commands can help to test
#

curl -H "Content-Type: application/json" -X POST -d '{ "friends":["andy@example.com","john@example.com"] }' http://localhost:9000/friend/create

curl -H "Content-Type: application/json" -X POST -d '{ "email":"andy@example.com" }' http://localhost:9000/friend/get

curl -H "Content-Type: application/json" -X POST -d '{ "friends":["wilson@example.com","john@example.com"] }' http://localhost:9000/friend/create

curl -H "Content-Type: application/json" -X POST -d '{ "friends":["wilson@example.com","andy@example.com"] }' http://localhost:9000/friend/getCommonFriends

curl -H "Content-Type: application/json" -X POST -d '{ "requestor":"jy@example.com", "target":"wilson@example.com" }' http://localhost:9000/friend/subscribe

curl -H "Content-Type: application/json" -X POST -d '{ "requestor":"wilson@example.com", "target":"java@example.com" }' http://localhost:9000/friend/block

curl -H "Content-Type: application/json" -X POST -d '{ "sender":"wilson@example.com", "text":"Hello World! kate@example.com" }' http://localhost:9000/friend/getRecipients

curl -H "Content-Type: application/json" -X POST -d '{ "friends":["java@example.com","java@example.com"] }' http://localhost:9000/friend/create
