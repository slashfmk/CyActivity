package com.group3sc2.cyactivity.service.implementation;

import com.group3sc2.cyactivity.model.Activity;
import com.group3sc2.cyactivity.repository.ActivityRepository;
import com.group3sc2.cyactivity.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ActivityServiceImplement implements ActivityService {

    private final ActivityRepository activityRepository;

    @Autowired
    public ActivityServiceImplement(ActivityRepository activityRepository){
        this.activityRepository = activityRepository;
    }

    @Override
    public Activity getActivityById(long id) {
        return this.activityRepository.getById(id);
    }

    @Override
    public List<Activity> getAllActivities() {
        return this.activityRepository.findAll();
    }

    @Override
    public void deleteActivity(long id) {
        this.activityRepository.deleteById(id);
    }

    @Override
    public Activity addActivity(Activity activity) {
        Activity foundActivity = this.activityRepository.findActivityByTitle(activity.getTitle());
        if(foundActivity != null)  throw new IllegalStateException("Activity "+activity.getTitle()+" already exists");

        activity.setIsApproved(false);
        this.activityRepository.save(activity);
        return activity;
    }

    @Override
    public Activity getActivityByTitle(String title) {
        return this.activityRepository.findActivityByTitle(title);
    }

    @Override
    public Activity approveActivity(long id) {
        Activity activityToApprove = this.getActivityById(id);
        if(activityToApprove == null) throw new IllegalStateException("There no activity for this "+id);
        activityToApprove.setIsApproved(true);
        this.activityRepository.save(activityToApprove);
        return activityToApprove;
    }
}
