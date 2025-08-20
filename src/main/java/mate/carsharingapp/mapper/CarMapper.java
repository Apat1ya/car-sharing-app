package mate.carsharingapp.mapper;

import mate.carsharingapp.config.MapperConfig;
import mate.carsharingapp.dto.car.CarRequestDto;
import mate.carsharingapp.dto.car.CarResponseDto;
import mate.carsharingapp.model.car.Car;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface CarMapper {
    CarResponseDto toResponseDto(Car car);

    Car toModel(CarRequestDto requestDto);

    void updateCarFromDto(CarRequestDto requestDto, @MappingTarget Car car);
}
