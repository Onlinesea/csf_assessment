package vttp2022.csf.assessment.server.repositories;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.imageio.stream.ImageInputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

import vttp2022.csf.assessment.server.models.LatLng;

@Repository
public class MapCache {

	@Autowired
    private AmazonS3 s3Client;
	// TODO Task 4
	// Use this method to retrieve the map
	// You can add any parameters (if any) and the return type 
	// DO NOT CHNAGE THE METHOD'S NAME
	public String getMap(LatLng coordinates)throws IOException {
		// Implmementation in here
        // Get the map from map api
        final String MAP_URL = "http://map.chuklee.com/map";
        String url = UriComponentsBuilder.fromUriString(MAP_URL)
                        .queryParam("lat", coordinates.getLatitude())
                        .queryParam("lng", coordinates.getLongitude())
                        .toUriString();
        
        RestTemplate template = new RestTemplate();
        ResponseEntity<byte[]> result = template.getForEntity(url, byte[].class);
        byte[] file = result.getBody();
        // ResponseEntity<String> resp =  template.getForEntity(url, String.class);

        // User data
        Map<String, String> userData = new HashMap<>();
        userData.put("name", "fred");
        userData.put("uploadTime", (new Date()).toString());

        // Metadata of the file
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setUserMetadata(userData);

        String key = UUID.randomUUID().toString().substring(0, 8);
        InputStream targetStream = new ByteArrayInputStream(file);
        // Create a put request
        PutObjectRequest putReq = new PutObjectRequest(
            "onlinesea", // bucket name
            "myobjects/%s".formatted(key), //key
            targetStream, //inputstream 
            metadata);

        // Allow public access
        putReq.withCannedAcl(CannedAccessControlList.PublicRead);

        s3Client.putObject(putReq);

        return key;
    }
		
	// }

	// You may add other methods to this class

}
