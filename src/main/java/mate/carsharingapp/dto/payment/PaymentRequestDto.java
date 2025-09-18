package mate.carsharingapp.dto.payment;

import lombok.Getter;
import lombok.Setter;
import mate.carsharingapp.model.payment.TypePayment;

@Getter
@Setter
public class PaymentRequestDto {
    private Long rentalId;
    private TypePayment typePayment;
}
