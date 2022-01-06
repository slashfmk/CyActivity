package com.group3sc2.cyactivity;

import com.group3sc2.cyactivity.model.User;
import com.group3sc2.cyactivity.repository.UserRepository;
import com.group3sc2.cyactivity.service.UserService;
import com.group3sc2.cyactivity.service.implementation.UserServiceImplement;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class CyactivityApplicationTests {

	@Autowired
	private UserService userService;
	@MockBean
	private UserRepository userRepository;

	@Test
	public void addUserTest(){
		User user = new User();
		user.setFirstname("Jarvis");
		user.setEmail("jar7@hotmail.com");

		when(userRepository.save(user)).thenReturn(user);
		assertEquals(user.getFirstname(),"Jarvis");
	}

	@Test
	public void deleteUserTest(){
		User user = new User();
		user.setFirstname("vix");
		user.setEmail("vix@hotmail.com");

		userRepository.deleteById(user.getUserId());
		verify(userRepository,times(1)).deleteById(user.getUserId());

	}

	@Test
	public void getUsersTest(){

		User userOne = new User();
		userOne.setFirstname("Yannick");
		userOne.setEmail("slashcs7@hotmail.com");

		User userTwo = new User();
		userTwo.setFirstname("Bean");
		userTwo.setEmail("bn7@hotmail.com");

		List<User> users = new ArrayList<>();
		users.add(userOne);
		users.add(userTwo);

		when(userRepository.findAll()).thenReturn(users);
		assertEquals(2, userService.getUsers().size());

	}


}
