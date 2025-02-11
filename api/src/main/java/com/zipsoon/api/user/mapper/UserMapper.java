package com.zipsoon.api.user.mapper;

import com.zipsoon.api.user.domain.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Optional;

@Mapper
public interface UserMapper {
    Optional<User> findByEmail(String email);

    Optional<User> findByProviderAndProviderId(
        @Param("provider") String provider,
        @Param("providerId") String providerId
    );

    void save(User user);

    boolean existsByEmail(String email);
}