FROM tomcat:9.0.12-jre8
ADD api.app/target/api.war /usr/local/tomcat/webapps/ 
ADD gwt.app/target/gavka.war /usr/local/tomcat/webapps/ 
