package mate.carsharingapp.service.security;

import lombok.RequiredArgsConstructor;
import mate.carsharingapp.dto.user.UserLoginRequestDto;
import mate.carsharingapp.dto.user.UserLoginResponseDto;
import mate.carsharingapp.security.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public UserLoginResponseDto authenticate(UserLoginRequestDto request) {
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        String token = jwtUtil.generateToke(authentication.getName());
        return new UserLoginResponseDto(token);
    }
}
