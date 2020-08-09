package com.dstym.pharmaciesondutyattica.repository;

import com.dstym.pharmaciesondutyattica.entity.WorkingHour;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface WorkingHourRepository extends MongoRepository<WorkingHour, String> {

}
