package com.example.price_monitoring_system.repository.mapper;

import com.example.price_monitoring_system.domain.User;
import com.example.price_monitoring_system.repository.entity.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserEntityMapper {
    User toUser(UserEntity userEntity);
    UserEntity toUserEntity(User user);
}
