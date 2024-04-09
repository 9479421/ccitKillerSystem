FROM openjdk:11
   MAINTAINER wqby
   ENV TZ=Asia/Shanghai
   RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
   ADD ccitserver.jar app.jar
   EXPOSE 9001
   ENTRYPOINT ["java","-Dmirai.slider.captcha.supported","-jar","app.jar"]