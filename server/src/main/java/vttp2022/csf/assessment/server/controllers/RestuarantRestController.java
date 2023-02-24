package vttp2022.csf.assessment.server.controllers;

import java.io.StringReader;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import vttp2022.csf.assessment.server.models.Restaurant;
import vttp2022.csf.assessment.server.repositories.RestaurantRepository;
import vttp2022.csf.assessment.server.services.RestaurantService;

@RestController
@RequestMapping(path="/api")
public class RestuarantRestController {
    

    @Autowired
    private RestaurantRepository rRepo;

    @Autowired
    private RestaurantService rSvc;


    @GetMapping(path="/cuisines")
    public ResponseEntity<String> getAllCuisines(){
        System.out.println("api called");
        List<String> cusinesList = rSvc.getCuisines();
        
        return ResponseEntity.ok().body(cusinesList.toString());
    }

    @GetMapping(path="/{cuisine}/restaurants")
    public ResponseEntity<String> getAllRestaurantsByCuisine(@PathVariable String cuisine){

        System.out.println("Cuisine wanted > " + cuisine);
        List<Restaurant> resultList = rSvc.getRestaurantsByCuisine(cuisine);
        List<String> stringList = resultList.stream().map(v -> v.getName()).toList();

        // List<String> stringList = resultList.stream().map(v -> rSvc.toJsonObject(v).toString()).toList();

        return ResponseEntity.ok().body(stringList.toString());
    }

    @GetMapping(path="/restaurant/{restaurantId}")
    public ResponseEntity<String> getRestaurantById(@PathVariable String restaurantId){

        System.out.println("restaurant wanted > " + restaurantId);
        Optional<Restaurant> selectedRestaurant = rSvc.getRestaurant(restaurantId);
        if(selectedRestaurant.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        String result = rSvc.toJsonObject(selectedRestaurant.get()).toString();
        return ResponseEntity.ok().body(result);
    }

    @PostMapping(path="/comments")
    public ResponseEntity<String> insertComment(@RequestBody String comment){

        JsonReader reader = Json.createReader(new StringReader(comment));
        JsonObject json = reader.readObject();

        System.out.println("newOrder >>>> " + comment);

        rSvc.addComment(rSvc.createFromJsonObject(json));
        JsonObject create = Json.createObjectBuilder()
                            .add("message", "Comment posted")
                            .build();

        
        return ResponseEntity.ok().body(create.toString());
    }

}
