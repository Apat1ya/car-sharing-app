package mate.carsharingapp.service.user;

import java.util.Set;
import lombok.RequiredArgsConstructor;
import mate.carsharingapp.dto.user.UserRegistrationRequestDto;
import mate.carsharingapp.dto.user.UserResponseDto;
import mate.carsharingapp.exception.EntityNotFoundException;
import mate.carsharingapp.exception.RegistrationException;
import mate.carsharingapp.mapper.UserMapper;
import mate.carsharingapp.model.user.Role;
import mate.carsharingapp.model.user.RoleName;
import mate.carsharingapp.model.user.User;
import mate.carsharingapp.repository.RoleRepository;
import mate.carsharingapp.repository.UserRepository;
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
}
