import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        // 1. Get all available PharmacyIds with WorkingHourIds

        var workingHoursIdByPharmacyId = AvailablePharmacies.getAvailablePharmacyIdsAndWorkingHourIds(0);

        if (workingHoursIdByPharmacyId != null)
            workingHoursIdByPharmacyId
                    .forEach((pharmacyId, workingHourId) -> System.out.println(pharmacyId + "\t" + workingHourId));

        // 2. Get all WorkingHours (instances)

//        var listOfWorkingHours = WorkingHoursInfo.getWorkingHoursInfo();
//
//        for (var workingHour : listOfWorkingHours) {
//            System.out.println(workingHour);
//        }

        // 3. Get all Pharmacies (instances)

//        var listOfPharmacies = PharmaciesInfo.getPharmaciesInfo();
//
//        for (var pharmacy : listOfPharmacies) {
//            System.out.println(pharmacy);
//        }
    }
}
