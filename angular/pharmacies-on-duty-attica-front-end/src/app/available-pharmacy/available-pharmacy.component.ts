import {Component, OnInit} from '@angular/core';
import {RestService} from "../rest.service";

@Component({
  selector: 'app-available-pharmacy',
  templateUrl: './available-pharmacy.component.html',
  styleUrls: ['./available-pharmacy.component.css']
})
export class AvailablePharmacyComponent implements OnInit {
  availablePharmacies: any = [];
  isFetching = false;

  constructor(public rest: RestService) {
  }

  ngOnInit() {
    this.getAvailablePharmacies();
  }

  getAvailablePharmacies() {
    this.availablePharmacies = [];
    this.isFetching = true;
    this.rest.getAvailablePharmacies().subscribe((data: {}) => {
      console.log(data);
      this.availablePharmacies = data;
      this.isFetching = false;
    });
  }
}
