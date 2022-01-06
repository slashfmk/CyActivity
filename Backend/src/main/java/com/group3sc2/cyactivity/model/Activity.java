package com.group3sc2.cyactivity.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table
public class Activity {

    @Id
    @SequenceGenerator(
            name = "activity_sequence",
            sequenceName = "activity_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "activity_sequence"
    )
    private long activityId;
    private String title;
    private String description;
    private boolean isApproved;

    @JsonIgnoreProperties("activities")
    @ManyToMany(mappedBy = "activities", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<User> users = new ArrayList<>();

   // private User user;
    public Activity(long activityId, String title, String description, boolean isApproved) {
        this.activityId = activityId;
        this.title = title;
        this.description = description;
        this.isApproved = isApproved;
      //  this.user = user;
    }

    public Activity(String title, String description, boolean isApproved) {
        this.title = title;
        this.description = description;
        this.isApproved = isApproved;
     //   this.user = user;
    }

    public Activity() {
    }

    public long getActivityId() {
        return activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean getIsApproved() {
        return isApproved;
    }

    public void setIsApproved(boolean approved) {
        isApproved = approved;
    }


}
