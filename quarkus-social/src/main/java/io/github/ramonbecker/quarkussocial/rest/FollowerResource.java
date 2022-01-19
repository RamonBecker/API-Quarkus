package io.github.ramonbecker.quarkussocial.rest;

import io.github.ramonbecker.quarkussocial.domain.repositories.FollowerRepository;
import io.github.ramonbecker.quarkussocial.domain.repositories.UserRespository;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

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
}
