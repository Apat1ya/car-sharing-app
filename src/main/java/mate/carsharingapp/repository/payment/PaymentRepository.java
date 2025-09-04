package mate.carsharingapp.repository.payment;

import java.util.List;
import java.util.Optional;
import mate.carsharingapp.model.payment.Payment;
import mate.carsharingapp.model.payment.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByRentalIdAndStatus(Long rentalId, Status status);

    @Query("""
            SELECT p FROM Payment p
            JOIN FETCH p.rental r
            WHERE r.user.id = :userId
            """)
    List<Payment> findAllPaymentsByUserId(Long userId);

    Payment findBySessionId(String sessionId);
}
