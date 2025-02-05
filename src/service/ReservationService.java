package service;

import model.Customer;
import model.IRoom;
import model.Reservation;

import java.util.*;

public class ReservationService {
    private static ReservationService instance = null;
    public Map<String, IRoom> rooms;
    public List<Reservation> reservations;

    private ReservationService() {
        rooms = new HashMap<>();
        reservations = new ArrayList<>();
    }

    public static ReservationService ReservationService() {
        if (instance == null) {
            instance = new ReservationService();
        }
        return instance;
    }

    public void addRoom(IRoom room) {
        rooms.put(room.getRoomNumber(), room);
    }


    public Collection<IRoom> getAllRooms() {
        return rooms.values();
    }

    public IRoom getARoom(String roomId) {
        if (rooms.containsKey(roomId)) {
            return rooms.get(roomId);
        }
        return null;
    }
    public Reservation reserveARoom(Customer customer, IRoom room, Date checkInDate, Date checkOutDate) {
        Reservation reservation = new Reservation(customer, room, checkInDate, checkOutDate);
        reservations.add(reservation);
        return reservation;
    }


    public Collection<IRoom> findRooms(Date checkInDate, Date checkOutDate) {
        Collection<IRoom> availableRooms = new ArrayList<>();
        for (IRoom room : rooms.values()) {
            if (isRoomAvailable(room, checkInDate, checkOutDate)) {
                availableRooms.add(room);
            }
        }
        return availableRooms;
    }


    public Collection<Reservation> getCustomersReservation(Customer customer) {
        Collection<Reservation> customerReservations = new ArrayList<>();
        for (Reservation reservation : reservations) {
            if (reservation.getCustomer().equals(customer)) {
                customerReservations.add(reservation);
            }
        }
        return customerReservations;
    }


    public void printAllReservations() {
        if (reservations.isEmpty()) {
            System.out.println("No reservations");
        } else {
            for (Reservation reservation : reservations) {
                System.out.println(reservation.toString());
            }
        }
    }

    private boolean isDateRangeOverlap(Date start1, Date end1, Date start2, Date end2) {
        return !(start1.after(end2) || end1.before(start2));
    }

    private boolean isRoomAvailable(IRoom room, Date checkInDate, Date checkOutDate) {
        for (Reservation reservation : reservations) {
            if (reservation.getRoom().getRoomNumber().equals(room.getRoomNumber())) {
                if (isDateRangeOverlap(reservation.getCheckInDate(), reservation.getCheckOutDate(), checkInDate, checkOutDate)) {
                    return false;
                }
            }
        }
        return true;
    }

    public Collection<IRoom> recommendRooms(Date checkInDate, Date checkOutDate) {
        Collection<IRoom> recommendedRooms = new ArrayList<>();
        for (IRoom room : rooms.values()) {
            if (isRoomAvailable(room, addDays(checkInDate, 7), addDays(checkOutDate, 7))) {
                recommendedRooms.add(room);
            }
        }
        return recommendedRooms;
    }

    Date addDays(Date checkInDate, int i) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(checkInDate);
        calendar.add(Calendar.DATE, i);
        return calendar.getTime();
    }


}
