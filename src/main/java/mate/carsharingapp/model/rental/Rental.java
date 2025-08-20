package mate.carsharingapp.model.rental;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;
import mate.carsharingapp.model.car.Car;
import mate.carsharingapp.model.user.User;

@Getter
@Setter
@Table(name = "rentals")
public class Rental {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private LocalDate rentalDate;
    @Column(nullable = false)
    private LocalDate returnDate;
    @Column(nullable = false)
    private LocalDate actualReturnDate;
    @ManyToOne
    @Column(name = "car_id",nullable = false)
    private Car car;
    @OneToOne
    @Column(name = "user_id",nullable = false)
    private User user;
    @Column(nullable = false)
    private boolean isDeleted = false;
}
