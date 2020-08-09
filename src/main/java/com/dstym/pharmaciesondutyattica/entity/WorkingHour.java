package com.dstym.pharmaciesondutyattica.entity;

import io.swagger.annotations.ApiModel;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "WorkingHours")
@ApiModel(description = "Working Hour Information")
public class WorkingHour {
    @Id
    private String id;

    private String workingHourText;

    public WorkingHour() {

    }

    public WorkingHour(String id, String workingHourText) {
        this.id = id;
        this.workingHourText = workingHourText;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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
