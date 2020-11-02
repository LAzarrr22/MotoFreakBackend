package com.MJ.MotoFreaksBackend.MotoFreaksBackend.repository;

import com.MJ.MotoFreaksBackend.MotoFreaksBackend.db.collections.CarCompany;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CarCompanyRepository extends MongoRepository<CarCompany, String> {

    @Query("{'company': ?0}")
    Optional<CarCompany> findCarByCompany(String company);
}
