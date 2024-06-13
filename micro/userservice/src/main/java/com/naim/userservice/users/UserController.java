package com.naim.userservice.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private final UserModelAssembler userAssembler;

    public UserController(UserModelAssembler userAssembler) {
        this.userAssembler = userAssembler;
    }


    //done
    @GetMapping   
    public List<EntityModel<User>> all() {
        List<User> users = userRepository.findAll();
            return users.stream()
            .map(userAssembler::toModel)
            .collect(Collectors.toList());
    }

    //done
    @PostMapping 
    public ResponseEntity<EntityModel<User>> createUser(@RequestBody @Valid User user) {
        User newUser = userRepository.save(user);
            return ResponseEntity.created(linkTo(methodOn(UserController.class).one(newUser.getUserID())).toUri()).body(userAssembler.toModel(newUser));
    }

    //done
    @GetMapping("/{id}") 
    public EntityModel<User> one(@PathVariable Long id) {
        User user = userRepository.findById(id)
        .orElseThrow(() -> new UserNotFoundException(id));
            return userAssembler.toModel(user);
    }

    //done //update method for updating a single field of the user
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<User>> updateUser(@PathVariable Long id, @Valid @RequestBody User newUser) {
        User existingUser = userRepository.findById(id)
        .orElseThrow(() -> new UserNotFoundException(id));
    
        existingUser.setUsername(newUser.getUsername());
        existingUser.setAge(newUser.getAge());
        existingUser.setBio(newUser.getBio());
        existingUser.setDOB(newUser.getDOB());
        existingUser.setemail(newUser.getemail());
        existingUser.setGender(newUser.getGender());
        existingUser.setLocation(newUser.getLocation());
        existingUser.setPassword(newUser.getPassword());
        existingUser.setProfilePic(newUser.getProfilePic());
    
        User updatedUser = userRepository.save(existingUser);
    
            return ResponseEntity.ok(userAssembler.toModel(updatedUser));
    }

    //done
    @DeleteMapping("/{id}") 
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id);
            return ResponseEntity.noContent().build();
    }

    //done// Get a user's email by ID
    @GetMapping("/{userId}/email")
    public ResponseEntity<String> getUserEmailById(@PathVariable Long userId) {
        User user = userRepository.findById(userId)
        .orElseThrow(() -> new UserNotFoundException(userId));
        String userEmail = user.getemail();
            return ResponseEntity.ok(userEmail);
    }

    //done // Get a user's age by ID 
    @GetMapping("/{id}/age")
    public ResponseEntity<Integer> getUserAge(@PathVariable Long id) {
    User user = userRepository.findById(id)
        .orElseThrow(() -> new UserNotFoundException(id));
    return ResponseEntity.ok(user.getAge());
    }

    //done // Get a user's username by ID 
    @GetMapping("/{userId}/username")
    public ResponseEntity<String> getUsernameById(@PathVariable Long userId) {
        User user = userRepository.findById(userId)
        .orElseThrow(() -> new UserNotFoundException(userId));
        
        String username = user.getUsername();
            return ResponseEntity.ok(username);
    }

    @GetMapping("/users/username/{username}")
    public ResponseEntity<User> getUsersByUsername(@PathVariable String username) {
        User user = userRepository.findOneUserByUsername(username);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(user);
    }

    //done // Get a user's profile picture URL by ID 
    @GetMapping("/{userId}/profile-pic")
    public ResponseEntity<String> getProfilePictureUrlById(@PathVariable Long userId) {
        User user = userRepository.findById(userId)
        .orElseThrow(() -> new UserNotFoundException(userId));
        
        String profilePicUrl = user.getProfilePic();
            return ResponseEntity.ok(profilePicUrl);
    }

    //done // Get a user's pass by ID 
    @GetMapping("/{userId}/pass")
    public ResponseEntity<String> getpassById(@PathVariable Long userId) {
        User user = userRepository.findById(userId)
        .orElseThrow(() -> new UserNotFoundException(userId));
        
        String pass = user.getPassword();
            return ResponseEntity.ok(pass);
    }

    //done // Get a user's BIO by ID 
    @GetMapping("/{userId}/bio")
    public ResponseEntity<String> getBioById(@PathVariable Long userId) {
        User user = userRepository.findById(userId)
        .orElseThrow(() -> new UserNotFoundException(userId));
        
        String userbio = user.getBio();
            return ResponseEntity.ok(userbio);
    }

    //done // Get a user's gender by ID 
    @GetMapping("/{userId}/gender")
    public ResponseEntity<Gender> getGenderById(@PathVariable Long userId) {
        User user = userRepository.findById(userId)
        .orElseThrow(() -> new UserNotFoundException(userId));
        
        Gender usergender = user.getGender();
            return ResponseEntity.ok(usergender);
    }

    //done // Get a user's dob by ID 
    @GetMapping("/{userId}/dob")
    public ResponseEntity<LocalDate> getDOBById(@PathVariable Long userId) {
        User user = userRepository.findById(userId)
        .orElseThrow(() -> new UserNotFoundException(userId));
        
        LocalDate userdob = user.getDOB();
            return ResponseEntity.ok(userdob);
    }

    //done // Get a user's location by ID 
    @GetMapping("/{userId}/location")
    public ResponseEntity<String> getlocationById(@PathVariable Long userId) {
        User user = userRepository.findById(userId)
        .orElseThrow(() -> new UserNotFoundException(userId));
        
        String userlocation = user.getLocation();
            return ResponseEntity.ok(userlocation);
    }

    //done // Method to get the list of users that follow a user
    @GetMapping("/{id}/followers")
    public ResponseEntity<List<User>> getFollowers(@PathVariable Long id) {
        User user = userRepository.findById(id)
        .orElseThrow(() -> new UserNotFoundException(id));

        List<User> followers = user.getFollowers();
            return ResponseEntity.ok(followers);
    }

    //done // Method to get the count of followers for a user
    @GetMapping("/{id}/followers/count")
    public ResponseEntity<Long> getFollowersCount(@PathVariable Long id) {
        User user = userRepository.findById(id)
        .orElseThrow(() -> new UserNotFoundException(id));

        Long followersCount = (long) user.getFollowers().size();
            return ResponseEntity.ok(followersCount);
    }

    //done // Method to get the count of users that a user is following
    @GetMapping("/{id}/following/count")
    public ResponseEntity<Long> getFollowingCount(@PathVariable Long id) {
        User user = userRepository.findById(id)
        .orElseThrow(() -> new UserNotFoundException(id));

        Long followingCount = (long) user.getFollowing().size();
            return ResponseEntity.ok(followingCount);
    }

    //done // Method to follow a user
    @PostMapping("/{id}/follow/{followId}")
    public ResponseEntity<?> followUser(@PathVariable Long id, @PathVariable Long followId) {
        User user = userRepository.findById(id)
        .orElseThrow(() -> new UserNotFoundException( id));
        User followUser = userRepository.findById(followId)
        .orElseThrow(() -> new UserNotFoundException( followId));

        if (!user.getFollowing().contains(followUser)) {
            user.getFollowing().add(followUser);
            userRepository.save(user);
        }

            return ResponseEntity.ok().build();
    }

    //done // Method to unfollow a user
    @DeleteMapping("/{id}/unfollow/{followId}")
    public ResponseEntity<?> unfollowUser(@PathVariable Long id, @PathVariable Long followId) {
        User user = userRepository.findById(id)
        .orElseThrow(() -> new UserNotFoundException( id));
        User followUser = userRepository.findById(followId)
        .orElseThrow(() -> new UserNotFoundException( followId));

        user.getFollowing().remove(followUser);
        userRepository.save(user);

            return ResponseEntity.ok().build();
    }

    //done // Method to get the list of users that a user is following
    @GetMapping("/{id}/following")
    public ResponseEntity<List<User>> getFollowing(@PathVariable Long id) {
        User user = userRepository.findById(id)
        .orElseThrow(() -> new UserNotFoundException(id));

        List<User> following = user.getFollowing();
            return ResponseEntity.ok(following);
    }  
}