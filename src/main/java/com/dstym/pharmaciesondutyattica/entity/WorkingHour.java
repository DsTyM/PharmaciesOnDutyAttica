package com.dstym.pharmaciesondutyattica.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "`working-hours`")
@Schema(description = "Working Hour Information")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkingHour implements Serializable {
    @Id
    @Column(name = "`id`")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "`working-hour-text`")
    private String workingHourText;
}
