package com.dstym.pharmaciesondutyattica.entity;

import io.swagger.annotations.ApiModel;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "AvailablePharmacies")
@ApiModel(description = "Available Pharmacy Information")
public class AvailablePharmacy {
    @Id
    private String id;

    private Pharmacy pharmacy;
    private WorkingHour workingHour;
    private String date;
    private int pulledVersion;

    public AvailablePharmacy() {

    }

    public AvailablePharmacy(int pulledVersion) {
        this.pulledVersion = pulledVersion;
    }

    public AvailablePharmacy(Pharmacy pharmacy, WorkingHour workingHour, String date, int pulledVersion) {
        this.pharmacy = pharmacy;
        this.workingHour = workingHour;
        this.date = date;
        this.pulledVersion = pulledVersion;
    }

    public AvailablePharmacy(String id, Pharmacy pharmacy, WorkingHour workingHour, String date, int pulledVersion) {
        this.id = id;
        this.pharmacy = pharmacy;
        this.workingHour = workingHour;
        this.date = date;
        this.pulledVersion = pulledVersion;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Pharmacy getPharmacy() {
        return this.pharmacy;
    }

    public void setPharmacy(Pharmacy pharmacy) {
        this.pharmacy = pharmacy;
    }

    public WorkingHour getWorkingHour() {
        return workingHour;
    }

    public void setWorkingHour(WorkingHour workingHour) {
        this.workingHour = workingHour;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getPulledVersion() {
        return pulledVersion;
    }

    public void setPulledVersion(int pulledVersion) {
        this.pulledVersion = pulledVersion;
    }

    @Override
    public String toString() {
        return "entity.AvailablePharmacy{" +
                "id=" + id +
                ", pharmacy=" + pharmacy +
                ", workingHour=" + workingHour +
                ", pulledVersion=" + pulledVersion +
                ", date=" + date +
                '}';
    }
}
