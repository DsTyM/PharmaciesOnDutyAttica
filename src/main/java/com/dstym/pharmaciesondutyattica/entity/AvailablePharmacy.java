package com.dstym.pharmaciesondutyattica.entity;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;

@Entity
@Table(name = "`available-pharmacies`")
@Schema(description = "Available Pharmacy Information")
public class AvailablePharmacy implements Serializable {
    @Id
    @Column(name = "`id`")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(optional = false, cascade = {CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "`pharmacy-id`", nullable = false)
    private Pharmacy pharmacy;

    @ManyToOne(optional = false, cascade = {CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "`working-hour-id`", nullable = false)
    private WorkingHour workingHour;

    @Column(name = "`date`", nullable = false)
    private Instant date;

    @Column(name = "`pulled-version`", nullable = false)
    private int pulledVersion;

    public AvailablePharmacy() {
    }

    public AvailablePharmacy(int pulledVersion) {
        setPulledVersion(pulledVersion);
    }

    public AvailablePharmacy(long id, Pharmacy pharmacy, WorkingHour workingHour, Instant date, int pulledVersion) {
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
        return pharmacy;
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

    public Instant getDate() {
        return date;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public int getPulledVersion() {
        return pulledVersion;
    }

    public void setPulledVersion(int pulledVersion) {
        this.pulledVersion = pulledVersion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AvailablePharmacy)) return false;

        AvailablePharmacy that = (AvailablePharmacy) o;

        if (getId() != that.getId()) return false;
        if (getPulledVersion() != that.getPulledVersion()) return false;
        if (getPharmacy() != null ? !getPharmacy().equals(that.getPharmacy()) : that.getPharmacy() != null)
            return false;
        if (getWorkingHour() != null ? !getWorkingHour().equals(that.getWorkingHour()) : that.getWorkingHour() != null)
            return false;
        return getDate() != null ? getDate().equals(that.getDate()) : that.getDate() == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (getId() ^ (getId() >>> 32));
        result = 31 * result + (getPharmacy() != null ? getPharmacy().hashCode() : 0);
        result = 31 * result + (getWorkingHour() != null ? getWorkingHour().hashCode() : 0);
        result = 31 * result + (getDate() != null ? getDate().hashCode() : 0);
        result = 31 * result + getPulledVersion();
        return result;
    }

    @Override
    public String toString() {
        return "AvailablePharmacy{" +
                "id=" + id +
                ", pharmacy=" + pharmacy +
                ", workingHour=" + workingHour +
                ", date=" + date +
                ", pulledVersion=" + pulledVersion +
                '}';
    }
}
