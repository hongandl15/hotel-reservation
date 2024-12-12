package model;

import java.lang.*;

public class Room implements IRoom {
    public final String roomNumber;
    private Double price;
    private final RoomType roomType;

    public Room(String roomNumber, Double price, RoomType roomType) {
        this.roomNumber = roomNumber;
        this.price = price;
        this.roomType = roomType;
    }

    @Override
    public java.lang.String toString() {
        return "Room{" +
                "roomNumber='" + roomNumber + '\'' +
                ", price=" + price +
                ", roomType=" + roomType +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Room other = (Room) obj;
        if (!this.roomNumber.equals(other.roomNumber)) {
            return false;
        }
        if (!this.price.equals(other.price)) {
            return false;
        }
        return this.roomType == other.roomType;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + (this.roomNumber != null ? this.roomNumber.hashCode() : 0);
        hash = 31 * hash + (this.price != null ? this.price.hashCode() : 0);
        hash = 31 * hash + (this.roomType != null ? this.roomType.hashCode() : 0);
        return hash;
    }

    @Override
    public String getRoomNumber() {
        return this.roomNumber;
    }

    @Override
    public Double getRoomPrice() {
        return this.price;
    }

    @Override
    public RoomType getRoomType() {
        return this.roomType;
    }

    public void setRoomPrice(Double price) {
        this.price = price;
    }

    @Override
    public boolean isFree() {
        return false;
    }
}
