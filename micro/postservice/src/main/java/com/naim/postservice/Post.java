package com.naim.postservice;

import java.time.LocalDate;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.naim.postservice.Post;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "Post")
public class Post {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column
  private Long PostID;

  @NotNull(message = "Content cannot be null")
  @Size(min = 1, message = "Content must not be empty")
  @Column
  private String content;

  @Column(name = "timestamp")
  @JsonFormat(pattern = "yyyy-MM-dd")
  private LocalDate timestamp;

  @Column
  private Integer likecount;

  @Column
  private Integer commentcount;

  @Column
  private Integer sharecount;

  @Column
  private Long userId;

  
  public Post() { }

  public Post(Long postID, String content, LocalDate timestamp, Integer likecount, Integer commentcount, Integer sharecount, Long userId) {
    this.PostID = postID;
    this.content = content;
    this.timestamp = timestamp;
    this.likecount = likecount;
    this.commentcount = commentcount;
    this.sharecount = sharecount;
    this.userId = userId;
  }

  public Long getPostID() {
    return PostID;
  }

  public void setPostID(Long postID) {
    PostID = postID;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public LocalDate getTimestampe() {
    return LocalDate.now();
  }

  public LocalDate getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(LocalDate timestamp) {
    this.timestamp = timestamp;
  }

  public Integer getLikecount() {
    return likecount;
  }

  public void setLikecount(Integer likecount) {
    this.likecount = likecount;
  }

  public Integer getCommentcount() {
    return commentcount;
  }

  public void setCommentcount(Integer commentcount) {
    this.commentcount = commentcount;
  }

  public Integer getSharecount() {
    return sharecount;
  }

  public void setSharecount(Integer sharecount) {
    this.sharecount = sharecount;
  }

  @JsonIgnore
  public Long getUser() {
    return userId;
  }

  public void setUser(Long userId) {
    this.userId = userId;
  }
  


}