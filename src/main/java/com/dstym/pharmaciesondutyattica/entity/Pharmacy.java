package com.dstym.pharmaciesondutyattica.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.hibernate.Hibernate;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "`pharmacies`")
@Schema(description = "Pharmacy Information")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
public class Pharmacy implements Serializable {
    @Id
    @Column(name = "`id`")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "`name`", nullable = false)
    private String name;

    @Column(name = "`address`", nullable = false)
    private String address;

    @Column(name = "`region`", nullable = false)
    private String region;

    @Column(name = "`phone-number`")
    private String phoneNumber;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Pharmacy pharmacy = (Pharmacy) o;
        return id != null && Objects.equals(id, pharmacy.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
