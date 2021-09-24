package APITests;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.http.Header;
import io.restassured.response.Response;
import java.io.File;
import static io.restassured.RestAssured.*;
import static org.testng.Assert.assertEquals;



@Test (priority=1)
public class automateAPICalls {
	//Provide the PET ID and PET NAME HERE	
	int petID=3456796;	
	String petName="testDogName_092320210977";
	String pet_cat=null;
	
	String filePath=System.getProperty("user.dir");
		
	@Test (priority = 1)	 
	public void test_addNewPet() {		
						
		JSONObject postRequest=new JSONObject();
		JSONArray Tags=new JSONArray();
		JSONObject category = new JSONObject();
		JSONObject Tag_list1=new JSONObject();
		JSONArray photoURLs=new JSONArray();
		//category
		category.put("id", "0");
		category.put("name", "testDogCategory 09232021");
		//photoUrl
		photoURLs.add("none");
		//Tags
		Tag_list1.put("id", "0");
		Tag_list1.put("name", "Tag1");
		Tags.add(Tag_list1);
		//requestJson
		postRequest.put("id", petID);
		postRequest.put("category", category);
		postRequest.put("photoUrls", photoURLs);
		postRequest.put("name", petName);
		postRequest.put("tags", Tags);
		postRequest.put("status", "available");
				
		Header petstoreHeader = new Header("api_key", "special-key");

		Response response=given()
		.contentType("application/json")
		.header(petstoreHeader)
		.body(postRequest.toJSONString())
		.when()
		.post("https://petstore.swagger.io/v2/pet")
		.then()
        .extract().response();
		

		assertEquals(response.statusCode(), 200);
		assertEquals(response.path("name"),petName);
		assertEquals(response.path("id"),petID);
		
		
		System.out.println("Pet got created with ID:" + response.path("id"));
	
	}
	
	@Test (priority=2, dependsOnMethods = "test_addNewPet")
	public void testuploadDogPhoto()
	{
		RestAssured.baseURI="https://petstore.swagger.io";
		RestAssured.basePath="v2/pet/" + petID + "/uploadImage";
		
		File file=new File(System.getProperty("user.dir") + "/pictures/dog1.jpg");
		
		Response response=
				given()
				.multiPart("file",file,"multipart/form-data")
				.post()
				.thenReturn();
		assertEquals(response.statusCode(), 200);
		System.out.println("Photo Uploaded for" + petID);
	}
	
	@Test (priority=3, dependsOnMethods="test_addNewPet")
	public void testGetNewPetDetails()
	{		
		
		Response response= given()
		.pathParam("id", petID)
		.when() 
		.get("https://petstore.swagger.io/v2/pet/{id}")
		.then()
		.extract().response();
		
		assertEquals(response.statusCode(), 200);
		assertEquals(response.contentType(),"application/json");
		assertEquals(response.path("name"),petName);
		assertEquals(response.path("id"),petID);

		response.prettyPrint();
		
		//$.category.name[?(@.name="testDogCategory")]
	}	
}
