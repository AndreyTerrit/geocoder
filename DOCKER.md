
```bash
$ sudo docker run \
    --name geocoder-postgres \
    -e POSTGRES_PASSWORD=geocoder \
    -d postgres:15

$ sudo docker exec -it geocoder-postgres /bin/bash
```

```bash
$ ./gradlew bootJar
$ sudo docker build --build-arg JAR_FILE="./build/libs/geocoder-0.0.1-SNAPSHOT.jar" -t geocoder:latest .
$ sudo docker compose up -d
```
