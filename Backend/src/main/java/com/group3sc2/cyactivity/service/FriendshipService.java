package com.group3sc2.cyactivity.service;

import com.group3sc2.cyactivity.model.Friendship;
import com.group3sc2.cyactivity.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

public interface FriendshipService {

    public Friendship sendRequest(String currentUsername, long id);
    public List<User> getFriendList(String currentUsername);
    public Friendship revokeFriendRequest(String currentUsername, long id);
    public Friendship unfriend(String currentUsername, long id);
    public List<Friendship> pendingRequest(String currentUsername);
    public Friendship approveRequest(String currentUsername, long friendshipId);
}
