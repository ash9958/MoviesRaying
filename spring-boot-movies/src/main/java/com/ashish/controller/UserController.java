package com.ashish.controller;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ashish.entity.User;
import com.ashish.execption.ResourceNotFoundException;
import com.ashish.repository.UserRepo;

@RestController
public class UserController {

	@Autowired
	UserRepo myusers;
	
	
	
	@GetMapping("/users")
	public List<User> getAllUsers ()
	{
		return myusers.findAll();
	}
	
	@GetMapping("/users/{id}")
	public User getUser(@PathVariable long userId)
	{
		
		return myusers.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not Found with id :"+userId));
	}
	
	@PostMapping("/users")
	public User saveUser(@RequestBody User user)
	{
		return myusers.save(user);
	}
	
	@PutMapping("/users")
	public User updateUser(@RequestBody User user)
	{
		return myusers.save(user);
	}
	
}
