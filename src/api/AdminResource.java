package api;

import model.Customer;
import model.IRoom;

import java.util.Collection;
import java.util.List;

public class AdminResource extends HotelResource {

    @Override
    public Customer getCustomer(String email) {
        return customerService.getCustomer(email);
    }

    public static void addRoom(IRoom room) {
        reservationService.addRoom(room);
    }

    public static Collection<IRoom> getAllRooms() {
        return reservationService.getAllRooms();
    }

    public static Collection<Customer> getAllCustomers() {
        return customerService.getAllCustomers();
    }


    public static void displayAllReservations() {
        reservationService.printAllReservations();
    }
}
