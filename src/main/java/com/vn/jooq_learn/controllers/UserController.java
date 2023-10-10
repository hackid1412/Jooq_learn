package com.vn.jooq_learn.controllers;

import com.vn.jooq_learn.dtos.UserDto;
import com.vn.jooq_learn.entities.User;
import com.vn.jooq_learn.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/create")
    public ResponseEntity<User> createUser(@RequestBody UserDto userDto) {
        User newUser = userService.createUser(userDto);
        return ResponseEntity.ok(newUser);
    }

    @GetMapping()
    public ResponseEntity<List<User>> getListUsers() {
        return ResponseEntity.ok(userService.getListUsers());
    }
}
