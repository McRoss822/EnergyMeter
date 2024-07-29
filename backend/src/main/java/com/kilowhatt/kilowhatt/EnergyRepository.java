package com.kilowhatt.kilowhatt;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnergyRepository extends MongoRepository<EnergyMeter, ObjectId> {

}
