package com.group3sc2.cyactivity.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table
public class User implements Serializable {

    @Id
    @SequenceGenerator(
            name = "user_sequence",
            sequenceName = "user_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "user_sequence"
    )
    @Column(nullable = false, updatable = false)
    private Long userId;
    private String firstname;
    private String lastname;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String username;

    private boolean isActive;

    @Column(nullable = false)
    private String password;

    private Date registeredDate;
    private boolean isNotLocked;
    private String[] authorities;
    private String role;

    private Date lastLoginDateDisplay;
    private Date lastLoginDate;

    private String profileImageUrl;

    @JsonIgnoreProperties("users")
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "Enrollment",
            joinColumns = @JoinColumn(
                    name = "user_id",
                    referencedColumnName = "userId"
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "activity_id",
                    referencedColumnName = "activityId"
            )
    )
    private List<Activity> activities;


//    @ManyToMany(fetch = FetchType.LAZY)
//    @JoinTable(
//            name = "friendship",
//            joinColumns = @JoinColumn(name = "fkIdUser", referencedColumnName = "senderId", nullable = false),
//            inverseJoinColumns = @JoinColumn(name = "fkIdFriend", referencedColumnName = "receiverId", nullable = false)
//    )
//    private List<User> friends;
//
//
//    public List<User> getFriends() {
//        return friends;
//    }
//
//    public void setFriends(List<User> friends) {
//        this.friends = friends;
//    }

    public User(
            Long userId,
            String firstname,
            String lastname,
            String email,
            String username,
            boolean isActive,
            String password,
            Date registeredDate,
            boolean isNotLocked,
            String[] authorities,
            String role,
            Date lastLoginDateDisplay,
            Date lastLoginDate
    ) {
        this.userId = userId;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.username = username;
        this.isActive = isActive;
        this.password = password;
        this.registeredDate = registeredDate;
        this.isNotLocked = isNotLocked;
        this.authorities = authorities;
        this.role = role;
        this.lastLoginDateDisplay = lastLoginDateDisplay;
        this.lastLoginDate = lastLoginDate;
        this.activities = new ArrayList<>();
      //  this.friends = new ArrayList<>();
    }


    public User(
            String firstname,
            String lastname,
            String email,
            String username,
            boolean isActive,
            String password,
            Date registeredDate,
            boolean isNotLocked,
            String[] authorities,
            String role,
            Date lastLoginDateDisplay,
            Date lastLoginDate
    ) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.username = username;
        this.isActive = isActive;
        this.password = password;
        this.registeredDate = registeredDate;
        this.isNotLocked = isNotLocked;
        this.authorities = authorities;
        this.role = role;
        this.lastLoginDateDisplay = lastLoginDateDisplay;
        this.lastLoginDate = lastLoginDate;
        this.activities = new ArrayList<>();
      //  this.friends = new ArrayList<>();
    }

    public User(String firstname, String email) {
        this.firstname = firstname;
        this.email = email;
    }


    public User() {
    }

    public String[] getAuthorities() {
        return authorities;
    }

    public void setAuthorities(String[] authorities) {
        this.authorities = authorities;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getRegisteredDate() {
        return registeredDate;
    }

    public void setRegisteredDate(Date registeredDate) {
        this.registeredDate = registeredDate;
    }

    public List<Activity> getActivities() {
        return activities;
    }

    public void setActivities(List<Activity> activities) {
        this.activities = activities;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(long user_id) {
        this.userId = user_id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public boolean isNotLocked() {
        return isNotLocked;
    }

    public void setNotLocked(boolean notLocked) {
        isNotLocked = notLocked;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String roles) {
        this.role = roles;
    }

    public Date getLastLoginDateDisplay() {
        return lastLoginDateDisplay;
    }

    public void setLastLoginDateDisplay(Date lastLoginDateDisplay) {
        this.lastLoginDateDisplay = lastLoginDateDisplay;
    }

    public Date getLastLoginDate() {
        return lastLoginDate;
    }

    public void setLastLoginDate(Date lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }
}
