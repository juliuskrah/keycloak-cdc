# Quickly for the impatient

Clone this repository and navigate into the project directory and run the following commands:

```bash
> docker-compose -f docker-compose.nobuild.yml up -d
```

You will need `docker`, `docker-compose` and `git` to run this example.

## Setting up Debezium

Make a JSON POST request (I am using cURL in this example - Use any REST client of choice) using the payload
indicated below:

```bash
> curl -s \
     -X "POST" "http://localhost:8083/connectors/" \
     -H "Content-Type: application/json" \
     -d '{
  "name": "keycloak-connector",
  "config": {
    "connector.class": "io.debezium.connector.postgresql.PostgresConnector",
    "database.hostname": "postgres",
    "database.port": "5432",
    "slot.name": "keycloak_debezium",
    "plugin.name": "pgoutput",
    "database.user": "postgres",
    "database.password": "postgrespwd",
    "database.dbname": "sample",
    "database.server.name": "keycloak",
    "schema.whitelist": "sample",
    "table.whitelist": "sample.user_role_mapping,sample.user_group_membership,sample.keycloak_role,sample.user_entity"
  }
}'
```

Navigate to the [URL](http://localhost:8080/auth/realms/master/protocol/openid-connect/auth?client_id=security-admin-console&redirect_uri=http%3A%2F%2Flocalhost%3A8080%2Fauth%2Fadmin%2Fmaster%2Fconsole%2F%23%2Frealms%2Fmaster%2Fusers&state=6a3729e8-ce83-4b6c-af62-87669d336ce2&response_mode=fragment&response_type=code&scope=openid&nonce=7971059d-cf2a-48ee-8f0c-209169c664bf)
and switch to the `sample` realm to start hacking;

Use the credentials below to log in:

```
Username: test
Password: test
```


# Introduction

This repository contains a sample project on how to stream in real-time, users created in Keycloak to your
application.

## Prerequisite

To be able to run this application, you need the following to be setup:

- Postgres
- Zookeeper
- Kafka
- Kafka Connect
- Keycloak
- Java
- Gradle

You don't have to set these up if you're using docker. I have provided a [compose file](./docker-compose.nobuild.yml) to ease this.

# Develop

For developers that want to contribute, you need Java setup. Clone this repository and 
navigate to the `app-service` directory. From there, run the following command:

```bash
> ./gradlew run
```

View real-time changes with:

```bash
> curl http://127.0.0.1:8082/api/users/stream
```
