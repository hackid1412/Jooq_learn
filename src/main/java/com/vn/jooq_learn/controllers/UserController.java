package com.vn.jooq_learn.controllers;

import com.vn.jooq_learn.dtos.UserDto;
import com.vn.jooq_learn.entities.User;
import com.vn.jooq_learn.responses.UserLoginResponse;
import com.vn.jooq_learn.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public ResponseEntity<List<UserDto>> getListUsers() {
        return ResponseEntity.ok(userService.getListUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUserById(@PathVariable Long id, @RequestBody UserDto userDto){
        return ResponseEntity.ok(userService.updateUserById(id, userDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.deleteUserById(id));
    }

    @PostMapping("/login")
    public ResponseEntity<UserLoginResponse> userLogin(@RequestBody UserDto userDto) throws Exception {
        UserLoginResponse userLoginResponse = new UserLoginResponse();
        userLoginResponse.setStatusCode(HttpStatus.OK.toString());
        userLoginResponse.setMessage("Login successfully");
        Map<String, String> token = new HashMap<>();
        token.put("access_token", userService.userLogin(userDto.getEmail(), userDto.getPassword()));
        userLoginResponse.setData(token);
        return ResponseEntity.ok(userLoginResponse);
    }
}
