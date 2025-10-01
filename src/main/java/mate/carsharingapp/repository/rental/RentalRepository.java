package mate.carsharingapp.repository.rental;

import java.time.LocalDate;
import java.util.List;
import mate.carsharingapp.model.rental.Rental;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RentalRepository extends JpaRepository<Rental, Long> {
    List<Rental> findAllByIsActiveTrue();

    Page<Rental> findAllByUserIdAndIsActive(Long userId, boolean isActive, Pageable pageable);

    Page<Rental> findAllByUserId(Long id, Pageable pageable);

    List<Rental> findAllByReturnDateLessThan(LocalDate date);
}
