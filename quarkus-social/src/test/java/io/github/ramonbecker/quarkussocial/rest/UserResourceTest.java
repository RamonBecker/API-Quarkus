package io.github.ramonbecker.quarkussocial.rest;

import io.github.ramonbecker.quarkussocial.rest.dto.CreateUserRequest;
import io.github.ramonbecker.quarkussocial.rest.dto.ResponseError;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@QuarkusTest
class UserResourceTest {

    @Test
    @DisplayName("Should create an user successfully")
    public void createUserTest() {
        var user = new CreateUserRequest();
        user.setName("Fulano");
        user.setAge(30);

        var response =
                given()
                        .contentType(ContentType.JSON)
                        .body(user)
                        .when()
                        .post("/users")
                        .then()
                        .extract().response();

        assertEquals(201, response.statusCode());
        assertNotNull(response.jsonPath().getString("id"));


    }

    @Test
    @DisplayName("Should return error when json is not valid")
    public void createUserValidationErrorTest(){
        var user = new CreateUserRequest();
        user.setName(null);
        user.setAge(null);

        var response =
                given()
                        .contentType(ContentType.JSON)
                        .body(user)
                        .when().post("/users")
                        .then().extract().response();

        assertEquals(ResponseError.UNPROCESSABLE_ENTITY_STATUS, response.getStatusCode());
        assertEquals("Validation error",response.jsonPath().getString("message"));

        List<Map<String, String>> errors = response.jsonPath().getList("errors");
        assertNotNull(errors.get(0).get("message"));
        assertNotNull(errors.get(1).get("message"));

    //    assertEquals("Age is required",errors.get(0).get("message"));
      //  assertEquals("Name is required", errors.get(1).get("message"));

    }
}