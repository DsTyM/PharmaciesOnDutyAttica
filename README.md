# Pharmacies on Duty Attica

Pharmacies on duty attica is a Java / Spring Boot web application where it gets the pharmacies on duty from the official
Pharmacists Association of Attica Website: [https://fsa-efimeries.gr](https://fsa-efimeries.gr)
and provide them as a REST API in JSON format. You can get the pharmacies on duty for any number of days you want, e.g.
for the whole next week, as long as Pharmacists Association of Attica provides them.

It uses HtmlUnit to interact and crawl the "fsa.gr" website and jsoup to scrape the data
(the available pharmacies per day, pharmacies' information and the different working hours available). It also uses
MySQL to save the data on the server and Spring Boot to provide the Rest API.

The application has been tested using JUnit and Mockito.

Here is an example of how it returns the pharmacies on duty:

```json
{
  "content": [
    {
      "id": 251,
      "pharmacy": {
        "id": 564,
        "name": "ΓΙΩΡΓΟΣ ΠΑΠΑΔΟΠΟΥΛΟΣ",
        "address": "ΠΛΑΤ.ΑΕΡΟΠΟΡΙΑΣ 153",
        "region": "ΗΛΙΟΥΠΟΛΗ",
        "phoneNumber": "210-1234567"
      },
      "workingHour": {
        "id": 1,
        "workingHourText": "ΑΝΟΙΧΤΑ 8 ΠΡΩΙ ΕΩΣ 8 ΒΡΑΔΥ"
      },
      "date": "2021-01-06T00:00:00Z",
      "pulledVersion": 2
    }
  ],
  "pageable": {
    "sort": {
      "sorted": false,
      "unsorted": true,
      "empty": true
    },
    "offset": 0,
    "pageSize": 30,
    "pageNumber": 0,
    "unpaged": false,
    "paged": true
  },
  "totalPages": 4,
  "totalElements": 106,
  "last": false,
  "size": 30,
  "number": 0,
  "sort": {
    "sorted": false,
    "unsorted": true,
    "empty": true
  },
  "numberOfElements": 30,
  "first": true,
  "empty": false
}
```

It also contains a scheduler where it refreshes the available pharmacies for the given day when the application is
started, and it also uses cron expressions to refresh the available pharmacies list many times per day. This can also be
customised. The application also uses Ehcache for In-Memory Caching.

To clone the project, just run:

```shell
git clone https://github.com/DsTyM/PharmaciesOnDutyAttica.git
cd PharmaciesOnDutyAttica
```

To start the project using docker, just run:

```shell
docker-compose up --build
```

The application can be accessed on:

```
http://localhost:8080
```

This page will redirect you to the Documentation of the API of the application. It uses Swagger UI to generate the API
Documentation.

Of course, when you finish, don't forget to run:

```shell
docker-compose down
```

The application uses Liquibase for Database Versioning. To generate a new Liquibase changelog from the entities, run:

```shell
mvnw clean package -U -DskipTests
mvnw liquibase:diff
```

If you try to generate a new Liquibase changelog there is a chance that some warning / errors to appear
but the file to have been created successfully so try to check if the liquibase file is empty or populated. 
If it is populated with SQL queries / statements then it means that it was created successfully. 
The liquibase file is located at:

```
src/main/resources/liquibase/liquibase-changeLog.mysql.sql
```

If you are using Docker and Git on Windows, then the following error may occur:

```
/bin/sh not found
```

To fix this error, just run the following command on Git Bash:

```shell
git config --global core.autocrlf false
```

and clone the project again.

In case you mess with the Liquibase changelog, and the application won't start with Docker, you've most probably messed
with the database volume. To remove it, run:

```shell
docker volume rm pharmaciesondutyattica_db-data
```
