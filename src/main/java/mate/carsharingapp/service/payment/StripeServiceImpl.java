package mate.carsharingapp.service.payment;

import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import mate.carsharingapp.dto.payment.PaymentRequestDto;
import mate.carsharingapp.dto.payment.PaymentResponseDto;
import mate.carsharingapp.repository.rental.RentalRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@RequiredArgsConstructor
public class StripeServiceImpl implements StripeService {
    @Value("${stripe.currency:usd}")
    private String currency;
    private final RentalRepository rentalRepository;

    @Override
    public PaymentResponseDto createSession(PaymentRequestDto requestDto,
                                            UriComponentsBuilder uriComponentsBuilder,
                                            BigDecimal amountToPay) {
        try {
            SessionCreateParams params = SessionCreateParams.builder()
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setSuccessUrl(uriComponentsBuilder
                                    .path("/payment/success")
                                    .queryParam("session_id", "{CHECKOUT_SESSION_ID}")
                    .build().toString())
                    .setCancelUrl(uriComponentsBuilder
                            .path("/payments/cancel")
                            .build().toString())
                    .addLineItem(SessionCreateParams.LineItem.builder()
                            .setQuantity(1L)
                            .setPriceData(SessionCreateParams.LineItem.PriceData.builder()
                                    .setCurrency(currency)
                                    .setUnitAmount(amountToPay
                                            .multiply(BigDecimal.valueOf(100))
                                            .longValue())
                                    .setProductData(SessionCreateParams.LineItem.PriceData
                                            .ProductData.builder()
                                            .setName("Car rental: " + requestDto.getType().name())
                                            .build())
                                    .build())
                            .build())
                    .build();

            Session session = Session.create(params);
            return new PaymentResponseDto(session.getUrl(),session.getId());
        } catch (StripeException e) {
            throw new RuntimeException("Failed to create Stripe session", e);
        }
    }

    @Override
    public Session retrieveSession(String sessionId) {
        try {
            return Session.retrieve(sessionId);
        } catch (StripeException e) {
            throw new RuntimeException("Failed to retrieve Stripe session",e);
        }
    }
}
