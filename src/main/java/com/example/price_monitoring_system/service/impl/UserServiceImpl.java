package com.example.price_monitoring_system.service.impl;

import com.example.price_monitoring_system.domain.User;
import com.example.price_monitoring_system.repository.UserRepository;
import com.example.price_monitoring_system.repository.entity.UserEntity;
import com.example.price_monitoring_system.repository.mapper.UserEntityMapper;
import com.example.price_monitoring_system.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserEntityMapper userEntityMapper;

    @Override
    @Transactional
    public User saveUser(User user) {
        UserEntity userEntity = userRepository.saveAndFlush(
                userEntityMapper.toUserEntity(user));

        log.info("New User with id {} was saved", userEntity.getId());
        return userEntityMapper.toUser(userEntity);
    }
}
