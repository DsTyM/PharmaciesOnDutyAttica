package com.dstym.pharmaciesondutyattica.controller;

import com.dstym.pharmaciesondutyattica.entity.AvailablePharmacy;
import com.dstym.pharmaciesondutyattica.repository.AvailablePharmacyRepository;
import com.dstym.pharmaciesondutyattica.repository.PharmacyRepository;
import com.dstym.pharmaciesondutyattica.repository.WorkingHourRepository;
import com.dstym.pharmaciesondutyattica.scraper.AvailablePharmacies;
import com.dstym.pharmaciesondutyattica.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/get-data")
public class GetDataController {
    private WorkingHourRepository workingHourRepository;
    private PharmacyRepository pharmacyRepository;
    private AvailablePharmacyRepository availablePharmacyRepository;

    @Autowired
    public GetDataController(WorkingHourRepository workingHourRepository, PharmacyRepository pharmacyRepository,
                             AvailablePharmacyRepository availablePharmacyRepository) {
        this.workingHourRepository = workingHourRepository;
        this.pharmacyRepository = pharmacyRepository;
        this.availablePharmacyRepository = availablePharmacyRepository;
    }

    @GetMapping("/get-available-pharmacies")
    public String getAvailablePharmacies() {
        var daysFromToday = 0;
        var date = DateUtils.dateToString(DateUtils.getDateFromTodayPlusDays(daysFromToday));
        var workingHoursIdByPharmacyId = AvailablePharmacies.getAvailablePharmacyIdsAndWorkingHourIds(daysFromToday);
        AvailablePharmacy availablePharmacy;

        var result = availablePharmacyRepository.findFirstByDateOrderByPulledVersionDesc(date);

        int lastPulledVersion = 0;

        if (!result.isEmpty()) {
            var tempAvailablePharmacy = (AvailablePharmacy) result.toArray()[0];
            lastPulledVersion = tempAvailablePharmacy.getPulledVersion();
        }

        if (workingHoursIdByPharmacyId != null) {
            for (var pair : workingHoursIdByPharmacyId.keySet()) {
                int pharmacyId = pair;
                int workingHourId = workingHoursIdByPharmacyId.get(pair);

                availablePharmacy = new AvailablePharmacy();
                availablePharmacy.setId(0);
//                availablePharmacy.setPharmacyId(pharmacyId);
//                availablePharmacy.setWorkingHourId(workingHourId);
                availablePharmacy.setDate(date);
                availablePharmacy.setPulledVersion(lastPulledVersion + 1);

                System.out.println(availablePharmacy);
                availablePharmacyRepository.save(availablePharmacy);
            }
        }
        return "Operation Completed!";
    }

    // commented to not be triggered second time
//    @GetMapping("/get-pharmacies-info")
//    public String getPharmacies() throws IOException {
//
//        var listOfPharmacies = PharmaciesInfo.getPharmaciesInfo();
//        for (var pharmacy : listOfPharmacies) {
//            pharmacyRepository.save(pharmacy);
//        }
//
//        return "Operation Completed!";
//    }

    // commented to not be triggered second time
//    @GetMapping("/get-working-hours-info")
//    public String getWorkingHours() throws IOException {
//
//        var listOfWorkingHours = WorkingHoursInfo.getWorkingHoursInfo();
//        for (var workingHour : listOfWorkingHours) {
//            // if id found, update
//            // else add
//            workingHourRepository.save(workingHour);
//        }
//
//        return "Operation Completed!";
//    }
}
