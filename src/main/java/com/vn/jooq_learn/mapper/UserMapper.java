package com.vn.jooq_learn.mapper;

import com.vn.jooq_learn.dtos.UserDto;
import com.vn.jooq_learn.entities.User;
import org.jooq.generated.db_jooq_learn.tables.records.TblUsersRecord;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    // mapping to dto
    UserDto userJpaToUserDto(User user);    // jpa
    UserDto userRecordToUserDto(TblUsersRecord tblUsersRecord); // jooq

    // mapping to entity
    User userDtoToUserJpa(UserDto userDto);   // jpa
    TblUsersRecord userDtoToUserRecord(UserDto userDto); // jooq
}
