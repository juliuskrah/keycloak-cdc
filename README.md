# Introduction

This repository contains a sample project on how to stream created users in real-time from Keycloak to your
application.

To be able to run this application, you need the following to be setup:

- Postgres
- Zookeeper
- Kafka
- Kafka Connect
- Keycloak

## Quick Start

If you're impatient, just download this (docker-compose.nobuild.yml) file and execute the following
commands:

```bash
> docker-compose -f docker-compose.nobuild.yml up -d
```

Navigate :keycloak-user-url: and start hacking. Use the credentials below to log in:

```
Username: test
Password: test
```