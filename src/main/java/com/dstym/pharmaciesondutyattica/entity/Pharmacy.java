package com.dstym.pharmaciesondutyattica.entity;

import io.swagger.annotations.ApiModel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "`pharmacies`")
@ApiModel(description = "Pharmacy Information")
public class Pharmacy {
    @Id
    @Column(name = "`id`")
    private int id;

    @Column(name = "`name`")
    private String name;

    @Column(name = "`address`")
    private String address;

    @Column(name = "`region`")
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
    public String toString() {
        return "entity.Pharmacy{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", region='" + region + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }
}
