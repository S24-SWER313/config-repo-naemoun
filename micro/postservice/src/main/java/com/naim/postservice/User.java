package com.naim.postservice;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long userID;

  @NotBlank(message = "Username is required")
  @Size(min = 3, message = "Username must be at least 3 characters long")
  private String username;

  private int age;

  @NotBlank
  @Email(message = "email should be valid")
  private String email;

  @NotBlank(message = "Password is required")
  @Size(min = 8, message = "Password must be at least 8 characters long")
  private String Password;

  private String ProfilePic;

  private String Bio;

  private Gender gender;

  private LocalDate DOB;

  private String Location;

  private Set<Post> posts = new HashSet<>();

  private List<User> following = new ArrayList<>();

  private List<User> followers = new ArrayList<>();

  public User() { }

  public User(String username, String email, String password, String profilePic, String bio,Gender gender, LocalDate dOB, String location) {
    this.username = username;
    this.email = email;
    this.Password = password;
    this.ProfilePic = profilePic;
    this.Bio = bio;
    this.gender = gender;
    this.DOB = dOB;
    this.Location = location;
    this.age= getAge();
  }

  public User(String username, String email, String Password) {
    this.username = username;
    this.email = email;
    this.Password = Password;
  }

  public Long getUserID() {
    return userID;
  }

  public void setUserID(Long userID) {
    this.userID = userID;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public void setAge(int age) {
    this.age = age;
  }

  public int getAge() {
    if (DOB != null) {
      return Period.between(DOB, LocalDate.now()).getYears();
    } else {
        return 0;
    }
  }

  public String getemail() {
    return email;
  }

  public void setemail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return Password;
  }

  public void setPassword(String password) {
    this.Password = password;
  }

  public String getProfilePic() {
    return ProfilePic;
  }

  public void setProfilePic(String profilePic) {
    this.ProfilePic = profilePic;
  }

  public String getBio() {
    return Bio;
  }

  public void setBio(String bio) {
    this.Bio = bio;
  }

  public Gender getGender() {
    return gender;
  }

  public void setGender(Gender gender) {
    this.gender = gender;
  }

  public LocalDate getDOB() {
    return DOB;
  }

  public void setDOB(LocalDate dOB) {
    this.DOB = dOB;
  }

  public String getLocation() {
    return Location;
  }

  public void setLocation(String location) {
    this.Location = location;
  }

  @JsonIgnore
  public Set<Post> getPosts() {
    return posts;
  }

  public void setPosts(Set<Post> posts) {
    this.posts = posts;
  }

  @JsonIgnore
  public List<User> getFollowing() {
    return following;
  }

  public void setFollowing(List<User> following) {
    this.following = following;
  }

  public List<User> getFollowers() {
    return followers;
  }

  public void addFollower(User follower) {
    followers.add(follower);
  }
}