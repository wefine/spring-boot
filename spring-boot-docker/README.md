## Spring boot docker 

### Build Image
```bash

$ mvn package docker:build
```

### Run Image

#### Default
```bash
$ docker run -p 8080:8080 -t springio/gs-spring-boot-docker
```

#### Using Spring Profiles
```bash
$ docker run -e "SPRING_PROFILES_ACTIVE=prod" -p 8080:8080 -t springio/gs-spring-boot-docker
```
or
```bash
$ docker run -e "SPRING_PROFILES_ACTIVE=dev" -p 8080:8080 -t springio/gs-spring-boot-docker
```

#### Debugging the application in a Docker container
```bash
$ docker run -e "JAVA_OPTS=-agentlib:jdwp=transport=dt_socket,address=5005,server=y,suspend=n" -p 8080:8080 -p 5005:5005 -t springio/gs-spring-boot-docker
```