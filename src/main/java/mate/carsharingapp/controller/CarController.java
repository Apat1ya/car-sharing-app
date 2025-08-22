package mate.carsharingapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mate.carsharingapp.dto.car.CarRequestDto;
import mate.carsharingapp.dto.car.CarResponseDto;
import mate.carsharingapp.service.car.CarService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cars")
public class CarController {
    private final CarService carService;

    @Operation(
            summary = "Get all cars",
            description = "Returns a list of all cars in the app"
    )
    @GetMapping
    @PreAuthorize("hasAnyRole('MANAGER','CUSTOMER')")
    public Page<CarResponseDto> findAll(Pageable pageable) {
        return carService.findAll(pageable);
    }

    @Operation(
            summary = "Get car by ID",
            description = "Returns the car and details about it by its ID"
    )
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('MANAGER','CUSTOMER')")
    public CarResponseDto findCarById(@PathVariable Long id) {
        return carService.findById(id);
    }

    @Operation(
            summary = "Create new car",
            description = "Create and save new car"
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('MANAGER')")
    public CarResponseDto create(@Valid @RequestBody CarRequestDto requestDto) {
        return carService.create(requestDto);
    }

    @Operation(
            summary = "update car",
            description = "update car by id"
    )
    @PatchMapping("/{id}")
    @PreAuthorize("hasAnyRole('MANAGER')")
    public CarResponseDto update(@PathVariable Long id,
                                 @Valid @RequestBody CarRequestDto requestDto) {
        return carService.update(id, requestDto);
    }

    @Operation(
            summary = "delete car",
            description = "delete car by id"
    )
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyRole('MANAGER')")
    public void delete(@PathVariable Long id) {
        carService.delete(id);
    }
}
