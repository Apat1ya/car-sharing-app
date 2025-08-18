package mate.carsharingapp.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Objects;
import org.springframework.beans.BeanWrapperImpl;

public class FieldMatchValidator implements
        ConstraintValidator<FieldMatch,Object> {
    private String firstField;
    private String secondField;

    @Override
    public void initialize(FieldMatch constraintAnnotation) {
        this.firstField = constraintAnnotation.firstField();
        this.secondField = constraintAnnotation.secondField();
    }

    @Override
    public boolean isValid(Object object, ConstraintValidatorContext constraintValidatorContext) {
        Object first = new BeanWrapperImpl(object).getPropertyValue(firstField);
        Object second = new BeanWrapperImpl(object).getPropertyValue(secondField);
        return Objects.equals(first,second);
    }
}
