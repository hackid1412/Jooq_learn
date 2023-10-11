package com.vn.jooq_learn.services;

import com.vn.jooq_learn.dtos.UserDto;
import com.vn.jooq_learn.entities.User;

import java.util.List;

public interface UserService {
    User createUser(UserDto userDto);

    List<UserDto> getListUsers();
}
