package com.vn.jooq_learn.services.impls;

import com.vn.jooq_learn.dtos.UserDto;
import com.vn.jooq_learn.entities.User;
import com.vn.jooq_learn.mapper.UserMapper;
import com.vn.jooq_learn.repositories.UserRepository;
import com.vn.jooq_learn.services.UserService;
import org.jooq.DSLContext;
import org.jooq.generated.db_jooq_learn.Tables;
import org.jooq.generated.db_jooq_learn.tables.records.TblUsersRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private DSLContext dslContext;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;
    @Override
    public User createUser(UserDto userDto) {
        User newUser = userMapper.INSTANCE.userDtoToUserJpa(userDto);
        userRepository.save(newUser);

        return newUser;
    }

    @Override
    public List<UserDto> getListUsers() {
        return dslContext
                .selectFrom(Tables.TBL_USERS)
                .fetchInto(TblUsersRecord.class)
                .stream().map(userMapper.INSTANCE::userRecordToUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new RuntimeException("User not found")
        );
        UserDto userDto = userMapper.INSTANCE.userJpaToUserDto(user);
        return userDto;
    }

    @Override
    public UserDto updateUserById(Long id, UserDto userDto) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new RuntimeException("User not found")
        );
        userDto.setId(id);
        userMapper.INSTANCE.updateUserFromDto(userDto, user);
        userRepository.save(user);
        return userDto;
    }

    @Override
    public String deleteUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new RuntimeException("User not found")
        );
        userRepository.delete(user);
        return "Delete user successfully";
    }
}
