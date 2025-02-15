package com.zipsoon.api.exception;

import com.zipsoon.api.auth.dto.LoginRequest;
import com.zipsoon.api.auth.dto.SignupRequest;
import com.zipsoon.api.auth.service.AuthService;
import com.zipsoon.api.security.jwt.JwtProvider;
import com.zipsoon.api.user.domain.Role;
import com.zipsoon.api.user.domain.User;
import com.zipsoon.api.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceExceptionTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtProvider jwtProvider;

    @InjectMocks
    private AuthService authService;

    @Test
    @DisplayName("존재하지 않는 이메일로 로그인하면 User not found 예외 발생")
    void shouldThrowUserNotFound_When_LoginWithInvalidEmail() {
        // Given
        var request = new LoginRequest("test@test.com");
        when(userRepository.findByEmail(request.email())).thenReturn(Optional.empty());

        // When & Then
        BadCredentialsException exception = assertThrows(BadCredentialsException.class, () -> authService.login(request));

        // 예외 메시지 검증
        assertEquals("User not found", exception.getMessage());
    }

    @Test
    @DisplayName("이미 존재하는 이메일로 회원가입하면 User already exists 예외 발생")
    void shouldThrowUserDuplicate_When_SignupWithExistingEmail() {
        // Given
        var request = new SignupRequest("test@test.com", "name");
        when(userRepository.existsByEmail(request.email())).thenReturn(true);

        // When & Then
        BadCredentialsException exception = assertThrows(BadCredentialsException.class, () -> authService.signup(request));

        // 예외 메시지 검증
        assertEquals("User already exists", exception.getMessage());
    }

    @Test
    @DisplayName("잘못된 자격 증명으로 로그인하면 Invalid credentials 예외 발생")
    void shouldThrowInvalidCredentials_When_LoginWithIncorrectCredentials() {
        // Given
        var request = new LoginRequest("test@test.com");
        var user = User.builder()
            .id(1L)
            .email("test@test.com")
            .role(Role.USER)
            .build();
        when(userRepository.findByEmail(request.email())).thenReturn(Optional.of(user));
        when(jwtProvider.createAccessToken(any())).thenThrow(new BadCredentialsException("Invalid credentials"));

        // When & Then
        BadCredentialsException exception = assertThrows(BadCredentialsException.class,
            () -> authService.login(request));

        // 예외 메시지 검증
        assertEquals("Invalid credentials", exception.getMessage());
    }

}