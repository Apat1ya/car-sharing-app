package mate.carsharingapp.service;

import static mate.carsharingapp.util.TestUtil.createUserDtoForTest;
import static mate.carsharingapp.util.TestUtil.createUserForTests;
import static mate.carsharingapp.util.TestUtil.createUserResponseDto;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import mate.carsharingapp.dto.user.UserResponseDto;
import mate.carsharingapp.dto.user.UserUpdateProfileDto;
import mate.carsharingapp.exception.EntityNotFoundException;
import mate.carsharingapp.mapper.UserMapper;
import mate.carsharingapp.model.user.User;
import mate.carsharingapp.repository.user.UserRepository;
import mate.carsharingapp.service.user.UserServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;
    @InjectMocks
    private UserServiceImpl userService;
    @Mock
    private Authentication authentication;
    @Mock
    private SecurityContext securityContext;

    @Test
    @DisplayName("get current user with valid email")
    void getCurrentUser_WithValidEmail_Ok() {
        User user = createUserForTests();

        when(authentication.getName()).thenReturn(user.getEmail());
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        UserResponseDto expected = createUserDtoForTest();
        when(userMapper.toResponseDto(user)).thenReturn(expected);

        UserResponseDto actual = userService.getCurrentUser();

        verify(userRepository).findByEmail(user.getEmail());
        verify(userMapper).toResponseDto(user);
    }

    @Test
    @DisplayName("get current user with invalid email should throw EntityNotFoundException")
    void getCurrentUser_WithInvalidEmail_NotOk() {
        String invalidEmail = "test@test.com";
        when(authentication.getName()).thenReturn(invalidEmail);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(userRepository.findByEmail(invalidEmail)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class,
                () -> userService.getCurrentUser());
        verify(userRepository).findByEmail(invalidEmail);
    }

    @Test
    @DisplayName("Update your profile test")
    public void updateUserProfile_WithValidRequest_ok() {
        UserResponseDto responseDto = createUserResponseDto();
        responseDto.setEmail("update@email.com");
        responseDto.setFirstName("update");
        responseDto.setLastName("update");
        User beforeUpdate = createUserForTests();

        when(authentication.getName()).thenReturn(beforeUpdate.getEmail());
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(userRepository.findByEmail(beforeUpdate.getEmail()))
                .thenReturn(Optional.of(beforeUpdate));
        UserUpdateProfileDto request = new UserUpdateProfileDto("update@email.com",
                "update","update");
        doNothing().when(userMapper).updateUserFromDto(request, beforeUpdate);
        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        when(userMapper.toResponseDto(any(User.class))).thenReturn(responseDto);

        UserResponseDto actual = userService.updateUser(request);
        assertEquals(actual,responseDto);
    }
}
