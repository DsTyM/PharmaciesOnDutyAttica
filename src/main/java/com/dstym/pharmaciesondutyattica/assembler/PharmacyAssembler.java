package com.dstym.pharmaciesondutyattica.assembler;

import com.dstym.pharmaciesondutyattica.controller.PharmacyRestController;
import com.dstym.pharmaciesondutyattica.entity.Pharmacy;
import com.dstym.pharmaciesondutyattica.service.PharmacyService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class PharmacyAssembler extends RepresentationModelAssemblerSupport<Pharmacy, Pharmacy> {
    public PharmacyAssembler() {
        super(PharmacyRestController.class, Pharmacy.class);
    }

    @Override
    public Pharmacy toModel(Pharmacy entity) {
        Pharmacy pharmacy = instantiateModel(entity);

        pharmacy.add(linkTo(
                methodOn(PharmacyRestController.class)
                        .getPharmacy(entity.getId()))
                .withSelfRel());

        pharmacy.setId(entity.getId());
        pharmacy.setRegion(entity.getRegion());
        pharmacy.setPhoneNumber(entity.getPhoneNumber());
        pharmacy.setAddress(entity.getAddress());
        pharmacy.setName(entity.getName());

        return pharmacy;
    }

    @Override
    public CollectionModel<Pharmacy> toCollectionModel(Iterable<? extends Pharmacy> entities) {
        CollectionModel<Pharmacy> pharmacyModel = super.toCollectionModel(entities);

        pharmacyModel.add(linkTo(methodOn(PharmacyService.class).findAll()).withSelfRel());

        return pharmacyModel;
    }
}
