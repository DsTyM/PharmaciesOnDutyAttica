package com.dstym.pharmaciesondutyattica;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.TimeZone;

@SpringBootApplication
public class PharmaciesOnDutyAtticaApplication {
    public static void main(String[] args) {
        SpringApplication.run(PharmaciesOnDutyAtticaApplication.class, args);
    }

    @PostConstruct
    public void init() {
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/Athens"));
    }
}
