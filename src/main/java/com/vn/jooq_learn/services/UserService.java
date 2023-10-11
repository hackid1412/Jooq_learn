package com.vn.jooq_learn.services;

import com.vn.jooq_learn.dtos.UserDto;
import com.vn.jooq_learn.entities.User;

import java.util.List;

public interface UserService {
    User createUser(UserDto userDto); // create a new user

    List<UserDto> getListUsers(); // get list users

    UserDto getUserById(Long id); // get user by id

    UserDto updateUserById(Long id, UserDto userDto); // update user by id

    String deleteUserById(Long id); // delete user by id

    String userLogin(String email, String password) throws Exception; // user login
}
