package com.dstym.pharmaciesondutyattica.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "`pharmacies`")
@Schema(description = "Pharmacy Information")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Pharmacy implements Serializable {
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
}
