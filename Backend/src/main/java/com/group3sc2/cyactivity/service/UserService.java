package com.group3sc2.cyactivity.service;

import com.group3sc2.cyactivity.exception.domain.EmailExistsException;
import com.group3sc2.cyactivity.exception.domain.EmailNotFoundException;
import com.group3sc2.cyactivity.exception.domain.UsernameExistsException;
import com.group3sc2.cyactivity.model.Activity;
import com.group3sc2.cyactivity.model.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface UserService {

    public User register(String firstname, String lastname, String username, String email, String password) throws UsernameExistsException, EmailExistsException;

    public List<User> getUsers();

    public User findUserByUsername(String username);

    public User findUserByEmail(String email);

    public User addUser(
            String firstname,
            String lastname,
            String username,
            String email,
            String password,
            String role,
            boolean isNonLocked,
            boolean isActive,
            MultipartFile profileImage
    ) throws UsernameExistsException, EmailExistsException, IOException;

    public  User addNewUser(
            String firstname,
            String lastname,
            String username,
            String email,
            String password,
            String role,
            boolean isNonLocked,
            boolean isActive,
            MultipartFile profileImage
    ) throws UsernameExistsException, EmailExistsException, IOException;

    public User updateUser(
            String currentUsername,
            String newFirstname,
            String newLastname,
            String newUsername,
            String newEmail,
            String role,
            boolean isNonLocked,
            boolean isActive,
            MultipartFile profileImage
    ) throws UsernameExistsException, EmailExistsException, IOException;

    public void deleteUser(Long id);
    public  User getUserById(long id);

    public void resetPassword(String email) throws EmailNotFoundException;

    public Activity enrollActivity(Long id, String username);
    public Activity unrollActivity(Long id, String username);
    public List<Activity> enrolledIn(String username);

    public Activity createActivity(String title, String description, boolean isApproved);

    public User updateProfileImage(String username, MultipartFile profileImage) throws UsernameExistsException, EmailExistsException, IOException;;

}
