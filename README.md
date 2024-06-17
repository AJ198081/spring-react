You might need a login for yourself to ensure an end-to-end experience

### Register a new user
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

### Ensure your Docker Daemon is running
cd to 'ems', and 
docker-compose up