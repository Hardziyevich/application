#Introduction

Software represent CRUD application. That is allowed to manipulation set of passive electronic component and their properties.


#Non-functional requirements

* API: HTTP, request and response message - JSON format;<br/>
* JDK 16+;<br/>
* Maven 3.6+;<br/>
* Database PostgreSQL.<br/>
* Docker-machine.<br/>

#Functional requirements

The application must create, delete, update, select elements from database depending on http request will show information about capacitors and resistors.</br>

Http request should be for {element} where {element} is capacitor, resistor:

⦁	GET localhost:8081/{element}/search?{filter} - application should send response body with information about element in JSON type. Http response should be contained 200 codes if the requested resource was found.

Possible filters:</br>
&#8658; case - 0402, 0603, 0805, 1210;</br>
&#8658; value - digit value;</br>
&#8658; voltage - digit value + symbol "V" only for capacitor. Example : 25V";</br>
&#8658; power - double value + symbol "W" only for resistor. Example : 0.1W";</br>
&#8658; unit - for capacitor - pF|uF, for resistor - Ohm|kOhm;</br>
&#8658; temp-low - digit value. Example: -65°C ;</br>
&#8658; temp-high - digit value. Example: +55°C ;</br>

⦁	POST localhost:8081/ {element} /create - create element base on request body - json-type. Http response should be contained 201 codes if element create.</br> 
The request should contain:</br>
Resistor: value, unit, power, case, temp-low,temp-high.
Capacitor: value, unit, voltage, case, temp-low,temp-high.

⦁	DELETE localhost:8081/ {element} /delete - delete element base on request body - json-type. Http response should be contained 204 codes if the requested was successful.</br>

⦁	POST localhost:8081/ {element} /update - update element base on request body - json-type. Http response should be contained 201 codes if the requested was successful.</br>
The request should contain:</br>
Resistor: id, value, unit, power.
Capacitor: id, value, unit, voltage.

⦁	Other request should be ignored and response code 404.

Main structure database represents on pictures.

![img_1.png](img_1.png)

#Use case

$ git clone https://github.com/Hardziyevich/application


###Create capacitor:

Request:</br>

Method: POST</br>
URL: http://localhost:8081/capacitor/create </br>
Headers:Content-type - application/json</br>
Json body:</br>
{</br>
    "value":15,</br>
    "unit":"pF",</br>
    "voltage":"25V",</br>
    "case":"0402",</br>
    "temp-low":"-55°C",</br>
    "temp-high":"+125°C"</br>
}</br>

Response: http status - 201.</br>

###DELETE capacitor:

Request:</br>

Method: DELETE</br>
URL: http://localhost:8081/capacitor/delete?id=1 </br>

Response: http status - 204.</br>

###Update capacitor request:

Request:</br>

Method: POST</br>
URL: http://localhost:8081/capacitor/update </br>
Headers:Content-type - application/json</br>
Json body:</br>
{</br>
"id":12,</br>
"value":193,</br>
"unit":"pF",</br>
"voltage":"11V"</br>
}</br>

Response: http status - 201.</br>

###Search capacitor request:

Request:</br>

Method: GET</br>
URL: http://localhost:8081/capacitor/search?case=0603 </br>

Response: 
http status - 200.</br>
Json body:</br>
{</br>
"unit": "pF",</br>
"temp-high": "+175°C",</br>
"temp-low": "-65°C",</br>
"value": 100,</br>
"case": "0603",</br>
"voltage": "50V"</br>
}{</br>
"unit": "pF",</br>
"temp-high": "+175°C",</br>
"temp-low": "-65°C",</br>
"value": 193,</br>
"case": "0603",</br>
"voltage": "11V"</br>
}</br>

###Create resistor:

Request:</br>

Method: POST</br>
URL: http://localhost:8081/resistor/create </br>
Headers:Content-type - application/json</br>
Json body:</br>
{</br>
"value":15,</br>
"unit":"Ohm",</br>
"power":"0.1W",</br>
"case":"0402",</br>
"temp-low":"-55°C",</br>
"temp-high":"+125°C"</br>
}</br>

Response: http status - 201.</br>

###DELETE resistor:

Request:</br>

Method: DELETE</br>
URL: http://localhost:8081/resistor/delete?id=6 </br>

Response: http status - 204.</br>

###Update resistor request:

Request:</br>

Method: POST</br>
URL: http://localhost:8081/resistor/update </br>
Headers:Content-type - application/json</br>
Json body:</br>
{</br>
"id":12,</br>
"value":193,</br>
"unit":"Ohm",</br>
"power":"0.25W"</br>
}</br>

Response: http status - 201.</br>

###Search resistor request:

Request:</br>

Method: GET</br>
URL: http://localhost:8081/resistor/search?value=10&unit=kOhm </br>

Response:
http status - 200.</br>
Json body:</br>
{</br>
"unit": "kOhm",</br>
"temp-high": "+105°C",</br>
"temp-low": "-55°C",</br>
"power": "0.1W",</br>
"value": 10,</br>
"case": "0603"</br>
}

#Test case

All application covered integration test.

#User guide

###Compile project and create container

git clone https://github.com/Hardziyevich/application </br>
cd ./application</br>
mvn clean package</br>
docker build -t jar -f docker/Dockerfile . </br>
cd ./docker-compose </br>
docker compose up

curl example for request: 

Capacitor:</br>

Search: curl -X GET http://localhost:8081/capacitor/search?id=1 </br>
Delete: curl -X DELETE http://localhost:8081/capacitor/delete?id=1 </br>
Update: curl -X POST http://localhost:8081/capacitor/update -H 'Content-Type: application/json' -d '{"id":1,"value":200,"unit":"pF","voltage":"11V"}' </br>
Create: curl -X POST http://localhost:8081/capacitor/create -H 'Content-Type: application/json' -d '{"value":115,"unit":"pF","voltage":"25V","case":"0402","temp-low":"-55°C","temp-high":"+125°C"}' </br>

Resistor:</br>

Search: curl -X GET http://localhost:8081/resistor/search?value=10&unit=kOhm </br>
Delete: curl -X DELETE http://localhost:8081/resistor/delete?id=1 </br>
Update: curl -X POST http://localhost:8081/resistor/update -H 'Content-Type: application/json' -d '{"id":1,"value":120,"unit":"Ohm","power":"0.25W"}' </br>
Create: curl -X POST http://localhost:8081/resistor/create -H 'Content-Type: application/json' -d '{"value":15,"unit":"kOhm","power":"0.1W","case":"0402","temp-low":"-55°C","temp-high":"+125°C"}' </br>

If you want to stop application You will need to send http request:</br>
http://localhost:8081/stop </br>
Method:DELETE