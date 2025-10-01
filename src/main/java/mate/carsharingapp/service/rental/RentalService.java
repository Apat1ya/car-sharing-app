package mate.carsharingapp.service.rental;

import mate.carsharingapp.dto.rental.RentalRequestDto;
import mate.carsharingapp.dto.rental.RentalResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RentalService {
    RentalResponseDto create(RentalRequestDto requestDto);

    RentalResponseDto findById(Long id);

    RentalResponseDto returnRental(Long id);

    Page<RentalResponseDto> findAllByUserIdAndIsActive(Long userId,
                                                       boolean isActive,
                                                       Pageable pageable);

}
