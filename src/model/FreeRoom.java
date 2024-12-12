package model;

public class FreeRoom extends Room {
    public FreeRoom(String roomNumber, Double price, RoomType roomType) {
        super(roomNumber, price, roomType);
        this.setRoomPrice((double) 0);
    }


    @Override
    public String toString() {
        return "FreeRoom{}";
    }
}
