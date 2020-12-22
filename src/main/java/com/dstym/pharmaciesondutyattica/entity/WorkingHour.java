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
@Table(name = "`working-hours`")
@Schema(description = "Working Hour Information")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkingHour implements Serializable {
    @Id
    @Column(name = "`id`")
    private int id;

    @Column(name = "`working-hour-text`")
    private String workingHourText;
}
