FROM openjdk:21-slim

RUN apt-get update && apt-get install -y apt-utils curl gnupg && apt-get clean
RUN apt-get update && apt-get install -y openvpn && apt-get clean
# Install JavaFX dependencies
RUN apt update && \
    apt install -y libopenjfx-java && \
    apt clean

WORKDIR /app

COPY ./certs /app
COPY target/java-vpn-frontend-1.0.jar /app/app.jar

RUN chmod 660 /app/*.key /app/*.crt /app/client.ovpn

CMD ["java", "-jar", "java-vpn-frontend-fat.jar"]
