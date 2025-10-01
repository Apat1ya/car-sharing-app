package mate.carsharingapp.service.telegram.notification;

import java.time.LocalDate;
import mate.carsharingapp.model.payment.Payment;
import mate.carsharingapp.model.rental.Rental;
import mate.carsharingapp.model.user.User;

public interface TelegramNotificationService {
    void sendNotification(User user, String message);

    void sentNotificationCreateRental(Rental rental, User user, String car);

    void sentNotificationReturnedRental(Rental rental, User user);

    void sendNotificationOverdueRental(Rental rental, User user, LocalDate date);

    void sendNotificationSuccessPayment(Payment payment, User user);

    void sendNotificationCancelPayment(Payment payment, User user);
}
