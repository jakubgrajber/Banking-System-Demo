FROM openjdk

LABEL Author = "Jakub Grajber"

ADD src/main/java/com/smart/tech/start/BankingSystemApplication.java /opt/

WORKDIR opt

RUN javac BankingSystemApplication.java

CMD java -jar BankingSystemDemo.jar
