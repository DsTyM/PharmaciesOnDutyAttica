# PharmaciesOnDutyAttica
Pharmacies on duty attica is a web application where it gets the pharmacies on duty from the official 
Pharmacists Association of Attica Website: [http://www.fsa.gr/duties.asp](http://www.fsa.gr/duties.asp)
and provide them as a REST API in JSON format.

It uses HtmlUnit to interact and crawl the fsa.gr website and jsoup to scrape the data 
(the available pharmacies per day, pharmacies' information and the different working hours available).
It also uses MySQL to save the data on the server and Spring Boot to provide the Rest API.

Here is an example of how it returns the pharmacies on duty:
```json
{
    "id": 866,
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
It can also search for a specific date or for a specific region.

It also contains a scheduler where it refresh the available pharmacies for the given day 
when the application is started and it also use cron expressions to refresh the available pharmacies 
list twice per day. This can also be customised.

The final version of the API may change.

What's next:
- Unit Tests (with JUnit and Mockito)
- API Documentation (probably with Swagger)
- Front End in Angular
- Docker Support

This project is just for learning purposes.
