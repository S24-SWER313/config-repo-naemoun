package com.naim.postservice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import jakarta.validation.Valid;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/users/{userId}/posts")
public class PostController {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private PostModelAssembler postAssembler;
    @Autowired
    private UserServiceFeignClient userServiceFeignClient;

    public PostController(PostRepository postRepository,PostModelAssembler postAssembler, UserServiceFeignClient userServiceFeignClient) {
        this.postRepository = postRepository;
        this.postAssembler = postAssembler;
        this.userServiceFeignClient = userServiceFeignClient;
    }

    @PostMapping
    public ResponseEntity<EntityModel<Post>> createPost(@PathVariable Long userId, @RequestBody @Valid Post post) {
        User user = userServiceFeignClient.one(userId);
        if (user == null) {
            throw new RuntimeException("User not found with id: " + userId);
        }

        post.setUser(userId);
        post.setTimestamp(LocalDate.now());
        Post savedPost = postRepository.save(post);
        return ResponseEntity.status(HttpStatus.CREATED).body(postAssembler.toModel(savedPost));
    }

    @GetMapping
    public ResponseEntity<List<EntityModel<Post>>> getPostsByUser(@PathVariable Long userId) {
        User user = userServiceFeignClient.one(userId);
        if (user == null) {
            throw new RuntimeException("User not found with id: " + userId);
        }

        List<Post> posts = postRepository.findByUserId(userId);
        List<EntityModel<Post>> postModels = posts.stream()
        .map(postAssembler::toModel)
        .collect(Collectors.toList());

        return ResponseEntity.ok(postModels);
    }

    @PutMapping("/{postId}")
    public ResponseEntity<EntityModel<Post>> updatePost(@PathVariable Long userId, @PathVariable Long postId, @RequestBody @Valid Post updatedPost) {
        User user = userServiceFeignClient.one(userId);
        if (user == null) {
        throw new RuntimeException("User not found with id: " + userId);
        }

        Post post = postRepository.findById(postId)
        .orElseThrow(() -> new RuntimeException("Post not found with id: " + postId));

        if (!post.getUser().equals(userId)) {
        throw new RuntimeException("User is not authorized to update this post");
        }

        post.setContent(updatedPost.getContent());
        post.setTimestamp(LocalDate.now());

        Post savedPost = postRepository.save(post);
        return ResponseEntity.ok(postAssembler.toModel(savedPost));
    }

    
    @DeleteMapping("/{postId}")
    public ResponseEntity<?> deletePost(@PathVariable(value = "postId") Long postId) {
        Post post = postRepository.findById(postId)
        .orElseThrow(() -> new RuntimeException("Post not found with id: " + postId));

        postRepository.delete(post);

            return ResponseEntity.ok().build();
    }

   //done // Get a Single Post
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Post>> getPostById(@PathVariable(value = "id") Long postId) {
        Optional<Post> post = postRepository.findById(postId);
        return post.map(postAssembler::toModel)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    //done // Get the content of a post by its ID
    @GetMapping("/{postId}/content")
    public ResponseEntity<EntityModel<String>> getPostContent(@PathVariable(value = "postId") Long postId) {
        Optional<Post> optionalPost = postRepository.findById(postId);
        if (!optionalPost.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        Post post = optionalPost.get();

        EntityModel<String> contentModel = EntityModel.of(post.getContent(),
            linkTo(methodOn(PostController.class).getPostById(postId)).withRel("post"),
            linkTo(methodOn(PostController.class).getPostsByUser(post.getUser())).withRel("user-posts"));

        return ResponseEntity.ok(contentModel);
    }

    // done // Get the timestamp of a post by its ID
    @GetMapping("/{postId}/timestamp")
    public ResponseEntity<LocalDate> getPostTimestamp(@PathVariable(value = "postId") Long postId) {
        // Find the post by postId
        Optional<Post> optionalPost = postRepository.findById(postId);
        if (!optionalPost.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        Post post = optionalPost.get();

        // Return the timestamp of the post
            return ResponseEntity.ok(post.getTimestamp());
    }


   
    //done // Get the user ID of the user who posted the post
    @GetMapping("/{postId}/userid")
    public ResponseEntity<EntityModel<Long>> getPostUserId(@PathVariable(value = "postId") Long postId) {
        Optional<Post> optionalPost = postRepository.findById(postId);
        if (!optionalPost.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        Post post = optionalPost.get();
        Long userId = post.getUser();

        EntityModel<Post> entityModel = postAssembler.toModel(post);

        // Wrap the user ID with HATEOAS links
        EntityModel<Long> userIdModel = EntityModel.of(userId, entityModel.getLinks());

            return ResponseEntity.ok(userIdModel);
    }

    //done // Method to get the count of posts for a specific user
    @GetMapping("/count")
    public ResponseEntity<Long> getPostCountForUser(@PathVariable Long userId) {
        User user = userServiceFeignClient.one(userId);
        Long postCount = (long) user.getPosts().size();
            return ResponseEntity.ok(postCount);
    }

    // Update a Post
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Post>> updatePost(@PathVariable(value = "id") Long postId, @RequestBody Post updatedPost) {
        Optional<Post> optionalPost = postRepository.findById(postId);
        if (!optionalPost.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        Post existingPost = optionalPost.get();
        existingPost.setContent(updatedPost.getContent());
        existingPost.setTimestamp(updatedPost.getTimestamp());
        existingPost.setUser(updatedPost.getUser());

        Post savedPost = postRepository.save(existingPost);
        return ResponseEntity.ok(postAssembler.toModel(savedPost));
    }
    
}