package vttp2022.csf.assessment.server.repositories;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.Optional;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.mongodb.client.result.UpdateResult;
import com.mongodb.internal.operation.UpdateOperation;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import vttp2022.csf.assessment.server.models.Comment;
import vttp2022.csf.assessment.server.models.LatLng;
import vttp2022.csf.assessment.server.models.Restaurant;
import vttp2022.csf.assessment.server.services.uploadImageService;

@Repository
public class RestaurantRepository {

	final String RESTAURANT_COLLECTION = "restaurants";
	final String COMMENT_COLLECTION = "comments";

	@Autowired
	private MongoTemplate template;

	@Autowired
	private MapCache imgSvc;
	

	// TODO Task 2
	// Use this method to retrive a list of cuisines from the restaurant collection
	// You can add any parameters (if any) and the return type
	// DO NOT CHNAGE THE METHOD'S NAME
	// Write the Mongo native query above for this method
	//
	public List<String> getCuisines() {
		// Implmementation in here

		/*
		 * Native Mongo query
		 * db.restaurants.distinct("cuisine")
		 * 
		 */

		List<String> cuisineList = template.findDistinct(new Query(), "cuisine", RESTAURANT_COLLECTION, String.class);

		return cuisineList;
	}

	// TODO Task 3
	// Use this method to retrive a all restaurants for a particular cuisine
	// You can add any parameters (if any) and the return type
	// DO NOT CHNAGE THE METHOD'S NAME
	// Write the Mongo native query above for this method
	//
	public List<Restaurant> getRestaurantsByCuisine(String cuisine) {
		// Implmementation in here

		/*
		 * Native Mongo query
		 * 
		 * db.restaurants.find({
		 * "cuisine": { $in: [ "African" ] }
		 * })
		 * 
		 */

		Criteria c = Criteria.where("cuisine")
				.in(cuisine);
		Query q = Query.query(c);

		List<Document> resturantsListByCuisine = template.find(q, Document.class, RESTAURANT_COLLECTION);

		System.out.println("Retrieved sucessfully from mongo >>> " + resturantsListByCuisine.toString());
		return resturantsListByCuisine
				.stream()
				.map(d -> createFromDoc(d))
				.toList();
	}

	// TODO Task 4
	// Use this method to find a specific restaurant
	// You can add any parameters (if any)
	// DO NOT CHNAGE THE METHOD'S NAME OR THE RETURN TYPE
	// Write the Mongo native query above for this method
	//
	public Optional<Restaurant> getRestaurant(String restaurantId) {
		// Implmementation in here
		/*
		 * Native Mongo query
		 * 
		 * db.restaurants.find({
		 * "restaurant_id" : "40368021"
		 * })
		 * 
		 * 
		 * 
		 */

		Criteria c = Criteria.where("restaurant_id")
				.is(restaurantId);
		Query q = Query.query(c);

		List<Document> restaurantById = template.find(q, Document.class, RESTAURANT_COLLECTION);

		System.out.println("Retrieved sucessfully from mongo >>> " + restaurantById.toString());

		if(restaurantById.size()<1){
			return Optional.empty();
		}

		String key = "";
        String baseImageUrl="https://onlinesea.sgp1.digitaloceanspaces.com/";
		Restaurant r = createFromDoc(restaurantById.get(0));

		if(r.getMapURL().trim().equalsIgnoreCase("nill")){

		try {
            key = imgSvc.getMap(r.getCoordinates());
            r.setMapURL(baseImageUrl+"myobjects/%s".formatted(key));

			Update updateOps = new Update().set("mapUrl", r.getMapURL());
            UpdateResult updateResult= template.upsert(q, updateOps, RESTAURANT_COLLECTION);
			System.out.println(" Updated MapUrl >>>>>>>>>> " + updateResult);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
	}
		

		return Optional.of(r);
	}

	// TODO Task 5
	// Use this method to insert a comment into the restaurant database
	// DO NOT CHNAGE THE METHOD'S NAME OR THE RETURN TYPE
	// Write the Mongo native query above for this method
	//
	public void addComment(Comment comment) {
		// Implmementation in here

		
		template.insert(comment,COMMENT_COLLECTION);
	}

	// You may add other methods to this class



	public Restaurant createFromDoc(Document d) {
		Restaurant r = new Restaurant();

		// Set the resturant using objectId from document
		r.setRestaurantId(d.getString("restaurant_id"));

		// Set the name of the resturant
		r.setName(d.getString("name"));

		// Set cuisine
		r.setCuisine(d.getString("cuisine"));

		// Set address
		Document address = d.get("address", Document.class);
		// String building = address.getString("building");
		// String street = address.getString("street");
		// String zipcode = address.getString("zipcode");
		// String addressString = Json.createObjectBuilder()
		// .add("building", building)
		// .add("street", street)
		// .add("zipcode", zipcode)
		// .build().toString();

		// r.setAddress(addressString);
		// JsonReader reader = Json.createReader(new StringReader(address.toJson()));
		// JsonObject json = reader.readObject();
		r.setAddress(address.toJson().toString());

		// r.setAddress(json.toString());
		// r.setAddress(d.get("address",String.class));

		// Set coordinates
		LatLng coordinates = new LatLng();
		System.out.println("Gettign coordinates >>>>>>>>>>>>>>>>>>>> " + address);

		List<Object> coord = address.getList("coord", Object.class);
		coordinates.setLongitude(Float.parseFloat(coord.get(0).toString()));
		coordinates.setLatitude(Float.parseFloat(coord.get(1).toString()));
		r.setCoordinates(coordinates);
		System.out.println("coordinates long>>>>>>>>>>>>>>>>>>>> " + coordinates.getLongitude());

		// coordinates.setLongitude(Float.parseFloat(coord.get(0)));
		// coordinates.setLatitude(Float.parseFloat(coord.get(1)));

		// Set mapUrl
		 r.setMapURL(defaultValue(d.getString(address), "nill") );
		 // r.setMapURL(d.getString(address));

		return r;
	}

	private <T> T defaultValue(T value, T defValue) {
		if (null != value)
			return value;
		return  defValue;
	}

}
