# Use the official Maven image with JDK 17 as the base image
FROM maven:3-openjdk-18-slim AS build

ENV TZ=Asia/Singapore
ENV GOOGLE_APPLICATION_CREDENTIALS=/app/src/main/resources/credentials_google.json

# Set the working directory
WORKDIR /app

# Copy the pom.xml file
COPY pom.xml ./

# Copy the source code
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# Install required tools and download watchexec
RUN apt-get update && \
    apt-get install -y wget tar xz-utils && \
    apt-get install -y supervisor && \
    wget -qO- https://github.com/watchexec/watchexec/releases/download/cli-v1.17.1/watchexec-1.17.1-x86_64-unknown-linux-gnu.tar.xz | tar -xJf - -C /usr/local/bin --strip-components 1 && \
    apt-get autoremove -y && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*


COPY supervisord.conf /etc/supervisor/conf.d/supervisord.conf

EXPOSE 8080

# Set the entrypoint to watchexec
ENTRYPOINT ["supervisord", "-c", "/etc/supervisor/conf.d/supervisord.conf"]
