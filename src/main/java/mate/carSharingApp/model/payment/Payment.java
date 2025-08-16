package mate.carSharingApp.model.payment;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;
import mate.carSharingApp.model.rental.Rental;

@Entity
@Getter
@Setter
@Table(name = "payments")
public class Payment {
    private Long id;
    private Status status;
    private Type type;
    private Rental rentalId;
    @Column(name = "session_url", length = 512)
    private String sessionUrl;
    @Column(name = "session_id", unique = true)
    private String sessionId;
    private BigDecimal amountToPay;
}
