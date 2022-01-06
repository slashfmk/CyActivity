package com.group3sc2.cyactivity.listener;


import com.group3sc2.cyactivity.model.User;
import com.group3sc2.cyactivity.service.LoginAttemptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationSuccessListener {

    private final LoginAttemptService loginAttemptService;

    //@Autowired
    public AuthenticationSuccessListener() {
        this.loginAttemptService = new LoginAttemptService();
    }

    @EventListener
    public void onAuthenticationSuccess(AuthenticationSuccessEvent event){
        Object principal = event.getAuthentication().getPrincipal();
        if(principal instanceof User){
            User user = (User) event.getAuthentication().getPrincipal();
            loginAttemptService.evictUserFromLoginAttemptCache(user.getUsername());
        }
    }


}
