import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable, of} from 'rxjs';
import {map} from 'rxjs/operators';

const endpoint = 'http://localhost:8080/api/';
const httpOptions = {
  headers: new HttpHeaders({
    'Content-Type': 'application/json'
  })
};

@Injectable({
  providedIn: 'root'
})
export class RestService {

  constructor(private http: HttpClient) {
  }

  getAvailablePharmacies(): Observable<any> {
    return this.http.get(endpoint + 'available-pharmacies').pipe(
      map(RestService.extractData));
  }

  getPharmacy(id): Observable<any> {
    return this.http.get(endpoint + 'pharmacies/' + id).pipe(
      map(RestService.extractData));
  }

  private static extractData(res: Response) {
    return res || {};
  }

  private handleError<T>(operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {

      console.error(error);

      console.log(`${operation} failed: ${error.message}`);
      return of(result as T);
    };
  }
}
