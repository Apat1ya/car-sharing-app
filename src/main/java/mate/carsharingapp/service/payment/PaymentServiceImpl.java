package mate.carsharingapp.service.payment;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import mate.carsharingapp.dto.payment.PaymentDto;
import mate.carsharingapp.dto.payment.PaymentRequestDto;
import mate.carsharingapp.dto.payment.PaymentResponseDto;
import mate.carsharingapp.exception.EntityNotFoundException;
import mate.carsharingapp.mapper.PaymentMapper;
import mate.carsharingapp.model.car.Car;
import mate.carsharingapp.model.payment.Payment;
import mate.carsharingapp.model.payment.Status;
import mate.carsharingapp.model.rental.Rental;
import mate.carsharingapp.model.user.User;
import mate.carsharingapp.repository.payment.PaymentRepository;
import mate.carsharingapp.repository.rental.RentalRepository;
import mate.carsharingapp.repository.user.UserRepository;
import mate.carsharingapp.service.telegram.notification.TelegramNotificationService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentServiceImpl implements PaymentService {
    private final StripeService stripeService;
    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;
    private final TelegramNotificationService notificationService;
    private final RentalRepository rentalRepository;
    private final UserRepository userRepository;

    @Override
    public PaymentResponseDto createPaymentSession(PaymentRequestDto requestDto,
                                                   UriComponentsBuilder builder) {
        Optional<Payment> existingPayment = paymentRepository.findByRentalIdAndStatus(
                requestDto.getRentalId(),
                Status.PAID);
        if (existingPayment.isPresent()) {
            throw new IllegalStateException("Payment already been completed");
        }

        Rental rental = rentalRepository.findById(requestDto.getRentalId())
                .orElseThrow(() -> new EntityNotFoundException("Rental not found with id: "
                        + requestDto.getRentalId()));
        BigDecimal amountToPay = calculateRentalAmount(rental);
        PaymentResponseDto session = stripeService.createSession(requestDto,builder,amountToPay);
        Payment payment = new Payment();
        payment.setStatus(Status.PENDING);
        payment.setTypePayment(requestDto.getTypePayment());
        payment.setRental(rental);
        payment.setSessionUrl(session.sessionUrl());
        payment.setSessionId(session.sessionId());
        payment.setAmountToPay(amountToPay);

        paymentRepository.save(payment);
        return paymentMapper.toResponseDto(payment);
    }

    @Override
    public List<PaymentDto> getPaymentsByUserId(Long userId) {
        return paymentRepository.findAllPaymentsByUserId(userId)
                .stream()
                .map(paymentMapper::toDto)
                .toList();
    }

    @Override
    public void paymentSuccess(String sessionId) {
        Payment payment = paymentRepository.findBySessionId(sessionId);
        payment.setStatus(Status.PAID);
        paymentRepository.save(payment);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User Not found with email: "
                        + email));
        notificationService.sendNotificationSuccessPayment(payment,user);
    }

    @Override
    public void paymentCancel(String sessionId) {
        Payment payment = paymentRepository.findBySessionId(sessionId);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User Not found with email: "
                        + email));
        notificationService.sendNotificationCancelPayment(payment,user);
    }

    private BigDecimal calculateRentalAmount(Rental rental) {
        Car car = rental.getCar();
        BigDecimal dailyFee = car.getDailyFee();
        long days = ChronoUnit.DAYS.between(rental.getRentalDate(), rental.getReturnDate());
        if (days == 0) {
            days = 1;
        }
        return dailyFee.multiply(BigDecimal.valueOf(days));
    }
}
