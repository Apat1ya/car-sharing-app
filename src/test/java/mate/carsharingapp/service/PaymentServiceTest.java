package mate.carsharingapp.service;

import static mate.carsharingapp.util.TestUtil.createFirstPayment;
import static mate.carsharingapp.util.TestUtil.createFirstPaymentDto;
import static mate.carsharingapp.util.TestUtil.createSecondPayment;
import static mate.carsharingapp.util.TestUtil.createSecondPaymentDto;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import mate.carsharingapp.dto.payment.PaymentDto;
import mate.carsharingapp.dto.payment.PaymentRequestDto;
import mate.carsharingapp.dto.payment.PaymentResponseDto;
import mate.carsharingapp.mapper.PaymentMapper;
import mate.carsharingapp.model.car.Car;
import mate.carsharingapp.model.payment.Payment;
import mate.carsharingapp.model.payment.Status;
import mate.carsharingapp.model.payment.TypePayment;
import mate.carsharingapp.model.rental.Rental;
import mate.carsharingapp.model.user.User;
import mate.carsharingapp.repository.payment.PaymentRepository;
import mate.carsharingapp.repository.rental.RentalRepository;
import mate.carsharingapp.repository.user.UserRepository;
import mate.carsharingapp.service.payment.PaymentServiceImpl;
import mate.carsharingapp.service.payment.StripeServiceImpl;
import mate.carsharingapp.service.telegram.notification.TelegramNotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.util.UriComponentsBuilder;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceTest {
    @Mock
    private PaymentRepository paymentRepository;
    @Mock
    private PaymentMapper paymentMapper;
    @Mock
    private StripeServiceImpl stripeService;
    @Mock
    private TelegramNotificationService notificationService;
    @Mock
    private RentalRepository rentalRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private Authentication authentication;
    @Mock
    private SecurityContext securityContext;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    private UriComponentsBuilder componentsBuilder;

    @BeforeEach
    void setUp() {
        UriComponentsBuilder componentsBuilder = UriComponentsBuilder
                .fromUriString("http://localhost");
    }

    @Test
    @DisplayName("Should create new payment session")
    void createPaymentSession_OK() {
        PaymentRequestDto requestDto = new PaymentRequestDto();
        requestDto.setRentalId(1L);
        requestDto.setTypePayment(TypePayment.PAYMENT);
        Car car = new Car();
        car.setDailyFee(BigDecimal.valueOf(10.99));

        Rental rental = new Rental();
        rental.setId(1L);
        rental.setCar(car);
        rental.setRentalDate(LocalDate.now());
        rental.setReturnDate(LocalDate.now().plusDays(2));

        Payment savedPayment = new Payment();
        savedPayment.setId(1L);

        PaymentResponseDto stripeSession = new PaymentResponseDto("http://stripe.com","session");

        when(paymentRepository.findByRentalIdAndStatus(requestDto.getRentalId(), Status.PAID))
                .thenReturn(Optional.empty());
        when(rentalRepository.findById(requestDto.getRentalId())).thenReturn(Optional.of(rental));
        when(stripeService
                .createSession(eq(requestDto),eq(componentsBuilder), any(BigDecimal.class)))
                .thenReturn(stripeSession);
        when(paymentRepository.save(any(Payment.class))).thenReturn(savedPayment);
        when(paymentMapper.toResponseDto(any(Payment.class))).thenReturn(stripeSession);

        PaymentResponseDto actual = paymentService
                .createPaymentSession(requestDto, componentsBuilder);

        assertEquals(actual,stripeSession);
    }

    @Test
    @DisplayName("get payments by valid user id")
    void getPaymentsByUserId_WithValidId_OK() {
        List<PaymentDto> paymentDtos = List.of(createFirstPaymentDto(),
                createSecondPaymentDto());
        Long userId = 1L;
        List<Payment> payments = List.of(
                createFirstPayment(),
                createSecondPayment());
        when(paymentRepository.findAllPaymentsByUserId(userId)).thenReturn(payments);
        when(paymentMapper.toDto(payments.get(0))).thenReturn(paymentDtos.get(0));
        when(paymentMapper.toDto(payments.get(1))).thenReturn(paymentDtos.get(1));

        List<PaymentDto> actual = paymentService.getPaymentsByUserId(userId);

        assertEquals(actual, paymentDtos);
        verify(paymentRepository).findAllPaymentsByUserId(userId);
        verify(paymentMapper).toDto(payments.get(0));
        verify(paymentMapper).toDto(payments.get(1));
    }

    @Test
    @DisplayName("update status payment and send notification")
    void marPaymentSuccess_Ok() {
        String sessionId = "sessionId";
        String email = "user@test.com";

        Payment payment = new Payment();
        payment.setStatus(Status.PENDING);
        payment.setSessionId(sessionId);
        User user = new User();
        user.setEmail(email);

        when(authentication.getName()).thenReturn(email);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(paymentRepository.findBySessionId(sessionId)).thenReturn(payment);
        when(paymentRepository.save(payment)).thenReturn(payment);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        paymentService.paymentSuccess(sessionId);

        assertEquals(Status.PAID, payment.getStatus());
        verify(paymentRepository).save(payment);
        verify(notificationService).sendNotificationSuccessPayment(payment,user);
    }

    @Test
    @DisplayName("should send cancellation notification to user")
    void marPaymentCancel_Ok() {
        String sessionId = "sessionId";
        String email = "user@test.com";

        Payment payment = new Payment();
        payment.setSessionId(sessionId);
        User user = new User();
        user.setEmail(email);

        when(authentication.getName()).thenReturn(email);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(paymentRepository.findBySessionId(sessionId)).thenReturn(payment);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        paymentService.paymentCancel(sessionId);

        verify(notificationService).sendNotificationCancelPayment(payment,user);
    }
}
