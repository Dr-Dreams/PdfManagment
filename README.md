# PDF Management & Collaboration System

The PDF Management & Collaboration System is a web application designed to simplify the
management and collaboration of PDF files. It provides users with the ability to securely upload,
share, and collaborate on PDF documents. The system ensures easy access to shared files,
facilitates commenting and discussions, and prioritizes data privacy and security. This PRD
outlines the features, functionality, and specifications of the application.

## Website

[link](http://16.170.218.93:8081/login)

## Getting Started

### Requirements
- Java 19
- MySQL database
- Spring Boot
- Thymeleaf
- Postman

### Installation
1. Clone this repository to your local machine using git clone
    ```bash
    https://github.com/Dr-Dreams/PdfManagment.git
    ```
2. Navigate to the project directory:
    ```bash
    cd PdfManagment
    ```
3. Open the project in your IDE(Suggestion : - Intellij IDEA, VS-CODE)

    - [Intellij](https://www.jetbrains.com/idea/download/#section=mac)
    - [VS-CODE](https://code.visualstudio.com/download)

4. Open application.properties
    ```bash
   /src/main/resoucres
    ```
5. Set up your MySQL database and update the application.properties file with your database details
    ```
    spring.datasource.url=jdbc:mysql://localhost:3306/DataBaseName
    spring.datasource.username=user
    spring.datasource.password=pass
    spring.mail.username=enter_your_email_address
    spring.mail.password=your_password
    ```
6. Run the application
    ```
    mvn spring-boot:run
    ```
7. The application should now be accessible at
    ```
    http://localhost:8081/login
    or
    http://localhost:8081/register
## Features
- User Signup and Authentication
- File Upload
- Dashboard
- File Sharing
- Commenting
- Invited User File Access and Commenting
- Security and Data Privacy


### Built With
- Spring Boot - The web framework used
- Maven - Dependency Management
- MySQL - Database used
- OpenAPI - API documentation
### Reference 


For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/3.1.1/maven-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/3.1.1/maven-plugin/reference/html/#build-image)
* [Spring Security](https://docs.spring.io/spring-boot/docs/3.1.1/reference/htmlsingle/#web.security)
* [Spring Data JDBC](https://docs.spring.io/spring-boot/docs/3.1.1/reference/htmlsingle/#data.sql.jdbc)
* [Validation](https://docs.spring.io/spring-boot/docs/3.1.1/reference/htmlsingle/#io.validation)
* [Spring Web](https://docs.spring.io/spring-boot/docs/3.1.1/reference/htmlsingle/#web)
* [Spring Boot DevTools](https://docs.spring.io/spring-boot/docs/3.1.1/reference/htmlsingle/#using.devtools)
* [Thymeleaf](https://docs.spring.io/spring-boot/docs/3.1.1/reference/htmlsingle/#web.servlet.spring-mvc.template-engines)

### Guides

The following guides illustrate how to use some features concretely:

* [Securing a Web Application](https://spring.io/guides/gs/securing-web/)
* [Spring Boot and OAuth2](https://spring.io/guides/tutorials/spring-boot-oauth2/)
* [Authenticating a User with LDAP](https://spring.io/guides/gs/authenticating-ldap/)
* [Using Spring Data JDBC](https://github.com/spring-projects/spring-data-examples/tree/master/jdbc/basics)
* [Accessing data with MySQL](https://spring.io/guides/gs/accessing-data-mysql/)
* [Validation](https://spring.io/guides/gs/validating-form-input/)
* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/rest/)
* [Handling Form Submission](https://spring.io/guides/gs/handling-form-submission/)

