package model;

import java.util.regex.Pattern;

public class Customer {
    String firstName;
    String lastName;
    String email;


    public Customer(String firstName, String lastName, String email) {
        validateEmail(email);
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    private void validateEmail(String email) {
        String emailRegex = "^(.+)@(.+).(.+)$";
        Pattern pattern = Pattern.compile(emailRegex);

        if (!pattern.matcher(email).matches()) {
            throw new IllegalArgumentException("Invalid email format!");
        }
    }

    @Override
    public String toString() {
        return "Customer{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

}
