package mate.carsharingapp.service.payment;

import com.stripe.model.checkout.Session;
import java.math.BigDecimal;
import mate.carsharingapp.dto.payment.PaymentRequestDto;
import mate.carsharingapp.dto.payment.PaymentResponseDto;
import org.springframework.web.util.UriComponentsBuilder;

public interface StripeService {
    PaymentResponseDto createSession(PaymentRequestDto requestDto,
                                     UriComponentsBuilder uriComponentsBuilder,
                                     BigDecimal amountToPay);

    Session retrieveSession(String sessionId);
}
