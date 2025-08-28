package mate.carsharingapp.repository.rental;

import java.time.LocalDate;
import java.util.List;
import mate.carsharingapp.model.rental.Rental;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RentalRepository extends JpaRepository<Rental, Long> {
    List<Rental> findAllByIsActiveTrue();

    List<Rental> findAllByUserIdAndIsActive(Long userId, boolean isActive);

    List<Rental> findAllByUserId(Long id);

    List<Rental> findAllByReturnDateLessThan(LocalDate date);
}
