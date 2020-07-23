package com.ashish.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ashish.entity.User;

public interface UserRepo extends JpaRepository<User, Long> {

}
