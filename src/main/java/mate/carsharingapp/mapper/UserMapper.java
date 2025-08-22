package mate.carsharingapp.mapper;

import mate.carsharingapp.config.MapperConfig;
import mate.carsharingapp.dto.user.UserRegistrationRequestDto;
import mate.carsharingapp.dto.user.UserResponseDto;
import mate.carsharingapp.dto.user.UserUpdateProfileDto;
import mate.carsharingapp.model.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    UserResponseDto toResponseDto(User user);

    User toModel(UserRegistrationRequestDto requestDto);

    void updateUserFromDto(UserUpdateProfileDto requestDto, @MappingTarget User user);
}
