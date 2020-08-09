package com.dstym.pharmaciesondutyattica.entity;

import io.swagger.annotations.ApiModel;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Pharmacies")
@ApiModel(description = "Pharmacy Information")
public class Pharmacy {
    @Id
    private String id;

    private String name;
    private String address;
    private String region;
    private String phoneNumber;

    public Pharmacy() {

    }

    public Pharmacy(String id, String name, String address, String region, String phoneNumber) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.region = region;
        this.phoneNumber = phoneNumber;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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
