package com.naim.postservice;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class PostModelAssembler implements RepresentationModelAssembler<Post, EntityModel<Post>> {

    @Override
    public EntityModel<Post> toModel(Post post) {
        // Add null check for post
        if (post == null) {
            throw new IllegalArgumentException("Post must not be null");
        }

        // Generate HATEOAS links
        EntityModel<Post> entityModel = EntityModel.of(post,
            linkTo(methodOn(PostController.class).getPostById(post.getPostID())).withSelfRel(),
            //linkTo(methodOn(PostController.class).getAllPostsForUser(post.getUser().getUserID())).withRel("user-posts"),
            linkTo(methodOn(PostController.class).getPostContent(post.getPostID())).withRel("content"),
            linkTo(methodOn(PostController.class).getPostTimestamp(post.getPostID())).withRel("timestamp"),
            linkTo(methodOn(PostController.class).getPostUserId(post.getPostID())).withRel("user-id"));
            //linkTo(methodOn(PostController.class).getPostCountForUser(post.getUser().getUserID())).withRel("user-post-count"));

        return entityModel;
    }
}