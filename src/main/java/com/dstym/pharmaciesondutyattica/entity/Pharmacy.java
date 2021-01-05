package com.dstym.pharmaciesondutyattica.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
