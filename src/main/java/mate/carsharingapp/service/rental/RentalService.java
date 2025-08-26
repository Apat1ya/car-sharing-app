package mate.carsharingapp.service.rental;

import java.util.List;
import mate.carsharingapp.dto.rental.RentalRequestDto;
import mate.carsharingapp.dto.rental.RentalResponseDto;

public interface RentalService {
    RentalResponseDto create(RentalRequestDto requestDto);

    RentalResponseDto findById(Long id);

    RentalResponseDto returnRental(Long id);

    List<RentalResponseDto> findAllByUserIdAndIsActive(Long userId, boolean isActive);

}
