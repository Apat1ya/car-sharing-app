package mate.carsharingapp.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;
import mate.carsharingapp.dto.rental.RentalRequestDto;
import mate.carsharingapp.dto.rental.RentalResponseDto;
import mate.carsharingapp.exception.EntityNotFoundException;
import mate.carsharingapp.mapper.RentalMapper;
import mate.carsharingapp.model.car.Car;
import mate.carsharingapp.model.rental.Rental;
import mate.carsharingapp.model.user.User;
import mate.carsharingapp.repository.car.CarRepository;
import mate.carsharingapp.repository.rental.RentalRepository;
import mate.carsharingapp.repository.user.UserRepository;
import mate.carsharingapp.service.rental.RentalServiceImpl;
import mate.carsharingapp.service.telegram.notification.TelegramNotificationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

@ExtendWith(MockitoExtension.class)
public class RentalServiceTest {
    @Mock
    private RentalRepository rentalRepository;
    @Mock
    private CarRepository carRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private RentalMapper rentalMapper;
    @Mock
    private TelegramNotificationService notificationService;
    @Mock
    private Authentication authentication;
    @Mock
    private SecurityContext securityContext;

    @InjectMocks
    private RentalServiceImpl rentalService;

    @Test
    @DisplayName("create rental and send notification")
    void createRental_WithValidRequest_Ok() {
        String email = "user@test.com";

        User user = new User();
        user.setEmail(email);

        Car car = new Car();
        car.setInventory(2);
        car.setModel("test model");

        Rental rental = new Rental();
        Long carId = 1L;

        when(carRepository.findById(carId)).thenReturn(Optional.of(car));
        when(authentication.getName()).thenReturn(email);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(rentalRepository.save(any(Rental.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        RentalResponseDto responseDto = new RentalResponseDto();
        when(rentalMapper.toResponseDto(any(Rental.class))).thenReturn(responseDto);

        RentalRequestDto requestDto = new RentalRequestDto(LocalDate.now(), carId);
        RentalResponseDto actual = rentalService.create(requestDto);
        assertEquals(actual,responseDto);
        verify(rentalRepository).save(any(Rental.class));
        verify(rentalMapper).toResponseDto(any(Rental.class));
        verify(notificationService).sentNotificationCreateRental(
                any(Rental.class), eq(user), eq(car.getModel())
        );
    }

    @Test
    @DisplayName("Find rental by valid id")
    void findRental_WithValidId_OK() {
        Long rentalId = 1L;
        Rental rental = new Rental();
        rental.setId(rentalId);

        RentalResponseDto responseDto = new RentalResponseDto();
        responseDto.setId(1L);

        when(rentalRepository.findById(rentalId)).thenReturn(Optional.of(rental));
        when(rentalMapper.toResponseDto(rental)).thenReturn(responseDto);

        RentalResponseDto actual = rentalService.findById(rentalId);

        verify(rentalRepository).findById(rentalId);
        verify(rentalMapper).toResponseDto(rental);
    }

    @Test
    @DisplayName("return rental by valid id")
    void returnRental_WithValidId_Ok() {
        String email = "user@test.com";

        Car car = new Car();
        car.setModel("test car");

        User user = new User();
        user.setId(1L);
        user.setEmail(email);

        Long rentalId = 1L;
        Rental rental = new Rental();
        rental.setId(rentalId);
        rental.setUser(user);
        rental.setActive(true);

        when(authentication.getName()).thenReturn(email);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(rentalRepository.findById(rentalId)).thenReturn(Optional.of(rental));
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(rentalRepository.save(rental)).thenReturn(rental);
        RentalResponseDto responseDto = new RentalResponseDto();
        when(rentalMapper.toResponseDto(rental)).thenReturn(responseDto);

        rentalService.returnRental(rentalId);

        verify(rentalRepository).findById(rentalId);
        verify(userRepository).findByEmail(email);
        verify(rentalRepository).save(rental);
        verify(rentalMapper).toResponseDto(rental);
        verify(notificationService).sentNotificationReturnedRental(rental, user);
    }

    @Test
    @DisplayName("return of rental that has already been returned "
            + " -> throws IllegalStateException")
    void returnRental_AlreadyReturned_NotOk() {
        String email = "user@test.com";
        User user = new User();
        user.setId(1L);
        user.setEmail(email);

        Rental rental = new Rental();
        rental.setId(1L);
        rental.setActive(false);
        rental.setUser(user);

        when(authentication.getName()).thenReturn(email);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(rentalRepository.findById(1L)).thenReturn(Optional.of(rental));
        assertThrows(IllegalStateException.class,
                () -> rentalService.returnRental(1L));
        verify(userRepository).findByEmail(email);
        verify(rentalRepository).findById(1L);
    }

    @Test
    @DisplayName("find rental by invalid id -> throws EntityNotFoundException")
    void findRental_WithInvalidId_NotOk() {
        Long invalidId = 999L;
        when(rentalRepository.findById(invalidId)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class,
                () -> rentalService.findById(invalidId));
        verify(rentalRepository).findById(invalidId);
    }
}
