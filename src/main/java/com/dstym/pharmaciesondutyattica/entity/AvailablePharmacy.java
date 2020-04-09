package com.dstym.pharmaciesondutyattica.entity;

import io.swagger.annotations.ApiModel;

import javax.persistence.*;

@Entity
@Table(name = "`available-pharmacies`")
@ApiModel(description = "Available Pharmacy Information")
public class AvailablePharmacy {
    @Id
    @Column(name = "`id`")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(optional = false,
            cascade = {CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "`pharmacy-id`")
    private Pharmacy pharmacy;

    @ManyToOne(optional = false,
            cascade = {CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "`working-hour-id`")
    private WorkingHour workingHour;

    @Column(name = "`date`")
    private String date;

    @Column(name = "`pulled-version`")
    private int pulledVersion;

    public AvailablePharmacy() {

    }

    public AvailablePharmacy(int pulledVersion) {
        this.pulledVersion = pulledVersion;
    }

    public AvailablePharmacy(int id, Pharmacy pharmacy, WorkingHour workingHour, String date, int pulledVersion) {
        this.id = id;
        this.pharmacy = pharmacy;
        this.workingHour = workingHour;
        this.date = date;
        this.pulledVersion = pulledVersion;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
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
