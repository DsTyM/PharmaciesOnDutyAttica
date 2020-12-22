package com.dstym.pharmaciesondutyattica.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "`available-pharmacies`")
@Schema(description = "Available Pharmacy Information")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AvailablePharmacy implements Serializable {
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

    public AvailablePharmacy(int pulledVersion) {
        this.pulledVersion = pulledVersion;
    }
}
