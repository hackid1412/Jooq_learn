package com.vn.jooq_learn.services.impls;

import com.vn.jooq_learn.dtos.UserDto;
import com.vn.jooq_learn.entities.User;
import com.vn.jooq_learn.mapper.UserMapper;
import com.vn.jooq_learn.repositories.UserRepository;
import com.vn.jooq_learn.services.UserService;
import org.jooq.DSLContext;
import org.jooq.generated.db_jooq_learn.Tables;
import org.jooq.generated.db_jooq_learn.tables.TblUsers;
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
    @Override
    public User createUser(UserDto userDto) {
        User newUser = UserMapper.INSTANCE.userDtoToUserJpa(userDto);
//        newUser.setFirstName(userDto.getFirstName());
//        newUser.setLastName(userDto.getLastName());
//        newUser.setEmail(userDto.getEmail());
//        newUser.setPassword(userDto.getPassword());


        userRepository.save(newUser);

        return newUser;
    }

    @Override
    public List<UserDto> getListUsers() {
        return dslContext
                .selectFrom(Tables.TBL_USERS)
                .fetchInto(TblUsersRecord.class)
                .stream().map(UserMapper.INSTANCE::userRecordToUserDto)
                .collect(Collectors.toList());
    }
}
