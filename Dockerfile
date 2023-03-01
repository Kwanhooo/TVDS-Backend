FROM hub.c.163.com/dwyane/openjdk:8
EXPOSE 8080
MAINTAINER Kwanho <kwanhooo@foxmail.com>
COPY *.jar /app.jar
CMD ["--server.port=8080"]
ENTRYPOINT ["java", "-jar", "/app.jar"]
