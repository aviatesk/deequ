# this docker image is supposed to be used for deequ's development

# TODO perform migration to sbt

# adapated from https://github.com/big-data-europe/docker-spark/blob/8b59cf4a71b1cc4e1eac3b5f5b231c7d30f7c475/template/scala/Dockerfile
FROM bde2020/spark-submit:3.0.1-hadoop3.2

# general dev tools
RUN apk add --no-cache \
    git

# spark setup
ENV PATH /spark/bin:${PATH}

# maven setup
RUN apk add --no-cache openjdk8 maven
ENV JAVA_HOME="/usr/lib/jvm/java-1.8-openjdk/"

COPY . /deequ
WORKDIR /deequ

RUN mvn package
