package requestBuilder;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class APIRequestBuilder {

    public static RequestSpecification getRequestSpec() {
        return RestAssured.given().contentType("application/json").urlEncodingEnabled(false);
    }

    // Method to send a GET request to the specified URL and return the response
    public static Response get(String url){
        Response response = getRequestSpec()
                .when()
                .get(url)
                .then()
                .extract()
                .response();
        response.getBody().prettyPrint();
        return response;
    }

    // Method to send a POST request to the specified URL with the provided body and return the response
    public static Response post(String url, String body){
        Response response = getRequestSpec()
                .body(body)
                .when().log().all()
                .post(url)
                .then()
                .extract()
                .response();
        response.getBody().prettyPrint();
        return response;
    }

    // Method to send a PUT request to the specified URL with the provided body and authentication token, and return the response
    public static Response put(String url, Object body, String authToken){
        RequestSpecification requestSpec = getRequestSpec();
        if (authToken != null && !authToken.isEmpty()) {
            requestSpec.header("Authorization", "Bearer " + authToken);
        }
        if (body != null) {
            requestSpec.body(body);
        }
        Response response = requestSpec
                .when()
                .put(url)
                .then()
                .extract()
                .response();
        response.getBody().prettyPrint();
        return response;
    }

    // Method to send a DELETE request to the specified URL with the provided authentication token and return the response
    public static Response delete(String url, String authToken){
        RequestSpecification requestSpec = getRequestSpec();
        if (authToken != null && !authToken.isEmpty()) {
            requestSpec.header("Authorization", "Bearer " + authToken);
        }
        Response response = requestSpec
                .when()
                .delete(url)
                .then()
                .extract()
                .response();
        response.getBody().prettyPrint();
        return response;
    }
}
