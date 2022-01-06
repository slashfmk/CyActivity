package com.group3sc2.cyactivity.repository;

import com.group3sc2.cyactivity.model.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ActivityRepository extends JpaRepository <Activity, Long> {

    @Query("select a from Activity a where a.title =?1")
    Activity findActivityByTitle(String title);
}
