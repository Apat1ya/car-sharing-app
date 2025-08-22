package mate.carsharingapp.service.user;

import java.util.Set;
import lombok.RequiredArgsConstructor;
import mate.carsharingapp.dto.user.UserRegistrationRequestDto;
import mate.carsharingapp.dto.user.UserResponseDto;
import mate.carsharingapp.dto.user.UserUpdateProfileDto;
import mate.carsharingapp.exception.EntityNotFoundException;
import mate.carsharingapp.exception.RegistrationException;
import mate.carsharingapp.mapper.UserMapper;
import mate.carsharingapp.model.user.Role;
import mate.carsharingapp.model.user.RoleName;
import mate.carsharingapp.model.user.User;
import mate.carsharingapp.repository.role.RoleRepository;
import mate.carsharingapp.repository.user.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto requestDto)
            throws RegistrationException {
        if (userRepository.existsByEmail(requestDto.getEmail())) {
            throw new RegistrationException("User with email"
            + requestDto.getEmail() + "already exist");
        }
        User savedUser = userMapper.toModel(requestDto);
        savedUser.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        Role userRole = roleRepository.findByRole(RoleName.ROLE_CUSTOMER)
                .orElseThrow(() -> new EntityNotFoundException(RoleName.ROLE_CUSTOMER
                        + "not found"));
        savedUser.setRoles(Set.of(userRole));
        userRepository.save(savedUser);
        return userMapper.toResponseDto(savedUser);
    }

    @Override
    public UserResponseDto getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new EntityNotFoundException("User Not found with email: "
                            + email));
        return userMapper.toResponseDto(user);
    }

    @Override
    public UserResponseDto updateRoleByUserId(Long id, String role) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found by id: " + id));
        Role roleName = roleRepository.findByRole(RoleName.valueOf(role))
                .orElseThrow(() -> new EntityNotFoundException("Role not found "));
        user.setRoles(Set.of(roleName));
        userRepository.save(user);
        return userMapper.toResponseDto(user);
    }

    @Override
    public UserResponseDto updateUser(UserUpdateProfileDto requestDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User Not found with email: "
                        + email));
        userMapper.updateUserFromDto(requestDto, user);
        userRepository.save(user);
        return userMapper.toResponseDto(user);

    }
}
