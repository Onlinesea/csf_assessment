package vttp2022.csf.assessment.server.services;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import vttp2022.csf.assessment.server.models.Comment;
import vttp2022.csf.assessment.server.models.LatLng;
import vttp2022.csf.assessment.server.models.Restaurant;
import vttp2022.csf.assessment.server.repositories.MapCache;
import vttp2022.csf.assessment.server.repositories.RestaurantRepository;

@Service
public class RestaurantService {

	@Autowired
	private RestaurantRepository restaurantRepo;

	@Autowired
	private uploadImageService imgSvc;

	@Autowired
	private MapCache mapCache;

	// TODO Task 2 
	// Use the following method to get a list of cuisines 
	// You can add any parameters (if any) and the return type 
	// DO NOT CHNAGE THE METHOD'S NAME
	public List<String> getCuisines() {
		// Implmementation in here

		return restaurantRepo.getCuisines();
		
	}

	// // TODO Task 3 
	// // Use the following method to get a list of restaurants by cuisine
	// // You can add any parameters (if any) and the return type 
	// // DO NOT CHNAGE THE METHOD'S NAME
	public List<Restaurant> getRestaurantsByCuisine(String cuisine) {
		// Implmementation in here
		return restaurantRepo.getRestaurantsByCuisine(cuisine);
	}

	// // TODO Task 4
	// // Use this method to find a specific restaurant
	// // You can add any parameters (if any) 
	// // DO NOT CHNAGE THE METHOD'S NAME OR THE RETURN TYPE
	public Optional<Restaurant> getRestaurant(String restaurantId) {
		// Implmementation in here
		return restaurantRepo.getRestaurant(restaurantId);
	}

	// TODO Task 5
	// Use this method to insert a comment into the restaurant database
	// DO NOT CHNAGE THE METHOD'S NAME OR THE RETURN TYPE
	public void addComment(Comment comment) {
		// Implmementation in here
		restaurantRepo.addComment(comment);
	}
	//
	// You may add other methods to this class

	public JsonObject toJsonObject(Restaurant r){

		Float [] c = {r.getCoordinates().getLongitude(),r.getCoordinates().getLatitude()};
		String coordinates = Arrays.asList(c).toString();
		return Json.createObjectBuilder()
				.add("restaurantId", r.getRestaurantId())
				.add("name",r.getName())
				.add("cuisine", r.getCuisine())
				.add("address", r.getAddress())
				.add("coodinates",defaultValue(coordinates,"nill")) 
				.add("mapUrl", defaultValue(r.getMapURL(),"nill"))
				.build();

	}
	private <T> T defaultValue(T value, T defValue) {
		if (null != value)
			return value;
		return  defValue;
	}

	public Comment createFromJsonObject(JsonObject jObj){

		Comment c = new Comment();
		c.setName(jObj.getString("name"));
		c.setRating(jObj.getInt("rating"));
		c.setRestaurantId(jObj.getString("restaurantId"));
		c.setText(jObj.getString("text"));

		return c;
	}
}
