# 使用 Java 21 作为基础镜像
FROM openjdk:21-jdk-slim

# 设置工作目录
WORKDIR /app

# 暴露端口，具体的端口可以在 docker-compose 中覆盖
EXPOSE 8080

# 启动微服务，具体的 jar 包名称可以在 docker-compose 中覆盖
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
