FROM sbtscala/scala-sbt:eclipse-temurin-17.0.4_1.7.1_3.2.0
RUN mkdir -p /home/app
COPY . /home/app
CMD ["sh","/home/app/start.sh"]

