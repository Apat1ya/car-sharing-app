package mate.carsharingapp.service.payment;

import java.util.List;
import mate.carsharingapp.dto.payment.PaymentDto;
import mate.carsharingapp.dto.payment.PaymentRequestDto;
import mate.carsharingapp.dto.payment.PaymentResponseDto;
import org.springframework.web.util.UriComponentsBuilder;

public interface PaymentService {
    PaymentResponseDto createPaymentSession(PaymentRequestDto requestDto,
                                            UriComponentsBuilder builder);

    List<PaymentDto> getPaymentsByUserId(Long userId);

    void paymentSuccess(String sessionId);

    void paymentCancel(String sessionId);
}
