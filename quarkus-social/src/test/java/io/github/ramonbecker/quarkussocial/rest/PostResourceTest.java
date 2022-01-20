package io.github.ramonbecker.quarkussocial.rest;

import io.github.ramonbecker.quarkussocial.domain.model.User;
import io.github.ramonbecker.quarkussocial.domain.repositories.UserRespository;
import io.github.ramonbecker.quarkussocial.rest.dto.CreatePostRequest;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.transaction.Transactional;

import static io.restassured.RestAssured.given;

@QuarkusTest
@TestHTTPEndpoint(PostResource.class)
class PostResourceTest {

    @Inject
    UserRespository userRespository;

    Long userId;

    @BeforeEach
    @Transactional
    public void setUP(){
        var user = new User();
        user.setName("Fulano");
        user.setAge(30);
        userRespository.persist(user);
        userId = user.getId();
    }

    @Test
    @DisplayName("Should create a post for a user")
    public void createPostTest(){
        var postRequest = new CreatePostRequest();
        postRequest.setText("Some text");

        given().
                contentType(ContentType.JSON)
                .body(postRequest)
                .pathParam("userId", userId)
                .when().post()
                .then().statusCode(201);
    }

    @Test
    @DisplayName("Should return 404 when trying to make a post for a inexistent user")
    public void postForAndInexistentUserTest(){
        var postRequest = new CreatePostRequest();
        postRequest.setText("Some text");

        var inexistentUserID = 999;

        given().
                contentType(ContentType.JSON)
                .body(postRequest)
                .pathParam("userId", inexistentUserID)
                .when().post()
                .then().statusCode(404);
    }

    @Test
    @DisplayName("Should return 404 when user doesn't exist")
    public void listPostUserNotFoundTest(){
            var inexistentUserId = 999;

            given().pathParam("userId", inexistentUserId)
                    .when().get()
                    .then().statusCode(404);

    }

    @Test
    @DisplayName("Should return 400 when followerId header is not present")
    public void listPostFollowerHeaderNotSendTest(){
        //
        given().pathParam("userId", userId)
                .when().get()
                .then().statusCode(400)
                .body(Matchers.is("You forgot the header followerId"));
    }

    @Test
    @DisplayName("Should return 400 when follower doesn't exist")
    public void listPostFollowerNotFoundTest(){

        var inexistentFollowerId = 999;

        given().pathParam("userId", userId)
                .header("followerId", inexistentFollowerId)
                .when().get()
                .then().statusCode(400)
                .body(Matchers.is("Inexistent followerId"));
    }

    @Test
    @DisplayName("Should return 403 when follower isn't a follower")
    public void listPostNotFollowerTest(){

    }

    @Test
    @DisplayName("Should return posts")
    public void listPostsTest(){

    }
}