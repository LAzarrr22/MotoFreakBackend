package com.MJ.MotoFreaksBackend.MotoFreaksBackend.services;

import com.MJ.MotoFreaksBackend.MotoFreaksBackend.db.collections.CarCompany;
import com.MJ.MotoFreaksBackend.MotoFreaksBackend.repository.CarCompanyRepository;
import com.MJ.MotoFreaksBackend.MotoFreaksBackend.resource.requests.NewCarCompany;
import com.google.common.collect.Ordering;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

import static org.springframework.http.ResponseEntity.ok;

@Service
@Slf4j
public class CarsService {

    private final CarCompanyRepository carRepository;
    private final UserService userService;

    @Autowired
    public CarsService(CarCompanyRepository carRepository, UserService userService) {
        this.carRepository = carRepository;
        this.userService = userService;
    }


    public Object mergeCarModel(String token, NewCarCompany newCars) {
        Map<Object, Object> responseModel = new HashMap<>();
        try {
            CarCompany carCompanyExists = getCompanyByName(newCars.getCompany());
            newCars.getModelList().keySet().forEach(key -> {

                carCompanyExists.getModelList().put(key
                        , mergeGenerationList(carCompanyExists.getModelList().get(key), newCars.getModelList().get(key)));
            });
            carCompanyExists.setUpdatedDate(new Date());
            responseModel.put("message", "Company " + carCompanyExists.getCompany() + " was updated.");
            log.info("Company " + carCompanyExists.getCompany() + " was updated.");
            this.carRepository.save(carCompanyExists);
        } catch (ResponseStatusException e) {
            CarCompany carCompany = new CarCompany(newCars.getCompany(), newCars.getModelList());
            carCompany.setCreatedDate(new Date());
            carCompany.setCreatorId(userService.getUserByToken(token).getId());
            responseModel.put("message", "Company " + carCompany.getCompany() + " was added.");
            log.info("Company " + carCompany.getCompany() + " was added.");
            this.carRepository.save(carCompany);
        }
        return ok(responseModel);
    }

    private List<String> mergeGenerationList(List<String> exists, List<String> newList) {
        newList.removeAll(exists);
        newList.addAll(exists);
        return newList;
    }

    public Object deleteCompany(String company) {
        Map<Object, Object> responseModel = new HashMap<>();
        CarCompany carCompanyExists = getCompanyByName(company);
        this.carRepository.delete(carCompanyExists);
        responseModel.put("message", "Company " + company + " was removed.");
        log.info("Company " + company + " was removed.");
        return ok(responseModel);
    }

    public Object deleteModel(String company, String model) {
        Map<Object, Object> responseModel = new HashMap<>();
        CarCompany carCompanyExists = getCompanyByName(company);
        carCompanyExists.getModelList().remove(model);
        this.carRepository.save(carCompanyExists);
        responseModel.put("message", "Model " + model + " from " + company + " was removed.");
        log.info("Model " + model + " from " + company + " was removed.");
        return ok(responseModel);
    }

    public Object deleteGeneration(String company, String model, String generation) {
        Map<Object, Object> responseModel = new HashMap<>();
        CarCompany carCompanyExists = getCompanyByName(company);
        carCompanyExists.getModelList().get(model).remove(generation);
        this.carRepository.save(carCompanyExists);
        responseModel.put("message", "Generation " + generation + " from model " + model + " from " + company + " was removed.");
        log.info("Generation " + generation + " from model " + model + " from " + company + " was removed.");
        return ok(responseModel);
    }

    public List<CarCompany> findAll() {
        return carRepository.findAll();
    }

    private CarCompany getCompanyByName(String company) {
        Optional<CarCompany> optionalCarCompany = carRepository.findCarByCompany(company);
        return optionalCarCompany.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found"));
    }

    public Object getAllCompanies() {
        List<String> companies = new ArrayList<>();
        carRepository.findAll().forEach(carCompanyModel -> {
            companies.add(carCompanyModel.getCompany());
        });
        return ok(Ordering.natural().sortedCopy(companies));
    }

    public Object getModels(String company) {
        return ok(Ordering.natural().sortedCopy(getCompanyByName(company).getModelList().keySet()));
    }

    public Object getGenerations(String company, String model) {

        return ok(Ordering.natural().sortedCopy(getCompanyByName(company).getModelList().get(model)));
    }
}


