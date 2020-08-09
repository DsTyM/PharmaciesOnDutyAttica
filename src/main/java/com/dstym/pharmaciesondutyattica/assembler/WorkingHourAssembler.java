package com.dstym.pharmaciesondutyattica.assembler;

import com.dstym.pharmaciesondutyattica.controller.WorkingHourRestController;
import com.dstym.pharmaciesondutyattica.entity.WorkingHour;
import com.dstym.pharmaciesondutyattica.service.WorkingHourService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class WorkingHourAssembler extends RepresentationModelAssemblerSupport<WorkingHour, WorkingHour> {
    public WorkingHourAssembler() {
        super(WorkingHourRestController.class, WorkingHour.class);
    }

    @Override
    public WorkingHour toModel(WorkingHour entity) {
        WorkingHour workingHour = instantiateModel(entity);

        workingHour.add(linkTo(
                methodOn(WorkingHourRestController.class)
                        .getWorkingHour(entity.getId()))
                .withSelfRel());

        workingHour.setId(entity.getId());
        workingHour.setWorkingHourText(entity.getWorkingHourText());

        return workingHour;
    }

    @Override
    public CollectionModel<WorkingHour> toCollectionModel(Iterable<? extends WorkingHour> entities) {
        CollectionModel<WorkingHour> workingHourModel = super.toCollectionModel(entities);

        workingHourModel.add(linkTo(methodOn(WorkingHourService.class).findAll()).withSelfRel());

        return workingHourModel;
    }
}
