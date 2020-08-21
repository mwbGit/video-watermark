FROM java:8
EXPOSE 8088

VOLUME /tmp
ADD video-watermark-1.0.0-SNAPSHOT.jar  /app.jar
RUN bash -c 'touch /app.jar'
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
