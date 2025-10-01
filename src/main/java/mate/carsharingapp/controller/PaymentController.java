package mate.carsharingapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.carsharingapp.dto.payment.PaymentDto;
import mate.carsharingapp.dto.payment.PaymentRequestDto;
import mate.carsharingapp.dto.payment.PaymentResponseDto;
import mate.carsharingapp.service.payment.PaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequiredArgsConstructor
@RequestMapping("/payments")
public class PaymentController {
    private final PaymentService paymentService;

    @GetMapping("/success")
    @PreAuthorize("hasAnyRole('MANAGER','CUSTOMER')")
    @Operation(
            summary = "Handle successful Stripe payment",
            description = "Handles redirect from Stripe when payment succeeds"
    )
    public String successPayment(@RequestParam("session_id") String sessionId) {
        paymentService.paymentSuccess(sessionId);
        return "payment successful!";
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('MANAGER','CUSTOMER')")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Create payment session",
            description = "Creates a payment session for the given rental and returns session info"
    )
    public PaymentResponseDto createPayment(@RequestBody @Valid PaymentRequestDto requestDto,
                                            UriComponentsBuilder builder) {
        return paymentService.createPaymentSession(requestDto,builder);
    }

    @GetMapping("/cancel")
    @PreAuthorize("hasAnyRole('MANAGER','CUSTOMER')")
    @Operation(
            summary = "Handle canceled Stripe payment",
            description = "Informs the user that the payment was canceled"
    )
    public String cancelPayment(@RequestParam("session_id") String sessionId) {
        paymentService.paymentCancel(sessionId);
        return "payment cancel!";
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('MANAGER','CUSTOMER')")
    public List<PaymentDto> findAllByUserId(@RequestParam("user_id") Long id) {
        return paymentService.getPaymentsByUserId(id);
    }
}
