package com.vn.jooq_learn.services.impls;

import com.vn.jooq_learn.dtos.UserDto;
import com.vn.jooq_learn.entities.User;
import com.vn.jooq_learn.exceptions.DataNotFoundException;
import com.vn.jooq_learn.mapper.UserMapper;
import com.vn.jooq_learn.repositories.UserRepository;
import com.vn.jooq_learn.securities.JwtTokenUtils;
import com.vn.jooq_learn.services.UserService;
import com.vn.jooq_learn.utils.LocalizationUtils;
import com.vn.jooq_learn.utils.MessageKeys;
import org.jooq.DSLContext;
import org.jooq.generated.db_jooq_learn.Tables;
import org.jooq.generated.db_jooq_learn.tables.records.TblUsersRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private DSLContext dslContext;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    LocalizationUtils localizationUtils;

    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtils jwtTokenUtils;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(AuthenticationManager authenticationManager, JwtTokenUtils jwtTokenUtils, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtils = jwtTokenUtils;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User createUser(UserDto userDto) {
        User newUser = userMapper.INSTANCE.userDtoToUserJpa(userDto);
        String encodedPassword = passwordEncoder.encode(userDto.getPassword());
        newUser.setPassword(encodedPassword);
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

    @Override
    public String userLogin(String email, String password) throws Exception {
        Optional<User> user = userRepository.findUserByEmail(email);

        if (user.isEmpty()) {
            throw new DataNotFoundException(localizationUtils
                    .getLocalizedMessage(MessageKeys.WRONG_EMAIL_PASSWORD));
        }

        User existedUser = user.get();
        if(!passwordEncoder.matches(password, existedUser.getPassword())){
            throw new BadCredentialsException(localizationUtils
                    .getLocalizedMessage(MessageKeys.WRONG_EMAIL_PASSWORD));
        }
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(
                        email,
                        password
                );
        //authenticate with Java Spring security
        authenticationManager.authenticate(authenticationToken);
        String token = jwtTokenUtils.generateToken(existedUser);
        return token;
    }
}
