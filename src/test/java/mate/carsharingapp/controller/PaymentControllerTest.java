package mate.carsharingapp.controller;

import static mate.carsharingapp.util.TestUtil.createFirstPaymentDto;
import static mate.carsharingapp.util.TestUtil.createSecondPaymentDto;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import mate.carsharingapp.dto.payment.PaymentDto;
import mate.carsharingapp.dto.payment.PaymentRequestDto;
import mate.carsharingapp.dto.payment.PaymentResponseDto;
import mate.carsharingapp.model.payment.TypePayment;
import mate.carsharingapp.service.payment.PaymentService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.util.UriComponentsBuilder;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
public class PaymentControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private PaymentService paymentService;

    @Test
    @DisplayName("create payment session, shoul return status 201 and session_id")
    @WithMockUser(username = "user", roles = "CUSTOMER")
    void createPayment_WithValidRequest_Ok() throws Exception {
        PaymentRequestDto requestDto = new PaymentRequestDto();
        requestDto.setRentalId(1L);
        requestDto.setTypePayment(TypePayment.PAYMENT);
        PaymentResponseDto responseDto = new PaymentResponseDto("testSessionUrl", "testSessionId");
        when(paymentService.createPaymentSession(any(PaymentRequestDto.class),
                any(UriComponentsBuilder.class))).thenReturn(responseDto);
        mockMvc.perform(post("/payments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.sessionId").value("testSessionId"));
    }

    @Test
    @DisplayName("should return success payment")
    @WithMockUser(username = "user", roles = "CUSTOMER")
    void successPayment_Ok() throws Exception {
        String sessionId = "testSessionId";
        doNothing().when(paymentService).paymentSuccess(sessionId);

        mockMvc.perform(get("/payments/success")
                .param("session_id",sessionId))
                .andExpect(status().isOk())
                .andExpect(content().string("payment successful!"));
        verify(paymentService).paymentSuccess(sessionId);
    }

    @Test
    @DisplayName("should return cancel payment")
    @WithMockUser(username = "user", roles = "CUSTOMER")
    void cancelPayment_Ok() throws Exception {
        String sessionId = "testSessionId";
        doNothing().when(paymentService).paymentSuccess(sessionId);

        mockMvc.perform(get("/payments/cancel")
                        .param("session_id",sessionId))
                .andExpect(status().isOk())
                .andExpect(content().string("payment cancel!"));
        verify(paymentService).paymentCancel(sessionId);
    }

    @Test
    @DisplayName("return all payments by valid user id")
    @WithMockUser(username = "user", roles = "CUSTOMER")
    void returnAllPayments_WithValidUserId_Ok() throws Exception {
        List<PaymentDto> payments = List.of(createFirstPaymentDto(),
                createSecondPaymentDto());
        Long userId = 1L;

        when(paymentService.getPaymentsByUserId(userId)).thenReturn(payments);

        mockMvc.perform(get("/payments")
                        .param("user_id", String.valueOf(userId)))
                        .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2));

    }
}
