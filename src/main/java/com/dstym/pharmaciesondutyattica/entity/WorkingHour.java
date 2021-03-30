package com.dstym.pharmaciesondutyattica.entity;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "`working-hours`")
@Schema(description = "Working Hour Information")
public class WorkingHour implements Serializable {
    @Id
    @Column(name = "`id`")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "`working-hour-text`", nullable = false)
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WorkingHour)) return false;

        WorkingHour that = (WorkingHour) o;

        if (getId() != that.getId()) return false;
        return getWorkingHourText() != null ? getWorkingHourText().equals(that.getWorkingHourText()) : that.getWorkingHourText() == null;
    }

    @Override
    public int hashCode() {
        int result = getId();
        result = 31 * result + (getWorkingHourText() != null ? getWorkingHourText().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "WorkingHour{" +
                "id=" + id +
                ", workingHourText='" + workingHourText + '\'' +
                '}';
    }
}
