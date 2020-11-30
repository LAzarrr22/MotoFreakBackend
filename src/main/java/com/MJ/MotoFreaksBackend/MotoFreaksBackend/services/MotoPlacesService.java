package com.MJ.MotoFreaksBackend.MotoFreaksBackend.services;

import com.MJ.MotoFreaksBackend.MotoFreaksBackend.db.collections.MotoPlaces;
import com.MJ.MotoFreaksBackend.MotoFreaksBackend.db.collections.User;
import com.MJ.MotoFreaksBackend.MotoFreaksBackend.enums.TypePlace;
import com.MJ.MotoFreaksBackend.MotoFreaksBackend.models.Address;
import com.MJ.MotoFreaksBackend.MotoFreaksBackend.models.PointsReviews;
import com.MJ.MotoFreaksBackend.MotoFreaksBackend.models.Review;
import com.MJ.MotoFreaksBackend.MotoFreaksBackend.repository.MotoPlacesRepository;
import com.MJ.MotoFreaksBackend.MotoFreaksBackend.resource.requests.NewMotoPlace;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import static org.springframework.http.ResponseEntity.ok;

@Service
public class MotoPlacesService {


    private final MotoPlacesRepository motoPlacesRepository;
    private final UserService userService;

    @Autowired
    public MotoPlacesService(MotoPlacesRepository motoPlacesRepository, UserService userService) {
        this.motoPlacesRepository = motoPlacesRepository;
        this.userService = userService;
    }

    public Object init(String token){
        User currentUser= userService.getUserByToken(token);
        MotoPlaces motoPlaces= new MotoPlaces();
        Address motoPlacesAddress= new Address();
        motoPlacesAddress.setCity("Kraków");
        motoPlacesAddress.setCountry("PL");
        motoPlacesAddress.setState("małopolska");
        motoPlacesAddress.setStreet("Poznanska");
        motoPlaces.setAddress(motoPlacesAddress);
        motoPlaces.setCreatedDate(new Date());
        motoPlaces.setUpdatedDate(new Date());
        motoPlaces.setCreatorId(currentUser.getId());
        motoPlaces.setName("Super rekomendacja");
        motoPlaces.setType(TypePlace.SHOP);
        motoPlaces.setWebPageUrl("testowa strona");

        Review newReview=new Review();
        PointsReviews pointsReviews = new PointsReviews();
        pointsReviews.setReviewerId(currentUser.getId());
        pointsReviews.setPoints(5);
        newReview.setPointsReviewsUsers(new ArrayList<>());
        newReview.getPointsReviewsUsers().add(pointsReviews);
        newReview.setAvgPoints(5);//todo AVG
        newReview.setCreatorId(currentUser.getId());
        newReview.setBody("tresc opinii");
        newReview.setId(new ObjectId().toString());

        motoPlaces.setReviewList(new ArrayList<>());
        motoPlaces.getReviewList().add(newReview);
        motoPlacesRepository.save(motoPlaces);

    return ok("test");
}

    public Object addPlace(NewMotoPlace newMotoPlace, String token) {
        return ok();
    }
}
