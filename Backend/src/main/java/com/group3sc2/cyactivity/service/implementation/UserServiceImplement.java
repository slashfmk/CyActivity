package com.group3sc2.cyactivity.service.implementation;

import com.group3sc2.cyactivity.domain.UserPrincipal;
import com.group3sc2.cyactivity.enumaration.Role;
import com.group3sc2.cyactivity.exception.domain.EmailExistsException;
import com.group3sc2.cyactivity.exception.domain.EmailNotFoundException;
import com.group3sc2.cyactivity.exception.domain.UsernameExistsException;
import com.group3sc2.cyactivity.model.Activity;
import com.group3sc2.cyactivity.model.User;
import com.group3sc2.cyactivity.repository.UserRepository;
import com.group3sc2.cyactivity.service.ActivityService;
import com.group3sc2.cyactivity.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.transaction.Transactional;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;

import static com.group3sc2.cyactivity.constant.FileConstant.*;
import static com.group3sc2.cyactivity.constant.UserImplementationConstant.*;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static org.apache.commons.lang3.StringUtils.EMPTY;

@Service
@Transactional
@Slf4j
@Qualifier("userDetailsService")
public class UserServiceImplement implements UserService, UserDetailsService {

    private Logger LOGGER = LoggerFactory.getLogger(getClass());
    private final UserRepository userRepository;
    private final ActivityService activityService;
    private final BCryptPasswordEncoder passwordEncoder;
    //  private LoginAttemptService loginAttemptService;

    @Autowired
    public UserServiceImplement(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder, ActivityService activityService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.activityService = activityService;
        //  this.loginAttemptService = loginAttemptService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = this.userRepository.findByUsername(username);

        if (user == null) {
            //  LOGGER.error("User " + username + " is not found!!");
            throw new UsernameNotFoundException(NO_USER_FOUND_BY_USERNAME + username);
        } else {
            user.setLastLoginDateDisplay(user.getLastLoginDate());
            user.setLastLoginDate(new Date());
            userRepository.save(user);
            UserPrincipal userPrincipal = new UserPrincipal(user);
            // LOGGER.info("Returning found user by username: " + username);
            return userPrincipal;
        }
    }


    // save new user
    @Override
    public User register(String firstname, String lastname, String username, String email, String password) throws UsernameExistsException, EmailExistsException {
        this.validateNewUsernameAndEmail(EMPTY, username, email);

        User user = new User();
        // user.setUserId(generateUserId());
        // String password = generatePassword();

        user.setFirstname(firstname);
        user.setLastname(lastname);
        user.setUsername(username);
        user.setEmail(email);
        user.setRegisteredDate(new Date());
        user.setPassword(encodePassword(password));
        user.setActive(true);
        user.setNotLocked(true);
        user.setRole(Role.ROLE_USER.name());
        user.setAuthorities(Role.ROLE_USER.getAuthorities());
        user.setProfileImageUrl(getTemporaryProfileImageUrl(username));
        userRepository.save(user);
        // LOGGER.info("New user password: " + password);
        return user;
    }

    @Override
    public User addNewUser(String firstname, String lastname, String username, String email, String password, String role, boolean isNonLocked, boolean isActive, MultipartFile profileImage) throws UsernameExistsException, EmailExistsException, IOException {
        this.validateNewUsernameAndEmail(EMPTY, username, email);

        User user = new User();
        // user.setUserId(generateUserId());
        // String password = generatePassword();

        user.setFirstname(firstname);
        user.setLastname(lastname);
        user.setUsername(username);
        user.setEmail(email);
        user.setRegisteredDate(new Date());
        user.setPassword(encodePassword(password));
        user.setActive(true);
        user.setNotLocked(true);
        user.setRole(Role.ROLE_USER.name());
        user.setAuthorities(Role.ROLE_USER.getAuthorities());
        user.setProfileImageUrl(getTemporaryProfileImageUrl(username));
        userRepository.save(user);
        saveProfileImage(user, profileImage);
        // LOGGER.info("New user password: " + password);
        return user;
    }

    @Override
    public User addUser(String firstname, String lastname, String username, String email, String password, String role, boolean isNonLocked, boolean isActive, MultipartFile profileImage) throws UsernameExistsException, EmailExistsException, IOException {

        validateNewUsernameAndEmail(EMPTY, username, email);

        User user = new User();

        user.setFirstname(firstname);
        user.setLastname(lastname);
        user.setRegisteredDate(new Date());
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(encodePassword(password));
        user.setActive(isActive);
        user.setNotLocked(true);
        user.setRole(getRoleEnumName(role).name());
        user.setAuthorities(getRoleEnumName(role).getAuthorities());
        user.setProfileImageUrl(getTemporaryProfileImageUrl(username));

        this.userRepository.save(user);
        saveProfileImage(user, profileImage);

        return user;
    }

    @Override
    public User updateUser(String currentUsername, String newFirstname, String newLastname, String newUsername, String newEmail, String role, boolean isNonLocked, boolean isActive, MultipartFile profileImage) throws UsernameExistsException, EmailExistsException, IOException {

        User currentUser = validateNewUsernameAndEmail(currentUsername, newUsername, newEmail);

        currentUser.setFirstname(newFirstname);
        currentUser.setLastname(newLastname);
        currentUser.setUsername(newUsername);
        currentUser.setEmail(newEmail);
        currentUser.setActive(isActive);
        currentUser.setNotLocked(true);
        currentUser.setRole(getRoleEnumName(role).name());
        currentUser.setAuthorities(getRoleEnumName(role).getAuthorities());
        this.userRepository.save(currentUser);
        saveProfileImage(currentUser, profileImage);

        return currentUser;
    }

    @Override
    public void deleteUser(Long id) {
        User foundUser = this.userRepository.findUserById(id);

        if (foundUser == null) throw new UsernameNotFoundException("user with id: " + id + " doesn't exist");
        this.userRepository.deleteById(id);
    }

    public User getUserById(long id){
        User user = userRepository.findUserById(id);
        if (user == null) throw new IllegalStateException("no user found with this id");
        return user;
    }

    @Override
    public void resetPassword(String email) throws EmailNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user == null) throw new EmailNotFoundException(NO_USER_FOUND_BY_EMAIL + email);

        String genereatedPassword = generatePassword();

        user.setPassword(encodePassword(genereatedPassword));
        this.userRepository.save(user);
    }

    @Override
    public Activity createActivity(String title, String description, boolean isApproved) {
        Activity found = this.activityService.getActivityByTitle(title);
        if (found != null) throw new IllegalStateException(title + " activity already exists");
        this.activityService.addActivity(found);
        return found;
    }

    @Override
    public User updateProfileImage(String username, MultipartFile profileImage) throws UsernameExistsException, EmailExistsException, IOException {
        User user = validateNewUsernameAndEmail(username, null, null);
        if (user == null) throw new UsernameNotFoundException(NO_USER_FOUND_BY_USERNAME + username);
        saveProfileImage(user, profileImage);
        return user;
    }

    @Override
    public List<User> getUsers() {
        return this.userRepository.findAll();
    }

    @Override
    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public User findUserByEmail(String email) {
        return this.userRepository.findByEmail(email);
    }

    private long generateUserId() {
        return RandomUtils.nextLong();
    }

    private String generatePassword() {
        return RandomStringUtils.randomAlphanumeric(10);
    }

    private String encodePassword(String password) {
        return this.passwordEncoder.encode(password);
    }

    private boolean decodePassword(String password, String encodedPassword) {
        return this.passwordEncoder.matches(password, encodedPassword);
    }

    private void saveProfileImage(User user, MultipartFile profileImage) throws IOException {
        if (profileImage != null) {
            Path userFolder = Paths.get(USER_FOLDER + user.getUsername()).toAbsolutePath().normalize();
            if (!Files.exists(userFolder)) {
                Files.createDirectories(userFolder);
            }
            Files.deleteIfExists(Paths.get(userFolder + user.getUsername() + DOT + JPG_EXTENSION));
            Files.copy(profileImage.getInputStream(), userFolder.resolve(user.getUsername() + DOT + JPG_EXTENSION), REPLACE_EXISTING);
            user.setProfileImageUrl(setProfileImageUrl(user.getUsername()));
            userRepository.save(user);
        }
    }

    private String setProfileImageUrl(String username) {
        return ServletUriComponentsBuilder.fromCurrentContextPath().path(USER_IMAGE_PATH + username + FORWARD_SLASH
                + username + DOT + JPG_EXTENSION).toUriString();
    }

    private Role getRoleEnumName(String role) {
        return Role.valueOf(role.toUpperCase());
    }

    private String getTemporaryProfileImageUrl(String username) {
        // Get the current server address + /user/image/profile/temp
        return ServletUriComponentsBuilder.fromCurrentContextPath().path(DEFAULT_USER_PROFILE_IMAGE + username).toUriString();
    }

    @Override
    public List<Activity> enrolledIn(String username) {
        User user = this.userRepository.findByUsername(username);
        return user.getActivities();
    }

    @Override
    public Activity enrollActivity(Long id, String username) {
        Activity activityExist = this.activityService.getActivityById(id);
        User user = this.userRepository.findByUsername(username);
        if (activityExist == null) throw new IllegalStateException("Activity with id " + id + " doesn't exist");

        boolean isEnrolled = user.getActivities().contains(activityExist);
        if (isEnrolled) throw new IllegalStateException("You are already enrolled in " + activityExist.getTitle());

        user.getActivities().add(activityExist);
        this.userRepository.save(user);
        return activityExist;
    }

    @Override
    public Activity unrollActivity(Long id, String username) {
        Activity activityExist = this.activityService.getActivityById(id);
        User user = this.userRepository.findByUsername(username);

        if (activityExist == null) throw new IllegalStateException("Activity with id " + id + " doesn't exist");

        boolean isEnrolled = user.getActivities().contains(activityExist);
        if (!isEnrolled) throw new IllegalStateException("You have to enroll first, in order to be unrolled");

        user.getActivities().remove(activityExist);
        this.userRepository.save(user);
        return activityExist;
    }

    // check user existence
    private User validateNewUsernameAndEmail(String currentUsername, String newUsername, String newEmail) throws UsernameExistsException, EmailExistsException {

        User userByNewUsername = findUserByUsername(newUsername);
        User userByNewEmail = findUserByEmail(newEmail);

        if (StringUtils.isNotBlank(currentUsername)) {
            User currentUser = findUserByUsername(newUsername);
            if (currentUser == null)
                throw new UsernameNotFoundException(NO_USER_FOUND_BY_USERNAME + currentUsername);

            if (userByNewUsername != null && !currentUser.getUserId().equals(userByNewUsername.getUserId()))
                throw new UsernameExistsException(USERNAME_ALREADY_EXISTS);

            if (userByNewEmail != null && !currentUser.getUserId().equals(userByNewEmail.getUserId()))
                throw new EmailExistsException(EMAIL_ALREADY_EXISTS);

            return currentUser;
        } else {

            if (userByNewUsername != null) throw new UsernameExistsException(USERNAME_ALREADY_EXISTS);
            if (userByNewEmail != null) throw new EmailExistsException(EMAIL_ALREADY_EXISTS);

            return null;
        }

    }


}
