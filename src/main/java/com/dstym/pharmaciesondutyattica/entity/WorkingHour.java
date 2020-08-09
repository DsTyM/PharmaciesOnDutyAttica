package com.dstym.pharmaciesondutyattica.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "`working-hours`")
@Schema(description = "Working Hour Information")
@Relation(collectionRelation = "working-hours", itemRelation = "working-hour")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WorkingHour extends RepresentationModel<WorkingHour> implements Serializable {
    @Id
    @Column(name = "`id`")
    private int id;

    @Column(name = "`working-hour-text`")
    private String workingHourText;

    public WorkingHour() {

    }

    public WorkingHour(int id, String workingHourText) {
        this.id = id;
        this.workingHourText = workingHourText;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWorkingHourText() {
        return workingHourText;
    }

    public void setWorkingHourText(String workingHourText) {
        this.workingHourText = workingHourText;
    }

    @Override
    public String toString() {
        return "entity.WorkingHour{" +
                "id=" + id +
                ", workingHourText='" + workingHourText + '\'' +
                '}';
    }
}
