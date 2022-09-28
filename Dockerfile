FROM gradle:6.9.2-jdk11

# TODO
# Why doesn't gradle build work here?
# Copy the binary manually for now
COPY build/libs/minimal-0.2.0.jar build/libs/minimal-0.2.0.jar

ENTRYPOINT ["java", "-cp", "build/libs/minimal-0.2.0.jar", "MultiplicationServer"]
