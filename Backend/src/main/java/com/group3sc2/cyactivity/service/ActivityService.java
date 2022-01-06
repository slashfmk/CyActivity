package com.group3sc2.cyactivity.service;

import com.group3sc2.cyactivity.model.Activity;

import java.util.List;

public interface ActivityService {

    public Activity getActivityById(long id);
    public List<Activity> getAllActivities();
    public void deleteActivity(long id);
    public Activity addActivity(Activity activity);
    public Activity getActivityByTitle(String title);
    public Activity approveActivity(long id);

}
