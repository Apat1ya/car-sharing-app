package mate.carsharingapp.repository.payment;

import java.util.List;
import java.util.Optional;
import mate.carsharingapp.model.payment.Payment;
import mate.carsharingapp.model.payment.Status;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByRentalIdAndStatus(Long rentalId, Status status);

    List<Payment> findAllByRentalUserId(Long userId);

    Payment findBySessionId(String sessionId);
}
