package mate.carsharingapp.dto.car;

import java.math.BigDecimal;
import mate.carsharingapp.model.car.Type;

public class CarResponseDto {
    private Long id;
    private String model;
    private String brand;
    private Type type;
    private BigDecimal dailyFee;
}
