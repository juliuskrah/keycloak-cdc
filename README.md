# Quickly for the impatient

Clone this repository and navigate into the project directory and run the following commands:

```bash
> docker-compose -f docker-compose.nobuild.yml up -d
```

Navigate to the [URL](http://localhost:8080/auth/realms/master/protocol/openid-connect/auth?client_id=security-admin-console&redirect_uri=http%3A%2F%2Flocalhost%3A8080%2Fauth%2Fadmin%2Fmaster%2Fconsole%2F%23%2Frealms%2Fmaster%2Fusers&state=6a3729e8-ce83-4b6c-af62-87669d336ce2&response_mode=fragment&response_type=code&scope=openid&nonce=7971059d-cf2a-48ee-8f0c-209169c664bf)
and start hacking. Use the credentials below to log in:

```
Username: test
Password: test
```

# Introduction

This repository contains a sample project on how to stream created users in real-time from Keycloak to your
application.

To be able to run this application, you need the following to be setup:

- Postgres
- Zookeeper
- Kafka
- Kafka Connect
- Keycloak

