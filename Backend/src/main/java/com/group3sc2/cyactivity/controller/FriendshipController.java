package com.group3sc2.cyactivity.controller;


import com.group3sc2.cyactivity.domain.ApiInfo;
import com.group3sc2.cyactivity.domain.HttpResponse;
import com.group3sc2.cyactivity.model.Friendship;
import com.group3sc2.cyactivity.model.User;
import com.group3sc2.cyactivity.service.FriendshipService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@RequestMapping(path = {ApiInfo.API_V1 + "/friendship"})
@RestController
public class FriendshipController {

private final FriendshipService friendshipService;

    @Autowired
    public FriendshipController(FriendshipService friendshipService) {
        this.friendshipService = friendshipService;
    }

    // Friend request
    @GetMapping("/sendrequest/{friendId}")
    public Friendship sendRequest(Authentication authentication, @PathVariable(value = "friendId") Long friendId){
        return this.friendshipService.sendRequest(authentication.getName(), friendId);
    }

    // Unfriend
    @PutMapping("/unfriend/{friendId}")
    public Friendship unfriend(Authentication authentication, @PathVariable(value = "friendId") Long friendId){
        return this.friendshipService.unfriend(authentication.getName(), friendId);
    }

    // Revoke friend request
    @PutMapping("/revokerequest/{friendId}")
    public Friendship revokeRequest(Authentication authentication, @PathVariable(value = "friendId") Long friendId){
        return this.friendshipService.revokeFriendRequest(authentication.getName(), friendId);
    }

    // List of friends
    @GetMapping("/friendlist")
    public List<User> myFriendList(Authentication authentication){
        return this.friendshipService.getFriendList(authentication.getName());
    }

    // list of pending request waiting for the current user to approve
    @GetMapping("/pendingrequest")
    public List<Friendship> pendingRequest(Authentication authentication){
        return this.friendshipService.pendingRequest(authentication.getName());
    }

    // Approve pending request from others
    @PutMapping("/approve/{requestId}")
    public Friendship approveRequest(Authentication authentication, @PathVariable(value = "requestId") Long requestId){
        return this.friendshipService.approveRequest(authentication.getName(), requestId);
    }

}
