# Ensure your Docker Daemon is running
### cd to 'ems', and 
### docker-compose up

# Two ways to register a new userS for an end-to-end experience

## Via API, only 'superuser' role can do this
POST http://localhost:8080/user/register
Content-Type: application/json
Authorization: Basic {{superuseremail}} {{superpassword}}

{
"id": 0,
"name": "PW5",
"username": "PW4",
"password": "secret-user-test",
"email": "pw5@gmail.com",
"roles": ["superuser"]
}

## Via front-end, 'admin' and 'superuser' role can do it


### Ensure your Docker Daemon is running
### cd to 'ems', and 
# docker-compose up

# Change logging level - 

POST http://localhost:8080/actuator/loggers/org.springframework.security
Authorization: Basic {{superuseremail}} {{superpassword}}
Content-Type: application/json

{
"configuredLevel": "INFO"
}



