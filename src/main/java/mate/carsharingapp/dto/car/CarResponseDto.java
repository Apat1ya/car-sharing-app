package mate.carsharingapp.dto.car;

import java.math.BigDecimal;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import mate.carsharingapp.model.car.Type;

@Getter
@Setter
@EqualsAndHashCode
public class CarResponseDto {
    private Long id;
    private String model;
    private String brand;
    private Type type;
    private BigDecimal dailyFee;
}
