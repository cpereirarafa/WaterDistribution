package com.university.finalwork.database;

public class Users {

    private String userId;
    private boolean isAdmin;
    private String userName;
    private String userAddress;
    private String userCity;
    private String userEmail;

    public Users(String userId, boolean isAdmin, String userName, String userAddress, String userCity, String userEmail) {
        this.userId = userId;
        this.isAdmin = isAdmin;
        this.userName = userName;
        this.userAddress = userAddress;
        this.userCity = userCity;
        this.userEmail = userEmail;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public String getUserCity() {
        return userCity;
    }

    public void setUserCity(String userCity) {
        this.userCity = userCity;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}
