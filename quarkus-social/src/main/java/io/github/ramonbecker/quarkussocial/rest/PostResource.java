package io.github.ramonbecker.quarkussocial.rest;


import io.github.ramonbecker.quarkussocial.domain.model.Post;
import io.github.ramonbecker.quarkussocial.domain.model.User;
import io.github.ramonbecker.quarkussocial.domain.repositories.PostRepository;
import io.github.ramonbecker.quarkussocial.domain.repositories.UserRespository;
import io.github.ramonbecker.quarkussocial.rest.dto.CreatePostRequest;
import io.github.ramonbecker.quarkussocial.rest.dto.PostResponse;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Sort;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.stream.Collectors;

@Path("/users/{userId}/posts")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PostResource {


    private UserRespository userRespository;
    private PostRepository postRepository;

    @Inject
    public PostResource(UserRespository userRespository, PostRepository postRepository){
        this.userRespository = userRespository;
        this.postRepository = postRepository;
    }

    @POST
    @Transactional
    public Response savePost(@PathParam("userId") Long userId, CreatePostRequest request){
        User user = userRespository.findById(userId);

        if(user == null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        Post post = new Post();
        post.setText(request.getText());
        post.setUser(user);
      //  post.setDateTime(LocalDateTime.now());


        postRepository.persist(post);

        return Response.status(Response.Status.CREATED).build();
    }

    @GET
    public Response listPost(@PathParam("userId") Long userId){
        User user = userRespository.findById(userId);

        if(user == null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        PanacheQuery<Post> query = postRepository.find("user", Sort.by("dateTime", Sort.Direction.Descending) ,user);

        var list = query.list();

       var postResponseList =  list.stream()
            //   .map(post -> PostResponse.fromEntity(post))
               .map(PostResponse::fromEntity)
               .collect(Collectors.toList());
        return Response.ok(postResponseList).build();
    }
}
