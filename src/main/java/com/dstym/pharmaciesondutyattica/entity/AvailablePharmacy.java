package com.dstym.pharmaciesondutyattica.entity;

import javax.persistence.*;

@Entity
@Table(name = "`available-pharmacies`")
public class AvailablePharmacy {
    @Id
    @Column(name = "`id`")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(optional = false,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "`pharmacy-id`")
    private Pharmacy pharmacy;

//    @Column(name = "`pharmacy-id`")
//    private int pharmacyId;

//    @Column(name = "`working-hour-id`")
//    private int workingHourId;

    public WorkingHour getWorkingHour() {
        return workingHour;
    }

    public void setWorkingHour(WorkingHour workingHour) {
        this.workingHour = workingHour;
    }

    @ManyToOne(optional = false,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "`working-hour-id`")
    private WorkingHour workingHour;

    @Column(name = "`date`")
    private String date;

    @Column(name = "`pulled-version`")
    private int pulledVersion;

    public AvailablePharmacy() {

    }

    public AvailablePharmacy(int id, int pharmacyId, int workingHourId, String date, int pulledVersion) {
        this.id = id;
//        this.pharmacyId = pharmacyId;
//        this.workingHourId = workingHourId;
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

//    public void add(Pharmacy pharmacy) {
//        if(pharmacies == null) {
//            pharmacies = new ArrayList<>();
//        }
//
//        pharmacies.add(pharmacy);
//    }

//    public int getPharmacyId() {
//        return pharmacyId;
//    }
//
//    public void setPharmacyId(int pharmacyId) {
//        this.pharmacyId = pharmacyId;
//    }

//    public int getWorkingHourId() {
//        return workingHourId;
//    }
//
//    public void setWorkingHourId(int workingHourId) {
//        this.workingHourId = workingHourId;
//    }

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
//                ", pharmacyId=" + pharmacyId +
                ", pharmacy=" + pharmacy +
//                ", workingHourId=" + workingHourId +
                ", workingHour=" + workingHour +
                ", pulledVersion=" + pulledVersion +
                ", date=" + date +
                '}';
    }
}
