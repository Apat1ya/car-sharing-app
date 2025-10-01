package mate.carsharingapp.dto.car;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;
import mate.carsharingapp.model.car.Type;

@Getter
@Setter
public class CarRequestDto {
    @NotBlank
    private String model;
    @NotBlank
    private String brand;
    @NotNull
    private Type type;
    @NotNull
    private int inventory;
    @NotNull
    private BigDecimal dailyFee;
}
