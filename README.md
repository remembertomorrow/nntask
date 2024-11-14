# nntask
Wojtek Malek's implementation of the NN take home task

docker build -t nntask_db .
docker run --name nntask_db -p 5432:5432 -d nntask_db

Swagger documentation can be accessed at:

`/api/v1/swagger-ui/index.html`