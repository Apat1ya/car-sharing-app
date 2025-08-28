package mate.carsharingapp.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.carsharingapp.dto.rental.RentalRequestDto;
import mate.carsharingapp.dto.rental.RentalResponseDto;
import mate.carsharingapp.service.rental.RentalService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    public RentalResponseDto createRental(@RequestBody RentalRequestDto requestDto) {
        return rentalService.create(requestDto);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('MANAGER','CUSTOMER')")
    public List<RentalResponseDto> findAllByUserIdAndIsActive(
            @RequestParam("user_id") Long userId,
            @RequestParam("is_active") boolean isActive) {
        return rentalService.findAllByUserIdAndIsActive(userId, isActive);
    }

    @PostMapping("/{id}/return")
    @PreAuthorize("hasAnyRole('MANAGER','CUSTOMER')")
    public RentalResponseDto returnRental(@PathVariable Long id) {
        return rentalService.returnRental(id);
    }

}
