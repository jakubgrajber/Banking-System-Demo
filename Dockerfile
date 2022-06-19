FROM openjdk

ADD target/BankingSystemDemo-1.0-SNAPSHOT.war banking-system-demo.war

ENTRYPOINT ["java", "-jar", "banking-system-demo.war"]

