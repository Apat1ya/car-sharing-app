package mate.carsharingapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import mate.carsharingapp.dto.rental.RentalRequestDto;
import mate.carsharingapp.dto.rental.RentalResponseDto;
import mate.carsharingapp.service.rental.RentalService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/rentals")
public class RentalController {
    private final RentalService rentalService;

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('MANAGER','CUSTOMER')")
    public RentalResponseDto findRentalById(@PathVariable Long id) {
        return rentalService.findById(id);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('MANAGER','CUSTOMER')")
    @Operation(
            summary = "Create new rental",
            description = "Creates a rental and decreases car inventory by 1"
    )
    @ResponseStatus(HttpStatus.CREATED)
    public RentalResponseDto createRental(@RequestBody RentalRequestDto requestDto) {
        return rentalService.create(requestDto);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('MANAGER','CUSTOMER')")
    @Operation(
            summary = "Find all rentals by user id",
            description = "Creates a rental and decreases car inventory by 1"
    )
    public Page<RentalResponseDto> findAllByUserIdAndIsActive(
            @RequestParam("user_id") Long userId,
            @RequestParam("is_active") boolean isActive,
            Pageable pageable) {
        return rentalService.findAllByUserIdAndIsActive(userId, isActive, pageable);
    }

    @PostMapping("/{id}/return")
    @PreAuthorize("hasAnyRole('MANAGER','CUSTOMER')")
    @Operation(
            summary = "Return a rented car",
            description = "Sets actual return date and increases car inventory by 1"
    )
    public RentalResponseDto returnRental(@PathVariable Long id) {
        return rentalService.returnRental(id);
    }

}
