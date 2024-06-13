package com.naim.postservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
public class postService {

  @Autowired
  private PostRepository postRepository;
  
  @Autowired
  private UserServiceFeignClient userServiceFeignClient;

  public Post createPost(Long userId, Post post) {
    User user = userServiceFeignClient.one(userId);
    if (user == null) {
      throw new RuntimeException("User not found with id: " + userId);
    }
    post.setUser(userId);
    post.setTimestamp(LocalDate.now());
    return postRepository.save(post);
  }

  public List<Post> getPostsByUser(Long userId) {
    User user = userServiceFeignClient.one(userId);
    if (user == null) {
      throw new RuntimeException("User not found with id: " + userId);
    }
    return postRepository.findByUserId(userId);
  }

  public Post updatePost(Long userId, Long postId, Post updatedPost) {
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

    return postRepository.save(post);
  }

  public void deletePost(Long userId, Long postId) {
    User user = userServiceFeignClient.one(userId);
    if (user == null) {
      throw new RuntimeException("User not found with id: " + userId);
    }

    Post post = postRepository.findById(postId)
      .orElseThrow(() -> new RuntimeException("Post not found with id: " + postId));

    if (!post.getUser().equals(userId)) {
      throw new RuntimeException("User is not authorized to delete this post");
    }

    postRepository.delete(post);
  }

  public Post getPostById(Long postId) {
    return postRepository.findById(postId)
      .orElseThrow(() -> new RuntimeException("Post not found with id: " + postId));
  }

  public String getPostContent(Long postId) {
    Post post = getPostById(postId);
    return post.getContent();
  }

  public LocalDate getPostTimestamp(Long postId) {
    Post post = getPostById(postId);
    return post.getTimestamp();
  }


  public Long getPostUserId(Long postId) {
    Post post = getPostById(postId);
    return post.getUser();
  }

  public long getPostCountForUser(Long userId) {
    User user = userServiceFeignClient.one(userId);
    return (long) user.getPosts().size();
  }

 

    
}