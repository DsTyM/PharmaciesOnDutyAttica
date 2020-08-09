# PharmaciesOnDutyAttica
Pharmacies on duty attica is a web application where it gets the pharmacies on duty from the official 
Pharmacists Association of Attica Website: [http://www.fsa.gr/duties.asp](http://www.fsa.gr/duties.asp)
and provide them as a REST API in JSON format.

It uses HtmlUnit to interact and crawl the fsa.gr website and jsoup to scrape the data 
(the available pharmacies per day, pharmacies' information and the different working hours available).
It also uses MySQL to save the data on the server and Spring Boot to provide the Rest API.

The API is HATEOAS compliant.

The application has been tested using JUnit and Mockito.

Here is an example of how it returns the pharmacies on duty:
```json
{
    "_embedded": {
        "available-pharmacies": [
            {
                "id": 866,
                "pharmacy": {
                    "id": 3251,
                    "name": "ΓΙΩΡΓΟΣ ΠΑΠΑΔΟΠΟΥΛΟΣ",
                    "address": "ΠΛΑΤ.ΑΕΡΟΠΟΡΙΑΣ 153",
                    "region": "ΗΛΙΟΥΠΟΛΗ",
                    "phoneNumber": "210-1234567",
                    "_links": {
                        "self": {
                            "href": "http://localhost:8080/api/pharmacies/3251"
                        }
                    }
                },
                "workingHour": {
                    "id": 3,
                    "workingHourText": "8 ΠΡΩΙ - 2 ΜΕΣΗΜΕΡΙ & 5 ΑΠΟΓΕΥΜΑ ΕΩΣ 8 ΠΡΩΙ ΕΠΟΜΕΝΗΣ",
                    "_links": {
                        "self": {
                            "href": "http://localhost:8080/api/working-hours/3"
                        }
                    }
                },
                "date": "9/8/2020",
                "pulledVersion": 2
            },
            {
                "id": 15114,
                "pharmacy": {
                    "id": 1346,
                    "name": "ΜΑΡΙΝΑ ΒΙΒΙΕΛΣΟΠΟΥΛΟΥ",
                    "address": "ΠΛΑΤΑΡΑ 32",
                    "region": "ΧΑΛΑΝΔΡΙ",
                    "phoneNumber": "210-6519166",
                    "_links": {
                        "self": {
                            "href": "http://localhost:8080/api/pharmacies/1346"
                        }
                    }
                },
                "workingHour": {
                    "id": 2,
                    "workingHourText": "ΑΝΟΙΧΤΑ 8 ΠΡΩΙ - 2 ΜΕΣΗΜΕΡΙ & 5 ΑΠΟΓΕΥΜΑ - 8 ΠΡΩΙ ΕΠΟΜΕΝΗΣ",
                    "_links": {
                        "self": {
                            "href": "http://localhost:8080/api/working-hours/2"
                        }
                    }
                },
                "date": "9/8/2020",
                "pulledVersion": 2
            }
        ]
    },
   "_links": {
           "self": {
               "href": "http://localhost:8080/api/available-pharmacies?page=0&size=200"
           }
    },
    "page": {
        "size": 200,
        "totalElements": 110,
        "totalPages": 1,
        "number": 0
    }
}
```

It also contains a scheduler where it refreshes the available pharmacies for the given day 
when the application is started, and it also uses cron expressions to refresh the available pharmacies 
list twice per day. This can also be customised.
The application also uses Ehcache for In-Memory Caching.

The final version of the API may change.

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
