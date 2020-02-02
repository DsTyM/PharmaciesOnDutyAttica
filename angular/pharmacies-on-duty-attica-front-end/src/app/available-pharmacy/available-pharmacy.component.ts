import {Component, OnInit} from '@angular/core';
import {RestService} from "../rest.service";
import {AvailablePharmacy} from "../available-pharmacy";

@Component({
  selector: 'app-available-pharmacy',
  templateUrl: './available-pharmacy.component.html',
  styleUrls: ['./available-pharmacy.component.css']
})
export class AvailablePharmacyComponent implements OnInit {
  availablePharmacies: [AvailablePharmacy];
  isFetching = false;

  constructor(public rest: RestService) {
  }

  ngOnInit() {
    this.getAvailablePharmacies();
  }

  getAvailablePharmacies() {
    this.availablePharmacies = [new AvailablePharmacy()];
    this.isFetching = true;
    this.rest.getAvailablePharmacies().subscribe((data: [AvailablePharmacy]) => {
      console.log(data);
      this.availablePharmacies = data;
      this.isFetching = false;
    });
  }
}
