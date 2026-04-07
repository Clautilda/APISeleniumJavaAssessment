package tests;

import com.github.javafaker.Faker;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import payloadBuilder.PayloadBuilder;
import requestBuilder.APIRequestBuilder;
import utilities.DatabaseConnection;

import java.sql.SQLException;

import static common.BaseURIs.baseURL;

public class APIAssessmentTest {

    // This test class covers the following scenarios:
    // 1. Admin Login
    // 2. User Registration
    // 3. Approve User
    // 4. Update User Role
    // 5. User Login with new role
    // 6. Delete User
    Response response;

    // Static variables to store data across tests
    static String authToken;
    static String registeredUserId;
    static String registeredEmail;


    // Setup method to establish database connection before running the tests
    @BeforeClass
    public void setup() throws SQLException {
        DatabaseConnection.dbConnection();
    }

    // Test method for admin login to obtain authentication token
    @Test
    public void adminLoginTest(){

        // Create the API path for login and build the payload using the PayloadBuilder class with credentials from the database connection
        String apiPath = "/APIDEV/login";
        String payload = PayloadBuilder.loginUserPayload(DatabaseConnection.getEmail, DatabaseConnection.getPassword);

        // Send POST request to the login endpoint with the payload and store the response
        response = APIRequestBuilder.post(baseURL + apiPath, payload);

        // Assert that the response status code is 200, indicating a successful login
        Assert.assertEquals(response.getStatusCode(), 200, "Status code should be 200");

        // Extract the authentication token from the response and store it in a static variable for later use in other API calls
        authToken = response.jsonPath().getString("data.token");
    }

    // Test method for user registration, which depends on the successful admin login to obtain the authentication token
    @Test(dependsOnMethods = "adminLoginTest")
    public void registerUser(){

        registeredEmail = Faker.instance().internet().emailAddress();
        String apiPath = "/APIDEV/register";

        String payload = PayloadBuilder.registerUserPayload("Nontobeko", "Assessment", registeredEmail, "Winter123!", "Winter123!", "1deae17a-c67a-4bb0-bdeb-df0fc9e2e526");

        response = APIRequestBuilder.post(baseURL + apiPath, payload);

        Assert.assertEquals(response.getStatusCode(), 201, "Status code should be 201");

        registeredUserId = response.jsonPath().getString("data.id");
        registeredEmail = response.jsonPath().getString("data.email");
    }

    // Test method to approve the registered user, which depends on the successful registration of the user
    @Test(dependsOnMethods = "registerUser")
    public void approveUser(){

        String apiPath = "/APIDEV/admin/users/"+registeredUserId+"/approve";

        response = APIRequestBuilder.put(baseURL + apiPath, null, authToken);

        Assert.assertEquals(response.getStatusCode(), 200, "Status code should be 200");
    }

    // Test method to update the user's role to "admin", which depends on the successful approval of the user
    @Test(dependsOnMethods = "approveUser")
    public void updateUserRole(){

        String apiPath = "/APIDEV/admin/users/"+registeredUserId+"/role";
        String payload = PayloadBuilder.updateUserRolePayload("admin");

        response = APIRequestBuilder.put(baseURL + apiPath, payload, authToken);

        Assert.assertEquals(response.getStatusCode(), 200, "Status code should be 200");
    }

    // Test method to log in with the newly updated user role, which depends on the successful update of the user's role
    @Test(dependsOnMethods = "updateUserRole")
    public void userLogin(){

        String apiPath = "/APIDEV/login";
        String payload = PayloadBuilder.loginUserPayload(registeredEmail, "Winter123!");

        response = APIRequestBuilder.post(baseURL + apiPath, payload);

        Assert.assertEquals(response.getStatusCode(), 200, "Status code should be 200");

        String role = response.jsonPath().getString("data.user.role");
        Assert.assertEquals(role, "admin", "User role should be admin");
    }


    // Test method to delete the user, which depends on the successful login with the new role
    @Test(dependsOnMethods = "userLogin")
    public void deleteUser(){

        String apiPath = "/APIDEV/admin/users/"+registeredUserId;

        response = APIRequestBuilder.delete(baseURL + apiPath, authToken);

        Assert.assertEquals(response.getStatusCode(), 200, "Status code should be 200");
    }

}
