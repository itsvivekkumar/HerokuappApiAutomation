package MainAssignment;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.given;

public class BookingTests {
        private String authToken;
        private int bookingId;

        @BeforeClass
        public void setup() {
            RestAssured.baseURI = "https://restful-booker.herokuapp.com";
        }


        //creating token for authentication by using username and password data
        @Test(priority = 1)
        public void creatingToken() {
            String payload = "{ \"username\": \"admin\", \"password\": \"password123\" }";


            Response response = given().contentType(ContentType.JSON).body(payload)
                    .when().post("/auth")
                    .then().statusCode(200)
                    .extract()
                    .response();


            authToken = response.jsonPath().getString("token");
            Assert.assertNotNull(authToken, "Token should not be null");
            System.out.println("\nToken Created successfully..........");
        }


        //creating booking by using various field details in body
        @Test(priority = 2)
        public void creatingBooking() {


            // ExtentTestManager.getTest().log(Status.INFO, "Executing testCreateBooking........");


            String bookingPayload = "{\n" +
                    "    \"firstname\" : \"John\",\n" +
                    "    \"lastname\" : \"Doe\",\n" +
                    "    \"totalprice\" : 100,\n" +
                    "    \"depositpaid\" : true,\n" +
                    "    \"bookingdates\" : {\n" +
                    "        \"checkin\" : \"2023-09-01\",\n" +
                    "        \"checkout\" : \"2023-09-10\"\n" +
                    "    },\n" +
                    "    \"additionalneeds\" : \"Breakfast\"\n" +
                    "}";


            Response response = given().contentType(ContentType.JSON).body(bookingPayload)
                    .when().post("/booking")
                    .then().statusCode(200)
                    .extract()
                    .response();


            bookingId = response.jsonPath().getInt("bookingid");
            Assert.assertTrue(bookingId > 0, "booking id must be greater than 0");
            System.out.println("\nBooking Created..........");
            // ExtentTestManager.getTest().log(Status.PASS, "testCreateBooking Passed......");


        }


        //getting all booking details
        @Test(priority = 3)
        public void getAllBookingsDetails() {
            Response response = given().contentType(ContentType.JSON)
                    .when().get("/booking")
                    .then().statusCode(200)
                    .extract()
                    .response();
            //validating the booking list not be null by using booking id
            Assert.assertTrue(response.jsonPath().getList("bookingid").size() > 0, "Booking list not be empty...");
            System.out.println("\nGenerating all Booking details successfully..........");
        }


        //updating totalprice and additionalneeds field
        @Test(priority = 4)
        public void updatingPartialBookingDetails() {
            String updatePayload = "{\n" +
                    "    \"firstname\" : \"John\",\n" +
                    "    \"lastname\" : \"Doe\",\n" +
                    "    \"totalprice\" : 200,\n" +
                    "    \"depositpaid\" : true,\n" +
                    "    \"bookingdates\" : {\n" +
                    "        \"checkin\" : \"2023-09-01\",\n" +
                    "        \"checkout\" : \"2023-09-10\"\n" +
                    "    },\n" +
                    "    \"additionalneeds\" : \"Lunch\"\n" +
                    "}";


            given().contentType(ContentType.JSON).header("Cookie", "token=" + authToken).body(updatePayload)
                    .when().put("/booking/" + bookingId)
                    .then().statusCode(200);
            System.out.println("\nUpdating partial field value successfully..........");
        }


        //fetching updated fields and verifying it
        @Test(priority = 5)
        public void getUpdatedBookingDetails() {
            Response res = given().contentType(ContentType.JSON)
                    .when().get("/booking/" + bookingId)
                    .then().statusCode(200)
                    .extract()
                    .response();


            String additionalNeeds = res.jsonPath().getString("additionalneeds");
            int totalPrice = res.jsonPath().getInt("totalprice");


            Assert.assertEquals(additionalNeeds, "Lunch", "additional needs value must be updated as lunch");
            Assert.assertEquals(totalPrice, 200, "Total price value must be updated with 150");
            System.out.println("\nFetching and validating the updated fields value successfully..........");
        }


        //deleting booking with particular id
        @Test(priority = 6)
        public void deletingBooking() {
            given().header("Cookie", "token=" + authToken)
                    .when().delete("/booking/" + bookingId)
                    .then().statusCode(201);  // Assuming 201 for successful deletion


            // Validating the particular booking is deleted or not
            given()
                    .when().get("/booking/" + bookingId).then()
                    .statusCode(404);  // Booking should not exist anymore
            System.out.println("\nDeleting booking details successfully..........");
        }


        // Negative test 1--> updating the booking with invalid token value
        @Test(priority = 7)
        public void updatingBookingWithInvalidTokenValue() {
            String invalidToken = "invalidToken";
            String updatePayload = "{\n" +
                    "    \"firstname\" : \"John\",\n" +
                    "    \"lastname\" : \"Doe\",\n" +
                    "    \"totalprice\" : 150,\n" +
                    "    \"depositpaid\" : true,\n" +
                    "    \"bookingdates\" : {\n" +
                    "        \"checkin\" : \"2023-09-01\",\n" +
                    "        \"checkout\" : \"2023-09-10\"\n" +
                    "    },\n" +
                    "    \"additionalneeds\" : \"Lunch\"\n" +
                    "}";


            Response res=given().contentType(ContentType.JSON).header("Cookie", "token=" + invalidToken).body(updatePayload)
                    .when().put("/booking/" + bookingId)
                    .then().statusCode(403)  // Expecting 403 Forbidden for invalid token
                    .extract()
                    .response();


            Assert.assertEquals(res.getStatusCode(), 403, "Expecting status code 403 for the invalid token value.....");
            System.out.println("\nupdating booking with invalid inputs..........");
        }


        //Negative test case2----> creating  booking with invalid value in request body part
        @Test(priority = 8)
        public void creatingBookingWithInvalidRequestBody() {
            String invalidPayload = "{\n" +                                        //not using firstname and lastname value
                    "    \"totalprice\" : \"wrongType\",\n" +
                    "    \"depositpaid\" : true,\n" +
                    "    \"bookingdates\" : {\n" +
                    "        \"checkin\" : \"2023-09-02\",\n" +
                    "        \"checkout\" : \"2023-09-11\"\n" +
                    "    },\n" +
                    "    \"additionalneeds\" : \"Breakfast\"\n" +
                    "}";


            Response response = given().contentType(ContentType.JSON).body(invalidPayload)
                    .when().post("/booking")
                    .then()
                    .extract()
                    .response();
            Assert.assertEquals(response.getStatusCode(), 500, "Expecting the status code value 400 for invalid request body detaisl");
            System.out.println("\ncreating  booking with invalid inputs..........");
        }
    }

