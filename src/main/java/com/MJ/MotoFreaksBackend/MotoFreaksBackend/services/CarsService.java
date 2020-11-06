package com.MJ.MotoFreaksBackend.MotoFreaksBackend.services;

import com.MJ.MotoFreaksBackend.MotoFreaksBackend.db.collections.CarCompany;
import com.MJ.MotoFreaksBackend.MotoFreaksBackend.repository.CarCompanyRepository;
import com.MJ.MotoFreaksBackend.MotoFreaksBackend.resource.requests.NewCarCompany;
import com.google.common.collect.Ordering;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

import static org.springframework.http.ResponseEntity.ok;

@Service
@Slf4j
public class CarsService {
    @Autowired
    private CarCompanyRepository carRepository;
    @Autowired
    private UserService userService;


    public Object mergeCarModel(String token, NewCarCompany newCars, Map<String, String> carParam) {
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
        newList.addAll(newList);
        return newList;
    }

    public Object deleteCompany(String company) {
        CarCompany carCompanyExists = getCompanyByName(company);
        this.carRepository.delete(carCompanyExists);
        log.info("Company " + company + " was removed.");
        return getAllCompanies();
    }

    public Object deleteModel(String company, String model) {
        CarCompany carCompanyExists = getCompanyByName(company);
        carCompanyExists.getModelList().remove(model);
        this.carRepository.save(carCompanyExists);
        log.info("Model " + model + " from " + company + " was removed.");
        return getModels(company);
    }

    public Object deleteGeneration(String company, String model, String generation) {
        CarCompany carCompanyExists = getCompanyByName(company);
        carCompanyExists.getModelList().get(model).remove(generation);
        this.carRepository.save(carCompanyExists);
        log.info("Generation " + generation + " from model " + model + " from " + company + " was removed.");
        return getGenerations(company,model);
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
if(getCompanyByName(company).getModelList().get(model)==null){
    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Model not found");
}
        return ok(Ordering.natural().sortedCopy(getCompanyByName(company).getModelList().get(model)));
    }

    public Object addCompany(String token, String company) {
        Optional<CarCompany> optionalCarCompany = carRepository.findCarByCompany(company);
        if (optionalCarCompany.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Company is already exists.");
        } else {
            CarCompany carCompany = new CarCompany();
            carCompany.setCompany(company);
            carCompany.setCreatedDate(new Date());
            carCompany.setModelList(new HashMap<>());
            carCompany.setCreatorId(userService.getUserByToken(token).getId());
            log.info("Company " + carCompany.getCompany() + " was added.");
            this.carRepository.save(carCompany);
        }
        return getAllCompanies();
    }

    public Object addModel(String company, String model) {
        CarCompany existsCarCompany = getCompanyByName(company);
        if(existsCarCompany.getModelList().keySet().stream().anyMatch(mod->mod.equals(model))){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Model is already exists.");
        }
        existsCarCompany.setUpdatedDate(new Date());
        existsCarCompany.getModelList().put(model, new ArrayList<>());
        log.info("Model " + model + " was added to " + company);
        this.carRepository.save(existsCarCompany);
        return getModels(company);
    }

    public Object addGeneration(String company, String model, String generation) {
        CarCompany existsCarCompany = getCompanyByName(company);
        if(existsCarCompany.getModelList().get(model)==null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Model not found");
        }
        if(existsCarCompany.getModelList().get(model).stream().anyMatch(gen -> gen.equals(generation))){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Generation is already exists.");
        }
        existsCarCompany.setUpdatedDate(new Date());
        existsCarCompany.getModelList().get(model).add(generation);
        log.info("Generation " + generation + " was added to " + model + " from "+ company);
        this.carRepository.save(existsCarCompany);
        return getGenerations(company,model);
    }
}


