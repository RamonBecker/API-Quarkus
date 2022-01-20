package io.github.ramonbecker.quarkussocial.rest;

import io.github.ramonbecker.quarkussocial.domain.model.Follower;
import io.github.ramonbecker.quarkussocial.domain.model.User;
import io.github.ramonbecker.quarkussocial.domain.repositories.FollowerRepository;
import io.github.ramonbecker.quarkussocial.domain.repositories.UserRespository;
import io.github.ramonbecker.quarkussocial.rest.dto.FollowerRequest;
import io.github.ramonbecker.quarkussocial.rest.dto.FollowerResponse;
import io.github.ramonbecker.quarkussocial.rest.dto.FollowersPerUserResponse;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.stream.Collectors;

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
    @Transactional
    public Response followUser(@PathParam("userId") Long userId, FollowerRequest request){

        if(userId.equals(request.getFollowerId())){
            return Response.status(Response.Status.CONFLICT).entity("You can't follow yourself").build();
        }

        User user = userRespository.findById(userId);

        if(user == null){
            return  Response.status(Response.Status.NOT_FOUND).build();
        }


        User follower = userRespository.findById(request.getFollowerId());

        boolean follows = followerRepository.follows(follower, user);

        if(!follows) {
            var entity = new Follower();
            entity.setUser(user);
            entity.setFollower(follower);
            followerRepository.persist(entity);
        }
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @GET
    public Response listFollowers(@PathParam("userId") Long userId){

        User user = userRespository.findById(userId);

        if(user == null){
            return  Response.status(Response.Status.NOT_FOUND).build();
        }

        var list = followerRepository.findByUser(userId);
        FollowersPerUserResponse response = new FollowersPerUserResponse();
        response.setFollowersCount(list.size());

        var followerList =  list.stream().map(FollowerResponse::new).collect(Collectors.toList());

        response.setContent(followerList);
        return Response.ok(response).build();
    }
}
