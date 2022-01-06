package com.group3sc2.cyactivity.controller;

import com.group3sc2.cyactivity.domain.ApiInfo;
import com.group3sc2.cyactivity.domain.HttpResponse;
import com.group3sc2.cyactivity.domain.UserPrincipal;
import com.group3sc2.cyactivity.exception.domain.*;
import com.group3sc2.cyactivity.model.Activity;
import com.group3sc2.cyactivity.model.User;
import com.group3sc2.cyactivity.service.UserService;
import com.group3sc2.cyactivity.util.JWTTokenProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.List;

import static com.group3sc2.cyactivity.constant.FileConstant.*;
import static com.group3sc2.cyactivity.constant.SecurityConstant.JWT_TOKEN_HEADER;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.IMAGE_JPEG_VALUE;

// trying to fix merge problems
@RestController
@RequestMapping(path = {ApiInfo.API_V1 + "/user"})
@Tag(name = "User")
public class UserController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JWTTokenProvider jwtTokenProvider;

    @Autowired
    public UserController(UserService userService, AuthenticationManager authenticationManager, JWTTokenProvider jwtTokenProvider) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/signin")
    @Operation(summary = "Sign in a user", responses = {
            @ApiResponse(
                    responseCode = "200",
                    description = "User signed in successfully!",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = User.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Check your username or password!!",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = User.class))
            )
    })
    public ResponseEntity<User> signIn(@RequestBody User user, Principal principal) {

        authenticate(user.getUsername(), user.getPassword());

        User loginUser = this.userService.findUserByUsername(user.getUsername());
        UserPrincipal userPrincipal = new UserPrincipal(loginUser);
        HttpHeaders jwtHeader = getJwtHeader(userPrincipal);
        return new ResponseEntity<>(loginUser, jwtHeader, OK);
    }

    @PostMapping("/signup")
    @Operation(summary = "Register a new user to the app")
    public User signUp(@RequestBody User user) throws UserNotFoundException, UsernameExistsException, EmailExistsException {
        return this.userService.register(user.getFirstname(), user.getLastname(), user.getUsername(), user.getEmail(), user.getPassword());
    }


    @Operation(summary = "List all registered users in the database",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Users loaded successfully!"),
                    @ApiResponse(responseCode = "500", description = "Something went wrong!!")
            })
    @GetMapping("/list")
    public List<User> getAllUsers() {
        return this.userService.getUsers();
    }

    @Operation(summary = "Rest the password in case that the user has forgotten his")
    @PostMapping("/resetpassword/{email}")
    public String resetPassword(@PathVariable("email") String email) throws EmailNotFoundException {
        this.userService.resetPassword(email);
                return "An Email has been sent to " + email + " with a new password";
    }


    @PostMapping("/add")
    public User addNewUser(
            @RequestParam("firstname") String firstname,
            @RequestParam("lastname") String lastname,
            @RequestParam("username") String username,
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            @RequestParam("role") String role,
            @RequestParam("isActive") String isActive,
            @RequestParam("isNonLocked") String isNonLocked,
            @RequestParam(value = "profileImage", required = false) MultipartFile profileImage
    ) throws UsernameExistsException, EmailExistsException, IOException {

        return userService.addNewUser(
                firstname,
                lastname,
                username,
                email,
                password,
                role,
                Boolean.parseBoolean(isNonLocked),
                Boolean.parseBoolean(isActive),
                profileImage
        );

    }


    @GetMapping(path = "/image/{username}/{fileName}", produces = IMAGE_JPEG_VALUE)
    public byte[] getProfileImage(@PathVariable("username") String username, @PathVariable("fileName") String filename) throws IOException {
        return Files.readAllBytes(Paths.get(USER_FOLDER + username + FORWARD_SLASH + filename));
    }

    @GetMapping(path = "/image/{profile}/{username}", produces = IMAGE_JPEG_VALUE)
    public byte[] getTempProfileImage(@PathVariable("username") String username) throws IOException {
        URL url = new URL(TEMP_PROFILE_IMAGE_BASE_URL + username);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        try (InputStream inputStream = url.openStream()) {
            int bytesRead;
            byte[] chunk = new byte[1024];
            while ((bytesRead = inputStream.read(chunk)) > 0) {
                byteArrayOutputStream.write(chunk, 0, bytesRead);
            }
        }
        return byteArrayOutputStream.toByteArray();
    }


    @PutMapping("/update")
    public User update(
            @RequestParam("currentUsername") String currentUsername,
            @RequestParam("firstname") String firstname,
            @RequestParam("lastname") String lastname,
            @RequestParam("username") String username,
            @RequestParam("email") String email,
            @RequestParam("role") String role,
            @RequestParam("isActive") String isActive,
            @RequestParam("isNonLocked") String isNonLocked,
            @RequestParam(value = "profileImage", required = false) MultipartFile profileImage
    ) throws UsernameExistsException, EmailExistsException, IOException {

        return userService.updateUser(
                currentUsername,
                firstname,
                lastname,
                username,
                email,
                role,
                Boolean.parseBoolean(isNonLocked),
                Boolean.parseBoolean(isActive),
                profileImage
        );

    }

    @GetMapping("/find/{username}")
    public User findByUsername(@PathVariable("username") String username) {
        return this.userService.findUserByUsername(username);
    }

    @GetMapping("/findbyid/{id}")
    public User findByid(@PathVariable("id") Long id) {
        return this.userService.getUserById(id);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<HttpResponse> deleteUser(@PathVariable("id") Long id) {

        this.userService.deleteUser(id);

        HttpResponse body = new HttpResponse(
                HttpStatus.ACCEPTED.value(),
                OK,
                HttpStatus.ACCEPTED.getReasonPhrase().toUpperCase(),
                "user with id: " + id + " has been deleted successfully"
        );

        return new ResponseEntity<HttpResponse>(body, body.getHttpStatus());
    }

    @PostMapping("/activities/add")
    public Activity userCreateActivity(@RequestBody Activity activity) {
        return this.userService.createActivity(activity.getTitle(), activity.getDescription(), true);

    }

    //show all user activities
    @GetMapping("/activities/enrolled")
    public List<Activity> getUserEnrolledActivity(Authentication authentication) {
        return this.userService.enrolledIn(authentication.getName());
    }

    // Activity enrollment
    @PostMapping("/activities/enroll/{activityId}")
    public Activity enrollActivity(@PathVariable("activityId") long activityId, Authentication authentication) {
        return this.userService.enrollActivity(activityId, authentication.getName());
    }

    // Activity unrollment
    @PostMapping("/activities/unroll/{activityId}")
    public Activity unrollActivity(@PathVariable("activityId") long activityId, Authentication authentication) {
        return this.userService.unrollActivity(activityId, authentication.getName());
    }


    private HttpHeaders getJwtHeader(UserPrincipal user) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(JWT_TOKEN_HEADER, jwtTokenProvider.generateJWTToken(user));
        return headers;
    }

    private void authenticate(String username, String password) {
        this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    }

}
