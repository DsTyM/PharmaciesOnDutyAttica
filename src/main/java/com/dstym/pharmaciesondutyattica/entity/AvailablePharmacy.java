package com.dstym.pharmaciesondutyattica.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "available-pharmacies")
public class AvailablePharmacy {
    @Id
    @Column(name = "id")
    private long id;

    @Column(name = "pharmacy-id")
    private int pharmacyId;

    @Column(name = "working-hour-id")
    private int workingHourId;

    @Column(name = "date")
    private String date;

    @Column(name = "pulled-version")
    private int pulledVersion;

    public AvailablePharmacy() {

    }

    public AvailablePharmacy(int id, int pharmacyId, int workingHourId, String date, int pulledVersion) {
        this.id = id;
        this.pharmacyId = pharmacyId;
        this.workingHourId = workingHourId;
        this.date = date;
        this.pulledVersion = pulledVersion;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getPharmacyId() {
        return pharmacyId;
    }

    public void setPharmacyId(int pharmacyId) {
        this.pharmacyId = pharmacyId;
    }

    public int getWorkingHourId() {
        return workingHourId;
    }

    public void setWorkingHourId(int workingHourId) {
        this.workingHourId = workingHourId;
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
                ", pharmacyId=" + pharmacyId +
                ", workingHourId=" + workingHourId +
                ", date=" + date +
                '}';
    }
}
