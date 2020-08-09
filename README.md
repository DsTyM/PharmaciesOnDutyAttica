# PharmaciesOnDutyAttica
Pharmacies on duty attica is a web application where it gets the pharmacies on duty from the official 
Pharmacists Association of Attica Website: [http://www.fsa.gr/duties.asp](http://www.fsa.gr/duties.asp)
and provide them as a REST API in JSON format.

It uses HtmlUnit to interact and crawl the fsa.gr website and jsoup to scrape the data 
(the available pharmacies per day, pharmacies' information and the different working hours available).

The application has been tested using JUnit and Mockito.

Here is an example of how it returns the pharmacies on duty:
```json
{
    "id": "5f2a839442441f2ef32bfbd1",
    "pharmacy": {
        "id": 3251,
        "name": "ΓΙΩΡΓΟΣ ΠΑΠΑΔΟΠΟΥΛΟΣ",
        "address": "ΠΛΑΤ.ΑΕΡΟΠΟΡΙΑΣ 153",
        "region": "ΗΛΙΟΥΠΟΛΗ",
        "phoneNumber": "210-1234567"
    },
    "workingHour": {
        "id": 7,
        "workingHourText": "8 ΠΡΩΙ - 11 ΒΡΑΔΥ"
    },
    "date": "10/1/2020",
    "pulledVersion": 2
}
```

It also contains a scheduler where it refreshes the available pharmacies for the given day 
when the application is started, and it also uses cron expressions to refresh the available pharmacies 
list twice per day. This can also be customised.
The application also uses Ehcache for In-Memory Caching.

The application can be accessed on:
```
http://localhost:8080
```
This page will redirect you to the Documentation of the API of the application. 
It uses Swagger UI to generate the API Documentation.


This project is just for learning purposes.
