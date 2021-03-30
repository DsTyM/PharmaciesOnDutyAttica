package com.dstym.pharmaciesondutyattica.entity;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "`pharmacies`")
@Schema(description = "Pharmacy Information")
public class Pharmacy implements Serializable {
    @Id
    @Column(name = "`id`")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "`name`", nullable = false)
    private String name;

    @Column(name = "`address`", nullable = false)
    private String address;

    @Column(name = "`region`", nullable = false)
    private String region;

    @Column(name = "`phone-number`")
    private String phoneNumber;

    public Pharmacy() {
    }

    public Pharmacy(int id, String name, String address, String region, String phoneNumber) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.region = region;
        this.phoneNumber = phoneNumber;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pharmacy)) return false;

        Pharmacy pharmacy = (Pharmacy) o;

        if (getId() != pharmacy.getId()) return false;
        if (getName() != null ? !getName().equals(pharmacy.getName()) : pharmacy.getName() != null) return false;
        if (getAddress() != null ? !getAddress().equals(pharmacy.getAddress()) : pharmacy.getAddress() != null)
            return false;
        if (getRegion() != null ? !getRegion().equals(pharmacy.getRegion()) : pharmacy.getRegion() != null)
            return false;
        return getPhoneNumber() != null ? getPhoneNumber().equals(pharmacy.getPhoneNumber()) : pharmacy.getPhoneNumber() == null;
    }

    @Override
    public int hashCode() {
        int result = getId();
        result = 31 * result + (getName() != null ? getName().hashCode() : 0);
        result = 31 * result + (getAddress() != null ? getAddress().hashCode() : 0);
        result = 31 * result + (getRegion() != null ? getRegion().hashCode() : 0);
        result = 31 * result + (getPhoneNumber() != null ? getPhoneNumber().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Pharmacy{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", region='" + region + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }
}
