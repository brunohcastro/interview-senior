#!/usr/bin/env bash

./mvnw clean install && docker-compose up -d --scale core=3
