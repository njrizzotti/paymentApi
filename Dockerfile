FROM gradle:8.0.1-jdk17

WORKDIR .\portxChallenge
COPY . .
RUN ./gradlew clean build

CMD ./gradlew bootRun
