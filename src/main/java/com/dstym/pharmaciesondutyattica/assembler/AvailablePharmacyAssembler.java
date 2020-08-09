package com.dstym.pharmaciesondutyattica.assembler;

import com.dstym.pharmaciesondutyattica.controller.AvailablePharmacyRestController;
import com.dstym.pharmaciesondutyattica.controller.PharmacyRestController;
import com.dstym.pharmaciesondutyattica.controller.WorkingHourRestController;
import com.dstym.pharmaciesondutyattica.entity.AvailablePharmacy;
import com.dstym.pharmaciesondutyattica.entity.Pharmacy;
import com.dstym.pharmaciesondutyattica.entity.WorkingHour;
import com.dstym.pharmaciesondutyattica.service.AvailablePharmacyService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class AvailablePharmacyAssembler extends RepresentationModelAssemblerSupport<AvailablePharmacy, AvailablePharmacy> {
    public AvailablePharmacyAssembler() {
        super(AvailablePharmacyRestController.class, AvailablePharmacy.class);
    }

    @Override
    public AvailablePharmacy toModel(AvailablePharmacy entity) {
        AvailablePharmacy availablePharmacy = instantiateModel(entity);

        availablePharmacy.setId(entity.getId());
        availablePharmacy.setPulledVersion(entity.getPulledVersion());
        availablePharmacy.setDate(entity.getDate());
        availablePharmacy.setPharmacy(toPharmacyModel(Collections.singletonList(entity.getPharmacy())).get(0));
        availablePharmacy.setWorkingHour(toWorkingHourModel(Collections.singletonList(entity.getWorkingHour())).get(0));

        return availablePharmacy;
    }

    @Override
    public CollectionModel<AvailablePharmacy> toCollectionModel(Iterable<? extends AvailablePharmacy> entities) {
        CollectionModel<AvailablePharmacy> availablePharmacyModel = super.toCollectionModel(entities);

        availablePharmacyModel.add(linkTo(methodOn(AvailablePharmacyService.class).findAll()).withSelfRel());

        return availablePharmacyModel;
    }

    private List<Pharmacy> toPharmacyModel(List<Pharmacy> pharmacies) {
        if (pharmacies.isEmpty())
            return Collections.emptyList();

        return pharmacies.stream()
                .map(pharmacy -> new Pharmacy(
                        pharmacy.getId(),
                        pharmacy.getName(),
                        pharmacy.getAddress(),
                        pharmacy.getRegion(),
                        pharmacy.getPhoneNumber()
                ).add(linkTo(
                        methodOn(PharmacyRestController.class)
                                .getPharmacy(pharmacy.getId()))
                        .withSelfRel()))
                .collect(Collectors.toList());
    }

    private List<WorkingHour> toWorkingHourModel(List<WorkingHour> workingHours) {
        if (workingHours.isEmpty())
            return Collections.emptyList();

        return workingHours.stream()
                .map(workingHour -> new WorkingHour(
                        workingHour.getId(),
                        workingHour.getWorkingHourText()

                ).add(linkTo(
                        methodOn(WorkingHourRestController.class)
                                .getWorkingHour(workingHour.getId()))
                        .withSelfRel()))
                .collect(Collectors.toList());
    }
}
