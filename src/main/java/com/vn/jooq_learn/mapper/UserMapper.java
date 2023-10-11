package com.vn.jooq_learn.mapper;

import com.vn.jooq_learn.dtos.UserDto;
import com.vn.jooq_learn.entities.User;
import org.jooq.generated.db_jooq_learn.tables.records.TblUsersRecord;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    // update an existing User from a UserDto
    void updateUserFromDto(UserDto userDto, @MappingTarget User user); // jpa

    // mapping to dto
    UserDto userJpaToUserDto(User user);    // jpa
    UserDto userRecordToUserDto(TblUsersRecord tblUsersRecord); // jooq

    // mapping to entity
    User userDtoToUserJpa(UserDto userDto);   // jpa
    TblUsersRecord userDtoToUserRecord(UserDto userDto); // jooq
}
