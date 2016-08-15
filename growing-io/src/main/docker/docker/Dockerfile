FROM docker-registry.easemob.com/java8
VOLUME /tmp
ADD @project.artifactId@-@project.version@.jar app.jar
RUN bash -c 'touch /app.jar'
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]