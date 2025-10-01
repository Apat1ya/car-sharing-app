package mate.carsharingapp.dto.rental;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;

public record RentalRequestDto(
        @NotNull
        LocalDate returnDate,
        @NotNull
        @Positive
        Long carId
) {

}
