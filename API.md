# API Documentation
###### All the REST API will return a json response composed of 2 fields: errorCode and message. 
* If the errorCode is 1, it means the operation fails. 
* If the errorCode is 0, it means the operation succeeds.

###### User API
* POST /users/login
`````
In the response, in addition to errorCode and message, it will also return mode: either admin or user.
Example:
POST ec2-52-27-135-64.us-west-2.compute.amazonaws.com:3000/users/login
Body: 
{
  "userName": "uTest",
  "password": "12345678"
}
Response: 
{"errorCode":0,"message":"Logged in successfully.","mode":"user"}
`````
* POST /users/register
`````
POST ec2-52-27-135-64.us-west-2.compute.amazonaws.com:3000/users/register
Body:
{
  "phoneNumber": "3445666",
  "password": "12334",
  "email": "utest@gmail.com",
  "firstName": "User",
  "lastName": "Tester",
  "userName": "cloudTester"
}
Response:
{"errorCode":0,"message":"Created account successfully."}
`````

* PUT /users/update/:userName
`````
PUT ec2-52-27-135-64.us-west-2.compute.amazonaws.com:3000/users/update/uTest
Body: 
{
  "firstName": "Update new first Name"
}
Response:
{"errorCode":0,"message":"Update user info successfully."}
`````


###### Comment API
* GET /comments/list
`````
GET ec2-52-27-135-64.us-west-2.compute.amazonaws.com:3000/comments/list
Response:
{"errorCode":0,"message":[{"date":"2016-04-24T08:54:02.318Z","userName":"uTest","comment":"Great application","rating":3.5},{"date":"2016-04-24T08:55:00.045Z","userName":"nest","comment":"Amazing application","rating":4},{"date":"2016-04-24T18:42:10.958Z","userName":"nest","comment":"Great application","rating":4}]}
`````

* POST /comments/add
`````
POST ec2-52-27-135-64.us-west-2.compute.amazonaws.com:3000/comments/add
Body:
{
  "userName": "nest",
  "comment": "Great application",
  "rating": 4
}
Response:
{"errorCode":0,"message":"Comment added successfully."}
`````


###### Tree API
* POST /trees/add
`````
POST ec2-52-27-135-64.us-west-2.compute.amazonaws.com:3000/trees/add
Body:
{
  "tree_id": "123",
  "sensor_id": "123",
  "tree_status": "Working",
  "tree_location": "San Jose"
}
Response:
{"errorCode":0,"message":"Tree added successfully."}
`````

* PUT /trees/update/:id
`````
PUT ec2-52-27-135-64.us-west-2.compute.amazonaws.com:3000/trees/update/123
Body:
{
  "tree_location": "San Jose"
}
Response:
{"errorCode":0,"message":"Update tree data successfully."}
`````

* GET /tree/show/:id
`````
GET ec2-52-27-135-64.us-west-2.compute.amazonaws.com:3000/trees/show/123
Response:
{"errorCode":0,"message":{"_id":"571d14bb1bfd28d76a4bf236","deployment_date":"2016-04-24T18:47:23.680Z","tree_id":"123","sensor_id":"123","tree_status":"Working","tree_location":"San Jose","__v":0}}
`````

* GET /trees/list
`````
GET ec2-52-27-135-64.us-west-2.compute.amazonaws.com:3000/trees/list
Response:
{"errorCode":0,"message":[{"deployment_date":"2016-04-24T09:35:50.406Z","tree_id":"hdjjbgdujd","sensor_id":"ggdggfggf","tree_status":"Working","tree_location":"San Jose"},{"deployment_date":"2016-04-24T16:45:52.973Z","tree_id":"1234","sensor_id":"1234","tree_status":"Working","tree_location":"San Barbara"},{"deployment_date":"2016-04-24T18:47:23.680Z","tree_id":"123","sensor_id":"123","tree_status":"Working","tree_location":"San Jose"}]}
`````

* DELETE /trees/delete/:id
`````
DELETE ec2-52-27-135-64.us-west-2.compute.amazonaws.com:3000/trees/delete/123
Response:
{"errorCode":0,"message":"Tree data is deleted."}
`````


###### Sensor API
`````
The usage of sensor API is similar to tree API.
`````
* POST /sensors/add
* GET /sensors/show/:id
* GET /sensors/list
* PUT /sensors/update/:id
* DELETE /sensors/delete/:id


###### Activity API
* POST /activity/record
`````
POST ec2-52-27-135-64.us-west-2.compute.amazonaws.com:3000/activity/record
Body:
{
  "tree_id": "4321",
  "userName": "uTest",
  "activity": "Change Music"
}
Response:
{"errorCode":0,"message":"Successfully record user activity."}


`````
