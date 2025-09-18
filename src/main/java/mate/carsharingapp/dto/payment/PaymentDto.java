package mate.carsharingapp.dto.payment;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;
import mate.carsharingapp.model.payment.Status;
import mate.carsharingapp.model.payment.TypePayment;

@Getter
@Setter
public class PaymentDto {
    private Long id;
    private Status status;
    private TypePayment typePayment;
    private Long rentalId;
    private BigDecimal amountToPay;
}
