package io.github.ramonbecker.quarkussocial.rest;

import io.github.ramonbecker.quarkussocial.domain.model.User;
import io.github.ramonbecker.quarkussocial.domain.repositories.UserRespository;
import io.github.ramonbecker.quarkussocial.rest.dto.FollowerRequest;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.core.Response;

import static io.restassured.RestAssured.given;

@QuarkusTest
@TestHTTPEndpoint(FollowerResource.class)
class FollowerResourceTest {

    @Inject
    UserRespository userRepository;

    Long userId;



    @BeforeEach
    @Transactional
    void setUp() {
        var user = new User();
        user.setAge(30);
        user.setName("Fulano");
        userRepository.persist(user);
        userId = user.getId();
    }

    @Test
    @DisplayName("Should return 409 when followerId is equal to User id")
    public void sameUserAsFollowerTest(){
        var body = new FollowerRequest();
        body.setFollowerId(userId);

        given().contentType(ContentType.JSON)
                .body(body)
                .pathParam("userId", userId)
                .when()
                .put()
                .then().statusCode(Response.Status.CONFLICT.getStatusCode())
                .body(Matchers.is("You can't follow yourself"));
    }

    @Test
    @DisplayName("Should return 404 when Userid doen't exist")
    public void userNotFoundTest(){

        var inexistentUserId = 999;
        var body = new FollowerRequest();
        body.setFollowerId(userId);

        given().contentType(ContentType.JSON)
                .body(body)
                .pathParam("userId", inexistentUserId)
                .when()
                .put()
                .then().statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }
}