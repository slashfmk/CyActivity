package com.group3sc2.cyactivity.repository;

import com.group3sc2.cyactivity.model.Friendship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FriendshipRepository extends JpaRepository<Friendship, Long> {

    @Query("select f from Friendship f where f.requesterId =?1 and f.receiverId =?2")
    Friendship requestState(Long senderId, Long receiverId);

    @Query("select f from Friendship f where f.requesterId =?1 or f.receiverId =?1 and f.isApproved = true")
    List<Friendship> getFriendshipList(Long currentUserId);

    @Query("select f from Friendship f where f.receiverId =?1 and f.isApproved = false")
    List<Friendship> getUnapprovedFriendRequestFromOtherUsers(Long id);

    @Query("select f from Friendship f where f.friendshipId =?1")
    Friendship getFriendshipById(Long id);

}
