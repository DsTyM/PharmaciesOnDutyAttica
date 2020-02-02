export class AvailablePharmacy {
  id: number;
  pharmacy: {
    id: number,
    name: string,
    address: string,
    region: string,
    phoneNumber: string
  };
  workingHour: {
    id: number,
    workingHourText: string
  };
  date: string;
  pulledVersion: number
}
