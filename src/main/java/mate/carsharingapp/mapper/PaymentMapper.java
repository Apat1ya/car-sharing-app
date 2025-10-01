package mate.carsharingapp.mapper;

import mate.carsharingapp.config.MapperConfig;
import mate.carsharingapp.dto.payment.PaymentDto;
import mate.carsharingapp.dto.payment.PaymentResponseDto;
import mate.carsharingapp.model.payment.Payment;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface PaymentMapper {

    PaymentResponseDto toResponseDto(Payment payment);

    PaymentDto toDto(Payment payment);

}
