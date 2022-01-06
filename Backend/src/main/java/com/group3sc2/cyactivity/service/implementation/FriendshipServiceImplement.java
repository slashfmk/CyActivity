package com.group3sc2.cyactivity.service.implementation;

import com.group3sc2.cyactivity.model.Friendship;
import com.group3sc2.cyactivity.model.User;
import com.group3sc2.cyactivity.repository.FriendshipRepository;
import com.group3sc2.cyactivity.repository.UserRepository;
import com.group3sc2.cyactivity.service.FriendshipService;
import com.group3sc2.cyactivity.util.DateFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class FriendshipServiceImplement implements FriendshipService {

    private final FriendshipRepository friendshipRepository;
    private final UserRepository userRepository;

    @Autowired
    public FriendshipServiceImplement(FriendshipRepository friendshipRepository, UserRepository userRepository) {
        this.friendshipRepository = friendshipRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Friendship sendRequest(String currentUsername, long id) {
        User currentUser = this.userRepository.findByUsername(currentUsername);
        User friend = this.userRepository.findUserById(id);
        if(currentUser.getUserId() == id) throw new IllegalStateException("You cannot send a friend request to yourself");
        if (friend == null) throw new IllegalStateException("A friend request can't be sent to a non existing user");

        Friendship friendship = this.friendshipRepository.requestState(currentUser.getUserId(), friend.getUserId());

        if (friendship != null) {
            if (friendship.getIsApproved()) {
                throw new IllegalStateException("You are already friend with " + friend.getFirstname());
            } else {
                throw new IllegalStateException("Your friend request is pending since "+ DateFormatter.formattedDate(friendship.getRequestDate())+", still waiting for " + friend.getFirstname() + " to approve it");
            }
        }

        Friendship newFriendship = new Friendship(currentUser.getUserId(), friend.getUserId(), false, new Date());
        this.friendshipRepository.save(newFriendship);

        return newFriendship;
    }

    // Get all the friends of the current user
    @Override
    public List<User> getFriendList(String currentUsername) {
        User currentUser = this.userRepository.findByUsername(currentUsername);
        List<Friendship> friendshipList = this.friendshipRepository.getFriendshipList(currentUser.getUserId());
        List<User> friendList = new ArrayList<>();

        for(Friendship f : friendshipList){
            if(f.getRequesterId() == currentUser.getUserId()){
                friendList.add(this.userRepository.findUserById(f.getReceiverId()));
            } else if(f.getReceiverId() == currentUser.getUserId()) {
                friendList.add(this.userRepository.findUserById(f.getRequesterId()));
            }
        }


        return friendList;
    }

    @Override
    public Friendship revokeFriendRequest(String currentUsername, long id) {
        User currentUser = this.userRepository.findByUsername(currentUsername);
        User friend = this.userRepository.findUserById(id);

        if (friend == null) throw new IllegalStateException("You cannot revoke a friend request from a non existing user!!!");

        Friendship friendship = this.friendshipRepository.requestState(currentUser.getUserId(), friend.getUserId());

        if (friendship == null) throw new IllegalStateException("There is no pending request from you to revoke from this user!!!");

        if (friendship.getIsApproved()) throw new IllegalStateException("Your are already friend since "+
                DateFormatter.formattedDate(friendship.getRequestDate())+", no pending request to revoke!!");

        if (!friendship.getIsApproved()) this.friendshipRepository.deleteById(friendship.getFriendshipId());
      //  this.friendshipRepository.save();

        return friendship;
    }

    @Override
    public Friendship unfriend(String currentUsername, long id) {
        User currentUser = this.userRepository.findByUsername(currentUsername);
        User friend = this.userRepository.findUserById(id);

        if (friend == null) throw new IllegalStateException("You cannot unfriend from a non existing user!!!");

        Friendship friendship = this.friendshipRepository.requestState(currentUser.getUserId(), friend.getUserId());

        if (friendship == null) throw new IllegalStateException("You cannot unfriend from this user, you have never been friend before!!!");

        if (!friendship.getIsApproved()) this.friendshipRepository.deleteById(friendship.getFriendshipId());
        if (friendship.getIsApproved()) this.friendshipRepository.deleteById(friendship.getFriendshipId());

        return friendship;
    }

    // display unapproved sent request to the current user

    @Override
    public List<Friendship> pendingRequest(String currentUsername) {

        User currentUser = this.userRepository.findByUsername(currentUsername);
        List<Friendship> pendingFriendshipRequest = this.friendshipRepository.getUnapprovedFriendRequestFromOtherUsers(currentUser.getUserId());

        List<User> unapprovedfriendList = new ArrayList<>();

//        for(Friendship f : friendshipList){
//                friendList.add(this.userRepository.findUserById(f.getReceiverId()));
//        }
        return pendingFriendshipRequest;
    }

    // Approve request
    @Override
    public Friendship approveRequest(String currentUsername, long friendshipId) {
        User currentUser = this.userRepository.findByUsername(currentUsername);
        Friendship friendship = this.friendshipRepository.getFriendshipById(friendshipId);

        if(friendship == null) throw new IllegalStateException("There is no such request to approve!");

        if(friendship.getReceiverId() != currentUser.getUserId()) throw new IllegalStateException("You can't approve this request, because it doesn't belong to you!");
        if(friendship.getIsApproved()) throw new IllegalStateException("You already approved this request!");

        friendship.setIsApproved(true);
        friendshipRepository.save(friendship);

        return friendship;
    }
}
