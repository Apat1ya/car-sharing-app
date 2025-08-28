package mate.carsharingapp.service.rental;

import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.carsharingapp.dto.rental.RentalRequestDto;
import mate.carsharingapp.dto.rental.RentalResponseDto;
import mate.carsharingapp.exception.EntityNotFoundException;
import mate.carsharingapp.exception.NotificationException;
import mate.carsharingapp.exception.PermissionDeniedException;
import mate.carsharingapp.exception.UnavailableCarException;
import mate.carsharingapp.mapper.RentalMapper;
import mate.carsharingapp.model.car.Car;
import mate.carsharingapp.model.rental.Rental;
import mate.carsharingapp.model.user.User;
import mate.carsharingapp.repository.car.CarRepository;
import mate.carsharingapp.repository.rental.RentalRepository;
import mate.carsharingapp.repository.user.UserRepository;
import mate.carsharingapp.service.telegram.notification.TelegramNotificationService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class RentalServiceImpl implements RentalService {
    private final RentalRepository rentalRepository;
    private final CarRepository carRepository;
    private final UserRepository userRepository;
    private final RentalMapper rentalMapper;
    private final TelegramNotificationService notificationService;

    @Override
    public RentalResponseDto create(RentalRequestDto requestDto) {
        Car car = carRepository.findById(requestDto.carId())
                .orElseThrow(() -> new EntityNotFoundException("Car not found with id: "
                + requestDto.carId()));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        if (car.getInventory() <= 0) {
            throw new UnavailableCarException("Car is not available");
        }
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User Not found with email: "
                        + email));
        car.setInventory(car.getInventory() - 1);
        Rental rental = new Rental();
        rental.setCar(car);
        rental.setUser(user);
        rental.setRentalDate(LocalDate.now());
        rental.setReturnDate(requestDto.returnDate());
        rental.setActive(true);

        rentalRepository.save(rental);
        notificationService.sentNotificationCreateRental(rental, user, car.getModel());
        return rentalMapper.toResponseDto(rental);
    }

    @Override
    public RentalResponseDto findById(Long id) {
        Rental rental = rentalRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Rental not found by id: " + id));
        return rentalMapper.toResponseDto(rental);
    }

    @Override
    public RentalResponseDto returnRental(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Rental rental = rentalRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Rental not found with id: " + id));
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        if (!rental.getUser().getId().equals(user.getId())) {
            throw new PermissionDeniedException("You don`t have permission to perform this action");
        }
        if (!rental.isActive()) {
            throw new IllegalStateException("The car has already been returned.");
        }

        rental.setActive(false);
        rental.setActualReturnDate(LocalDate.now());
        Rental savedRental = rentalRepository.save(rental);
        notificationService.sentNotificationReturnedRental(rental,user);
        return rentalMapper.toResponseDto(savedRental);
    }

    @Override
    public List<RentalResponseDto> findAllByUserIdAndIsActive(Long userId, boolean isActive) {
        List<Rental> rentals = rentalRepository.findAllByUserIdAndIsActive(userId, isActive);
        return rentals.stream()
                .map(rentalMapper::toResponseDto)
                .toList();

    }

    @Scheduled(cron = "0 0 0 * * ?")
    private void updateRentalStatus() {
        List<Rental> rentals = rentalRepository.findAllByIsActiveTrue();
        LocalDate today = LocalDate.now();
        for (Rental rental : rentals) {
            if (rental.getReturnDate().isBefore(today) || rental.getReturnDate().isEqual(today)) {
                rental.setActive(false);
            }
        }
    }

    @Scheduled(cron = "0 0 9 * * ?")
    public void notificationOfOverdueRentals() {
        List<Rental> rentals = rentalRepository.findAllByReturnDateLessThan(LocalDate.now());
        for (Rental rental : rentals) {
            try {
                notificationService.sendNotificationOverdueRental(rental,
                        rental.getUser(),
                        rental.getReturnDate());
            } catch (NotificationException e) {
                throw new NotificationException("Unable to send notification"
                        + "because user not found." + e);
            }
        }
    }

}
