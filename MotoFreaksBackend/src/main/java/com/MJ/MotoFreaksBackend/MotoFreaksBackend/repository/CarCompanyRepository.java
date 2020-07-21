package com.MJ.MotoFreaksBackend.MotoFreaksBackend.repository;

import com.MJ.MotoFreaksBackend.MotoFreaksBackend.db.collections.CarCompany;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarCompanyRepository extends MongoRepository<CarCompany, String> {
}
