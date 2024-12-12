import api.AdminResource;
import api.HotelResource;
import model.Customer;
import model.IRoom;
import model.Reservation;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Scanner;

public class MainMenu {
    final static AdminResource resource = new AdminResource();

    public static void mainMenu() {
        int menuOption = 0;
        while (true) {
            displayMenu();
            while (true) {
                Scanner scanner = new Scanner(System.in);
                try {
                    menuOption = scanner.nextInt();
                    if (1 <= menuOption && menuOption <= 5) {
                        System.out.printf("You selected menu option %d.%n", menuOption);
                    }
                    break;
                } catch (Exception e) {
                    System.out.println("Invalid input. Please enter a number (1-5). Try again:");
                }
            }
            switch (menuOption) {
                case 1 -> findAndReserveARoom();
                case 2 -> seeMyReservations();
                case 3 -> createAnAccount();
                case 4 -> AdminMenu.admin();
                case 5 -> {
                    System.out.println("Exiting the application. Goodbye!");
                    return;
                }
                default -> System.out.println("Unexpected error. Please enter a valid number (1-5). Restarting menu...");
            }
        }
    }

    private static void displayMenu() {
        System.out.println("\nMain Menu:\n" +
                "1. Find and reserve a room\n" +
                "2. See my reservations\n" +
                "3. Create an account\n" +
                "4. Admin\n" +
                "5. Exit\n");
    }

    private static String emailInput() {
        System.out.println("Enter your email address (e.g., name@domain.com):");
        String email;
        while (true) {
            Scanner scanner = new Scanner(System.in);
            email = scanner.nextLine();
            if (email.matches("^(.+)@(.+).(.+)$")) {
                break;
            } else {
                System.out.println("Invalid email format. Please try again:");
            }
        }
        return email;
    }

    private static Date addDays(Date checkInDate, int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(checkInDate);
        calendar.add(Calendar.DATE, days);
        return calendar.getTime();
    }

    private static void findAndReserveARoom() {
        String email = emailInput().toLowerCase();
        Customer customer = resource.getCustomer(email);
        if (customer == null) {
            System.out.println("Customer not found. Redirecting to account creation...");
            createAnAccount();
        } else {
            Date checkIn = getDateInput("check-in");
            Date checkOut = getDateInput("check-out", checkIn);
            boolean changeDates = false;

            Collection<IRoom> rooms = resource.findARoom(checkIn, checkOut);
            if (!rooms.isEmpty()) {
                System.out.println("Available rooms:");
                rooms.forEach(room -> System.out.println(room));
            } else {
                System.out.println("No rooms found for the selected dates. Checking alternative dates...");
                rooms = resource.recommendRooms(checkIn, checkOut);
                if (!rooms.isEmpty()) {
                    System.out.println("Recommended alternative dates: +7 days from your selection.");
                    displayRecommendations(checkIn, checkOut, rooms);
                    changeDates = confirmChangeDates();
                } else {
                    System.out.println("No recommendations available. Returning to menu.");
                    return;
                }
            }

            if (changeDates) {
                checkIn = addDays(checkIn, 7);
                checkOut = addDays(checkOut, 7);
            }

            String roomNumber = selectRoom(rooms);
            Reservation reservation = resource.bookARoom(email, resource.getRoom(roomNumber), checkIn, checkOut);
            System.out.println("Reservation confirmed: " + reservation);
        }
    }

    private static Date getDateInput(String type) {
        return getDateInput(type, null);
    }

    private static Date getDateInput(String type, Date afterDate) {
        System.out.printf("Enter your %s date (yyyy-mm-dd):%n", type);
        Date date;
        while (true) {
            Scanner scanner = new Scanner(System.in);
            String dateInput = scanner.nextLine();
            date = HotelResource.parseDate(dateInput);
            if (date != null && (afterDate == null || date.after(afterDate))) {
                System.out.printf("Selected %s date: %s.%n", type, date);
                break;
            } else {
                System.out.printf("Invalid %s date. Please try again:%n", type);
            }
        }
        return date;
    }

    private static void displayRecommendations(Date checkIn, Date checkOut, Collection<IRoom> rooms) {
        System.out.printf("Suggested new check-in: %s, check-out: %s.%n", addDays(checkIn, 7), addDays(checkOut, 7));
        System.out.println("Recommended rooms:");
        rooms.forEach(room -> System.out.println(room));
    }

    private static boolean confirmChangeDates() {
        System.out.println("Do you want to use the suggested dates? (y/n):");
        while (true) {
            Scanner scanner = new Scanner(System.in);
            String response = scanner.nextLine();
            if (response.equalsIgnoreCase("y")) {
                return true;
            } else if (response.equalsIgnoreCase("n")) {
                return false;
            } else {
                System.out.println("Invalid response. Enter 'y' for yes or 'n' for no:");
            }
        }
    }

    private static String selectRoom(Collection<IRoom> rooms) {
        System.out.println("Enter the room number you want to reserve:");
        String roomNumber;
        while (true) {
            Scanner scanner = new Scanner(System.in);
            roomNumber = scanner.nextLine();
            String finalRoomNumber = roomNumber;
            if (rooms.stream().anyMatch(room -> room.getRoomNumber().equals(finalRoomNumber))) {
                break;
            } else {
                System.out.println("Invalid room number. Please try again:");
            }
        }
        return roomNumber;
    }

    private static void createAnAccount() {
        String firstName = getValidatedInput("first name", "^[a-zA-Z]+$", "Invalid first name. Only alphabet characters are allowed.");
        String lastName = getValidatedInput("last name", "^[a-zA-Z]+$", "Invalid last name. Only alphabet characters are allowed.");
        String email = emailInput().toLowerCase();

        Customer customer = resource.getCustomer(email);
        if (customer == null) {
            resource.createACustomer(email, firstName, lastName);
            System.out.println("Account created successfully!");
        } else {
            System.out.println("Account already exists. Returning to menu.");
        }
    }

    private static String getValidatedInput(String inputType, String regex, String errorMessage) {
        System.out.printf("Enter your %s:%n", inputType);
        String input;
        while (true) {
            Scanner scanner = new Scanner(System.in);
            input = scanner.nextLine();
            if (input.matches(regex)) {
                break;
            } else {
                System.out.println(errorMessage);
            }
        }
        return input;
    }

    private static void seeMyReservations() {
        String email = emailInput().toLowerCase();
        Customer customer = resource.getCustomer(email);
        if (customer == null) {
            System.out.println("Customer not found. Redirecting to account creation...");
            createAnAccount();
        } else {
            Collection<Reservation> reservations = resource.getCustomerReservations(email);
            if (!reservations.isEmpty()) {
                System.out.println("Your reservations:");
                reservations.forEach(reservation -> System.out.println(reservation));
            } else {
                System.out.println("No reservations found.");
            }
        }
    }
}
