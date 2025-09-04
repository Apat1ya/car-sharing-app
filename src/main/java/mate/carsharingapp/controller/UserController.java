package mate.carsharingapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mate.carsharingapp.dto.user.UserResponseDto;
import mate.carsharingapp.dto.user.UserUpdateProfileDto;
import mate.carsharingapp.service.user.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @Operation(
            summary = "Get user profile",
            description = "Returns information about the current user "
                    + "who is authorized in the system."
    )
    @GetMapping("/me")
    @PreAuthorize("hasAnyRole('MANAGER','CUSTOMER')")
    public UserResponseDto getUserProfile() {
        return userService.getCurrentUser();
    }

    @Operation(
            summary = "Update user role",
            description = "Updates the user's role based on their ID"
    )
    @PreAuthorize("hasAnyRole('MANAGER')")
    @PutMapping("/{id}/role")
    public UserResponseDto updateUserRole(@PathVariable Long id, String role) {
        return userService.updateRoleByUserId(id,role);
    }

    @Operation(
            summary = "Update user profile",
            description = " Updates the current user's profile data"
                    + "(name, email, password, etc.). "
    )
    @PatchMapping("/me")
    @PreAuthorize("hasAnyRole('MANAGER','CUSTOMER')")
    public UserResponseDto updateProfile(@RequestBody @Valid UserUpdateProfileDto requestDto) {
        return userService.updateUser(requestDto);
    }
}
