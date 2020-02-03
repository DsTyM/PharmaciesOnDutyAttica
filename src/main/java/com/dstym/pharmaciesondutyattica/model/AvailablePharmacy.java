package com.dstym.pharmaciesondutyattica.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

@Entity
@Table(name = "`available-pharmacies`")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
public class AvailablePharmacy implements Serializable {
    @Id
    @Column(name = "`id`")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, cascade = {CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "`pharmacy-id`", nullable = false)
    private Pharmacy pharmacy;

    @ManyToOne(optional = false, cascade = {CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "`working-hour-id`", nullable = false)
    private WorkingHour workingHour;

    @Column(name = "`date`", nullable = false)
    private Instant date;

    @Column(name = "`pulled-version`", nullable = false)
    private Integer pulledVersion;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        AvailablePharmacy that = (AvailablePharmacy) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
