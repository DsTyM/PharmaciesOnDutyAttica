# PharmaciesOnDutyAttica
Pharmacies on duty attica is a web application where it gets the pharmacies on duty from the official 
Pharmacists Association of Attica Website: [http://www.fsa.gr/duties.asp](http://www.fsa.gr/duties.asp)
and provide them as a REST API in JSON format.

It uses HtmlUnit to interact and crawl the fsa.gr website and jsoup to scrape the data 
(the available pharmacies per day, pharmacies' information and the different working hours available).
It also uses MySQL to save the data on the server and Spring Boot to provide the Rest API.

The application has been tested using JUnit and Mockito.

Here is an example of how it returns the pharmacies on duty:
```json
{
  "content": [
    {
      "id": 1055,
      "pharmacy": {
        "id": 3251,
        "name": "ΓΙΩΡΓΟΣ ΠΑΠΑΔΟΠΟΥΛΟΣ",
        "address": "ΠΛΑΤ.ΑΕΡΟΠΟΡΙΑΣ 153",
        "region": "ΗΛΙΟΥΠΟΛΗ",
        "phoneNumber": "210-1234567"
      },
      "workingHour": {
        "id": 1,
        "workingHourText": "ΑΝΟΙΧΤΑ 8 ΠΡΩΙ ΕΩΣ 8 ΒΡΑΔΥ"
      },
      "date": "31/10/2020",
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

It also contains a scheduler where it refreshes the available pharmacies for the given day 
when the application is started, and it also uses cron expressions to refresh the available pharmacies 
list twice per day. This can also be customised.
The application also uses Ehcache for In-Memory Caching.

To start the project using docker, just run:
```
$ git clone https://github.com/DsTyM/PharmaciesOnDutyAttica.git
$ cd PharmaciesOnDutyAttica
$ docker-compose up --build
```

The application can be accessed on:
```
http://localhost:8080
```
This page will redirect you to the Documentation of the API of the application. 
It uses Swagger UI to generate the API Documentation.


Of course, when you finish, don't forget to run:
```
$ docker-compose down
```

The application uses Liquibase for Database Versioning. 
To generate a new Liquibase changelog from the entities, run:
```
$ mvnw package -DskipTests
$ mvnw liquibase:diff
```

If you are using Docker and Git on Windows, then 
an error like this (or similar) may occur:
```
/bin/sh not found
```
To fix this error, just run the following command on Git Bash:
```
$ git config --global core.autocrlf false
```
and clone the project again.

In case you mess with the Liquibase changelog, and the application won't start with Docker,
you've most probably messed with the database volume. To remove it, run:
```
$ docker volume rm pharmaciesondutyattica_db-data
```

This project is just for learning purposes.
