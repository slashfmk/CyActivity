package com.group3sc2.cyactivity.model;

import javax.persistence.*;
import java.util.Date;


@Entity
@Table
public class Friendship {

    @Id
    @SequenceGenerator(
            name = "friendship_sequence",
            sequenceName = "friendship_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "friendship_sequence"
    )
    @Column(nullable = false, updatable = false)
    private Long friendshipId;
    @Column(nullable = false)
    private Long requesterId;
    @Column(nullable = false)
    private Long receiverId;
    @Column(nullable = false)
    private Boolean isApproved;
    @Column(nullable = false)
    private Date  requestDate;

    public Friendship(Long friendshipId, Long requesterId, Long receiverId, Boolean isApproved, Date requestDate) {
        this.friendshipId = friendshipId;
        this.requesterId = requesterId;
        this.receiverId = receiverId;
        this.isApproved = isApproved;
        this.requestDate = requestDate;
    }

    public Friendship(Long requesterId, Long receiverId, Boolean isApproved, Date requestDate) {
        this.requesterId = requesterId;
        this.receiverId = receiverId;
        this.isApproved = isApproved;
        this.requestDate = requestDate;
    }

    public Friendship() {
    }

    public Long getFriendshipId() {
        return friendshipId;
    }

    public void setFriendshipId(Long friendshipId) {
        this.friendshipId = friendshipId;
    }

    public Long getRequesterId() {
        return requesterId;
    }

    public void setRequesterId(Long requesterId) {
        this.requesterId = requesterId;
    }

    public Long getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Long receiverId) {
        this.receiverId = receiverId;
    }

    public Boolean getIsApproved() {
        return isApproved;
    }

    public void setIsApproved(Boolean friend) {
        isApproved = friend;
    }

    public Date getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(Date requestDate) {
        this.requestDate = requestDate;
    }

}
