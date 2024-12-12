import api.AdminResource;
import model.IRoom;
import model.Room;
import model.RoomType;

import java.util.Scanner;

public class AdminMenu {

    public static void admin() {
        int menuOption = 0;
        while (true) {
            System.out.println("""
                    Admin Menu
                    --------------------------------------
                    1. See all Customers;
                    2. See all Rooms;
                    3. See all Reservations;
                    4. Add a Room;
                    5. Back to Main Menu.
                    --------------------------------------
                    """);

            while (true) {
                System.out.print("Please select an option (1-5): ");
                Scanner scanner = new Scanner(System.in);
                try {
                    menuOption = scanner.nextInt();
                    if (menuOption >= 1 && menuOption <= 5) {
                        System.out.println("You selected admin menu option " + menuOption);
                        break;
                    } else {
                        System.out.println("Invalid option. Please enter a number between 1 and 5.");
                    }
                } catch (Exception e) {
                    System.out.println("Invalid input. Please enter a valid number (1-5).");
                }
            }

            switch (menuOption) {
                case 1 -> AdminResource.getAllCustomers();
                case 2 -> AdminResource.getAllRooms();
                case 3 -> AdminResource.displayAllReservations();
                case 4 -> AdminResource.addRoom(inputRoom());
                case 5 -> {
                    System.out.println("Returning to Main Menu...");
                    return;
                }
            }
        }
    }

    public static IRoom inputRoom() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Please enter the room number: ");
        String roomNumber = scanner.nextLine();

        RoomType roomType = null;
        while (roomType == null) {
            System.out.print("Please enter the room type (single/double): ");
            String roomTypeString = scanner.nextLine().trim().toLowerCase();
            if (roomTypeString.equals("single")) {
                roomType = RoomType.SINGLE;
            } else if (roomTypeString.equals("double")) {
                roomType = RoomType.DOUBLE;
            } else {
                System.out.println("Invalid room type. Please enter 'single' or 'double'.");
            }
        }

        double roomPrice = 0;
        while (true) {
            System.out.print("Please enter the room price: ");
            try {
                roomPrice = scanner.nextDouble();
                if (roomPrice >= 0) {
                    break;
                } else {
                    System.out.println("Room price cannot be negative. Please try again.");
                }
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a valid price.");
                scanner.next(); // Clear invalid input
            }
        }

        return new Room(roomNumber, roomPrice, roomType);
    }
}
