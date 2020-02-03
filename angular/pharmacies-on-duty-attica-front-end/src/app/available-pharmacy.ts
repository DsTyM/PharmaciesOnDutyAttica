import {Pharmacy} from "./pharmacy";
import {WorkingHour} from "./working-hour";

export class AvailablePharmacy {
  id: number;
  pharmacy: Pharmacy;
  workingHour: WorkingHour;
  date: string;
  pulledVersion: number;
}
