package com.group3sc2.cyactivity.controller;


import com.group3sc2.cyactivity.domain.ApiInfo;
import com.group3sc2.cyactivity.model.Activity;
import com.group3sc2.cyactivity.model.User;
import com.group3sc2.cyactivity.service.ActivityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "Activities")
@RequestMapping(ApiInfo.API_V1+"/activity")
public class ActivityController {

    private final ActivityService activityService;

    @Autowired
    public ActivityController(ActivityService activityService) {
        this.activityService = activityService;
    }

    @Operation(summary = "list of all activities", responses = {
            @ApiResponse(
                    responseCode = "200",
                    description = "All activities successfully loaded!",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Activity.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Something went wrong!!",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Activity.class))
            )
    })
    @GetMapping("/list")
    public List<Activity> activityList(){
        return this.activityService.getAllActivities();
    }

    @Operation(summary = "Get activity by its id", responses = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Activities received successfully!",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Activity.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "No activity for the provided url!!",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Activity.class))
            )
    })
    @GetMapping("/{id}")
    public Activity getActivity(@PathVariable("id") Long id){
        return this.activityService.getActivityById(id);
    }


    @Operation(summary = "Add a new activity", responses = {
            @ApiResponse(
                    responseCode = "200",
                    description = "New Activities added successfully!",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Activity.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Activity already exists!!",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Activity.class))
            )
    })
    @PostMapping("/add")
    public Activity addActivity(@RequestBody Activity activity){
        this.activityService.addActivity(activity);
        return activity;
    }

    @PutMapping("/approve/{activityId}")
    public Activity approveActivity(@PathVariable(value = "activityId") Long activityId){
        return this.activityService.approveActivity(activityId);
    }
}
