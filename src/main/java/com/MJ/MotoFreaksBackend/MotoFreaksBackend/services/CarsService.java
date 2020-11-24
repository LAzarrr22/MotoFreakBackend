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
import java.util.stream.Collectors;

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
        carCompanyExists.getModelList().remove(findExistsModelName(company,model));
        this.carRepository.save(carCompanyExists);
        log.info("Model " + model + " from " + company + " was removed.");
        return ok(getModels(company));
    }

    public Object deleteGeneration(String company, String model, String generation) {
        CarCompany carCompanyExists = getCompanyByName(company);
        carCompanyExists.getModelList().get(findExistsModelName(company,model)).remove(findExistsGenerationName(company,model,generation));
        this.carRepository.save(carCompanyExists);
        log.info("Generation " + generation + " from model " + model + " from " + company + " was removed.");
        return ok(getGenerations(company, model));
    }

    public List<CarCompany> findAll() {
        return carRepository.findAll();
    }


    private CarCompany getCompanyByName(String company) {
        if (carRepository.findAll().stream().anyMatch(company1 -> company1.getCompany().toLowerCase().equals(company.toLowerCase()))) {
            return carRepository.findAll().stream().filter(carCompany -> carCompany.getCompany().toLowerCase().equals(company.toLowerCase()))
                    .findAny().orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found"));
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Company not found");
    }

    public Object getAllCompanies() {
        List<String> companies = new ArrayList<>();
        carRepository.findAll().forEach(carCompanyModel -> {
            companies.add(carCompanyModel.getCompany());
        });
        return ok(Ordering.natural().sortedCopy(companies));
    }

    public List<String> getModels(String company) {
        return Ordering.natural().sortedCopy(getCompanyByName(company).getModelList().keySet());
    }

    public List<String> getGenerations(String company, String model) {
        if (Objects.isNull(findExistsModelName(company, model))) {
            return new ArrayList<>();
        }
        return Ordering.natural().sortedCopy(getCompanyByName(company).getModelList().get(findExistsModelName(company, model)));
    }

    public Object addCompany(String token, String company) {
        Optional<CarCompany> optionalCarCompany = carRepository.findCarByCompany(company);
        if (optionalCarCompany.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Company is already exists.");
        } else if (carRepository.findAll().stream().anyMatch(mod -> mod.getCompany().toLowerCase().equals(company.toLowerCase()))) {
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
        if (existsCarCompany.getModelList().keySet().stream().anyMatch(mod -> mod.toLowerCase().equals(model.toLowerCase()))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Model is already exists.");
        }
        existsCarCompany.setUpdatedDate(new Date());
        existsCarCompany.getModelList().put(model, new ArrayList<>());
        log.info("Model " + model + " was added to " + company);
        this.carRepository.save(existsCarCompany);
        return ok(getModels(company));
    }

    public Object addGeneration(String company, String model, String generation) {
        CarCompany existsCarCompany = getCompanyByName(company);
        if (getModels(company) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Model not found");
        }
        if (getGenerations(company, model).stream().anyMatch(gen -> gen.toLowerCase().equals(generation.toLowerCase()))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Generation is already exists.");
        }
        existsCarCompany.setUpdatedDate(new Date());
        existsCarCompany.getModelList().get(findExistsModelName(company, model)).add(generation);
        log.info("Generation " + generation + " was added to " + model + " from " + company);
        this.carRepository.save(existsCarCompany);
        return ok(getGenerations(company, model));
    }

    private String findExistsModelName(String company, String model) {
        List<String> foundModels = getCompanyByName(company).getModelList().keySet().stream().filter(models -> models.toLowerCase().equals(model.toLowerCase()))
                .collect(Collectors.toList());
        if (foundModels.size() == 1) {
            return foundModels.get(0);
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Model not found");
    }

    private String findExistsGenerationName(String company, String model, String generation) {
        List<String> foundGeneration = getCompanyByName(company).getModelList().get(findExistsModelName(company,model)).stream().filter(gens -> gens.toLowerCase().equals(generation.toLowerCase()))
                .collect(Collectors.toList());
        if (foundGeneration.size() == 1) {
            return foundGeneration.get(0);
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Generation not found");
    }

}


