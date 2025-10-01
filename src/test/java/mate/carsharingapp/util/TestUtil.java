package mate.carsharingapp.util;

import java.math.BigDecimal;
import mate.carsharingapp.dto.car.CarRequestDto;
import mate.carsharingapp.dto.car.CarResponseDto;
import mate.carsharingapp.dto.payment.PaymentDto;
import mate.carsharingapp.dto.user.UserRegistrationRequestDto;
import mate.carsharingapp.dto.user.UserResponseDto;
import mate.carsharingapp.model.car.Car;
import mate.carsharingapp.model.car.Type;
import mate.carsharingapp.model.payment.Payment;
import mate.carsharingapp.model.payment.Status;
import mate.carsharingapp.model.payment.TypePayment;
import mate.carsharingapp.model.user.User;

public class TestUtil {
    public static CarResponseDto createFirstCarDto() {
        CarResponseDto firstCar = new CarResponseDto();
        firstCar.setId(1L);
        firstCar.setModel("model 1");
        firstCar.setBrand("brand 1");
        firstCar.setType(Type.valueOf("SEDAN"));
        firstCar.setDailyFee(BigDecimal.valueOf(10.99));
        return firstCar;
    }

    public static CarResponseDto createSecondCarDto() {
        CarResponseDto secondCar = new CarResponseDto();
        secondCar.setId(2L);
        secondCar.setModel("model 2");
        secondCar.setBrand("brand 2");
        secondCar.setType(Type.valueOf("SUV"));
        secondCar.setDailyFee(BigDecimal.valueOf(9.99));
        return secondCar;
    }

    public static CarResponseDto createThirdCarDto() {
        CarResponseDto thirdCar = new CarResponseDto();
        thirdCar.setId(3L);
        thirdCar.setModel("model 3");
        thirdCar.setBrand("brand 3");
        thirdCar.setType(Type.valueOf("SEDAN"));
        thirdCar.setDailyFee(BigDecimal.valueOf(11.99));
        return thirdCar;
    }

    public static Car createFirstCar() {
        Car firstCar = new Car();
        firstCar.setId(1L);
        firstCar.setModel("model 1");
        firstCar.setBrand("brand 1");
        firstCar.setType(Type.valueOf("SEDAN"));
        firstCar.setDailyFee(BigDecimal.valueOf(10.99));
        return firstCar;
    }

    public static Car createSecondCar() {
        Car secondCar = new Car();
        secondCar.setId(2L);
        secondCar.setModel("model 2");
        secondCar.setBrand("brand 2");
        secondCar.setType(Type.valueOf("SUV"));
        secondCar.setDailyFee(BigDecimal.valueOf(9.99));
        return secondCar;
    }

    public static Car createThirdCar() {
        Car thirdCar = new Car();
        thirdCar.setId(3L);
        thirdCar.setModel("model 3");
        thirdCar.setBrand("brand 3");
        thirdCar.setType(Type.valueOf("SEDAN"));
        thirdCar.setDailyFee(BigDecimal.valueOf(11.99));
        return thirdCar;
    }

    public static PaymentDto createFirstPaymentDto() {
        PaymentDto firstDto = new PaymentDto();
        firstDto.setId(1L);
        firstDto.setStatus(Status.PAID);
        firstDto.setTypePayment(TypePayment.PAYMENT);
        firstDto.setRentalId(1L);
        firstDto.setAmountToPay(BigDecimal.valueOf(19.99));
        return firstDto;
    }

    public static PaymentDto createSecondPaymentDto() {
        PaymentDto secondDto = new PaymentDto();
        secondDto.setId(2L);
        secondDto.setStatus(Status.PAID);
        secondDto.setTypePayment(TypePayment.PAYMENT);
        secondDto.setRentalId(2L);
        secondDto.setAmountToPay(BigDecimal.valueOf(12.99));
        return secondDto;
    }

    public static Payment createFirstPayment() {
        Payment first = new Payment();
        first.setId(1L);
        first.setStatus(Status.PAID);
        first.setTypePayment(TypePayment.PAYMENT);
        first.setAmountToPay(BigDecimal.valueOf(19.99));
        return first;
    }

    public static Payment createSecondPayment() {
        Payment second = new Payment();
        second.setId(2L);
        second.setStatus(Status.PAID);
        second.setTypePayment(TypePayment.PAYMENT);
        second.setAmountToPay(BigDecimal.valueOf(12.99));
        return second;
    }

    public static UserRegistrationRequestDto createUserRegistrationRequestDto() {
        UserRegistrationRequestDto requestDto = new UserRegistrationRequestDto();
        requestDto.setEmail("test@email.com");
        requestDto.setFirstName("first");
        requestDto.setLastName("last");
        requestDto.setPassword("12345678");
        requestDto.setRepeatPassword("12345678");
        return requestDto;
    }

    public static UserResponseDto createResponseAfterRegistration() {
        UserResponseDto responseDto = new UserResponseDto();
        responseDto.setId(1L);
        responseDto.setEmail("test@email.com");
        responseDto.setFirstName("first");
        responseDto.setLastName("last");
        return responseDto;
    }

    public static CarRequestDto createFirstCarRequestDto() {
        CarRequestDto first = new CarRequestDto();
        first.setModel("model 1");
        first.setBrand("brand 1");
        first.setType(Type.valueOf("SEDAN"));
        first.setInventory(2);
        first.setDailyFee(BigDecimal.valueOf(10.99));
        return first;
    }

    public static UserResponseDto createUserResponseDto() {
        UserResponseDto user = new UserResponseDto();
        user.setId(1L);
        user.setEmail("customer@example.com");
        user.setFirstName("Ivan");
        user.setLastName("Petrov");
        return user;
    }

    public static User createUserForTests() {
        User user = new User();
        user.setId(5L);
        user.setFirstName("test");
        user.setLastName("test");
        user.setEmail("Admin@gmail.com");
        user.setPassword("password");
        user.setTelegramChatId(204343344L);
        return user;
    }

    public static UserResponseDto createUserDtoForTest() {
        UserResponseDto responseDto = new UserResponseDto();
        responseDto.setEmail("Admin@gmail.com");
        responseDto.setFirstName("test");
        responseDto.setLastName("test");
        return responseDto;
    }
}
