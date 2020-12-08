package com.MJ.MotoFreaksBackend.MotoFreaksBackend.resource.controller;

import com.MJ.MotoFreaksBackend.MotoFreaksBackend.db.collections.CarCompany;
import com.MJ.MotoFreaksBackend.MotoFreaksBackend.resource.requests.NewCarCompany;
import com.MJ.MotoFreaksBackend.MotoFreaksBackend.consts.AuthorizationHeader;
import com.MJ.MotoFreaksBackend.MotoFreaksBackend.services.CarsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/cars")
@CrossOrigin("*")
public class CarCompanyController {

    private final CarsService carsService;

    @Autowired
    public CarCompanyController(CarsService carsService) {
        this.carsService = carsService;
    }


    @RequestMapping(path = "/merge", method = RequestMethod.POST, produces = "application/json")
    public Object merge(HttpServletRequest req, @RequestBody NewCarCompany NewCarCompany, @RequestParam Map<String, String> carParam) {
        String token = req.getHeader(AuthorizationHeader.HEADER_NAME).replace(AuthorizationHeader.TOKEN_PREFIX, "");
        return this.carsService.mergeCarModel(token, NewCarCompany,carParam);
    }

    @RequestMapping(path = "/company/{company}", method = RequestMethod.PUT, produces = "application/json")
    public Object addCompany(HttpServletRequest req,@PathVariable String company) {
        String token = req.getHeader(AuthorizationHeader.HEADER_NAME).replace(AuthorizationHeader.TOKEN_PREFIX, "");
        return this.carsService.addCompany(token, company);
    }

    @RequestMapping(path = "/model/{company}/{model}", method = RequestMethod.PUT, produces = "application/json")
    public Object addModel(@PathVariable String company, @PathVariable String model) {
        return this.carsService.addModel(company, model);
    }

    @RequestMapping(path = "/generation/{company}/{model}/{generation}", method = RequestMethod.PUT, produces = "application/json")
    public Object addGeneration(@PathVariable String company, @PathVariable String model, @PathVariable String generation) {
        return this.carsService.addGeneration(company, model, generation);
    }

    @RequestMapping(path = "/company/{company}", method = RequestMethod.DELETE, produces = "application/json")
    public Object deleteCompany(@PathVariable String company) {
        return this.carsService.deleteCompany(company);
    }

    @RequestMapping(path = "/model/{company}/{model}", method = RequestMethod.DELETE, produces = "application/json")
    public Object deleteModel(@PathVariable String company, @PathVariable String model) {
        return this.carsService.deleteModel(company, model);
    }

    @RequestMapping(path = "/generation/{company}/{model}/{generation}", method = RequestMethod.DELETE, produces = "application/json")
    public Object deleteGeneration(@PathVariable String company, @PathVariable String model, @PathVariable String generation) {
        return this.carsService.deleteGeneration(company, model, generation);
    }

    @RequestMapping(path = "", method = RequestMethod.GET, produces = "application/json")
    public List<CarCompany> getAll() {
        return carsService.findAll();
    }

    @RequestMapping(path = "/company", method = RequestMethod.GET, produces = "application/json")
    public Object getCompanies() {
        return carsService.getAllCompanies();
    }

    @RequestMapping(path = "/model/{company}", method = RequestMethod.GET, produces = "application/json")
    public Object getModels(@PathVariable String company) {
        return ok(carsService.getModels(company));
    }

    @RequestMapping(path = "/generation/{company}/{model}", method = RequestMethod.GET, produces = "application/json")
    public Object getGenerations(@PathVariable String company, @PathVariable String model) {
        return ok(carsService.getGenerations(company, model));
    }

}
