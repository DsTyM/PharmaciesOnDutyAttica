package com.dstym.pharmaciesondutyattica;

import org.springframework.boot.SpringApplication;

public class TestPharmaciesOnDutyAtticaApplication {
    public static void main(String[] args) {
        SpringApplication.from(PharmaciesOnDutyAtticaApplication::main).with(TestcontainersConfiguration.class).run(args);
    }
}
