package mate.carsharingapp.mapper;

import mate.carsharingapp.config.MapperConfig;
import mate.carsharingapp.dto.rental.RentalRequestDto;
import mate.carsharingapp.dto.rental.RentalResponseDto;
import mate.carsharingapp.model.rental.Rental;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface RentalMapper {
    RentalResponseDto toResponseDto(Rental rental);

    Rental toModel(RentalRequestDto requestDto);
}
