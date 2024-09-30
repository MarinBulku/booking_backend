package com.algorhythm.booking_backend.application.mapper;
import com.algorhythm.booking_backend.adapter.in.models.user.UserDto;
import com.algorhythm.booking_backend.core.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "name", source = "fullName")
    @Mapping(target = "roleId", source = "role.roleId")
    @Mapping(target = "role", source = "role.role")
    UserDto toUserDto(User u);

    List<UserDto> toUserDtoList(List<User> users);

}
