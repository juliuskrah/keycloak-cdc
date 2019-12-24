FROM jboss/keycloak:8.0.1

LABEL maintainer="Julius Krah <juliuskrah@gmail.com>"

COPY sample_realm.json /opt/jboss/import/sample_realm.json

ENV KEYCLOAK_IMPORT /opt/jboss/import/sample_realm.json