package com.example.demo.model;

import java.io.Serializable;
import java.util.Objects;

public class ClientPhoneId implements Serializable {
    private Integer clientId;
    private String phoneNumber;

    // Default constructor
    public ClientPhoneId() {
    }

    // Parameterized constructor
    public ClientPhoneId(Integer clientId, String phoneNumber) {
        this.clientId = clientId;
        this.phoneNumber = phoneNumber;
    }

    // Getters and Setters
    public Integer getClientId() {
        return clientId;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ClientPhoneId)) return false;
        ClientPhoneId that = (ClientPhoneId) o;
        return Objects.equals(clientId, that.clientId) &&
                Objects.equals(phoneNumber, that.phoneNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clientId, phoneNumber);
    }
}
