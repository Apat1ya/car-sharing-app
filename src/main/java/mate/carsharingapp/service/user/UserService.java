package mate.carsharingapp.service.user;

import mate.carsharingapp.dto.user.UserRegistrationRequestDto;
import mate.carsharingapp.dto.user.UserResponseDto;
import mate.carsharingapp.dto.user.UserUpdateProfileDto;
import mate.carsharingapp.exception.RegistrationException;

public interface UserService {
    UserResponseDto register(UserRegistrationRequestDto requestDto) throws RegistrationException;

    UserResponseDto getCurrentUser();

    UserResponseDto updateRoleByUserId(Long id, String role);

    UserResponseDto updateUser(UserUpdateProfileDto requestDto);
}
