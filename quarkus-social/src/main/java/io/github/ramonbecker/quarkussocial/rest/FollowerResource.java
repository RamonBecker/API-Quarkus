package io.github.ramonbecker.quarkussocial.rest;

import io.github.ramonbecker.quarkussocial.domain.model.Follower;
import io.github.ramonbecker.quarkussocial.domain.model.User;
import io.github.ramonbecker.quarkussocial.domain.repositories.FollowerRepository;
import io.github.ramonbecker.quarkussocial.domain.repositories.UserRespository;
import io.github.ramonbecker.quarkussocial.rest.dto.FollowerRequest;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/users/{userId}/followers")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class FollowerResource {
    private FollowerRepository followerRepository;
    private UserRespository userRespository;

    @Inject
    public FollowerResource(FollowerRepository followerRepository, UserRespository userRespository){
        this.followerRepository = followerRepository;
        this.userRespository = userRespository;
    }

    @PUT
    public Response followUser(@PathParam("userId") Long userId, FollowerRequest request){
        User user = userRespository.findById(userId);

        if(user == null){
            return  Response.status(Response.Status.NOT_FOUND).build();
        }

        User follower = userRespository.findById(request.getFollowerId());

        var entity = new Follower();
        entity.setUser(user);
        entity.setFollower(follower);

        followerRepository.persist(entity);

        return Response.status(Response.Status.NO_CONTENT).build(); 
    }
}
