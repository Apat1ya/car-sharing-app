package mate.carsharingapp.dto.rental;

import java.time.LocalDate;

public class RentalResponseDto {
    private Long id;
    private LocalDate rentalDate;
    private LocalDate returnDate;
    private Long userId;
    private Long carId;
}
