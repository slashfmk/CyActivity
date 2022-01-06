package com.group3sc2.cyactivity.repository;
import com.group3sc2.cyactivity.model.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository <User, Long> {

    @Query("select u from User u where u.userId = ?1 ")
    User findUserById(Long id);

    @Query("select u from User u where u.email = ?1")
    User findByEmail(String email);

    @Query("select u from User u where u.username = ?1")
    User findByUsername(String username);

    @Query("delete from User u where u.userId = ?1 ")
    void deleteById(Long id);

    @Query("select u from User u where u.username = ?1 and u.password = ?2")
    User findUserByUsernameAndPassword(String username, String password);
}
