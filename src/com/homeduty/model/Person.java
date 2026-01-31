package com.homeduty.model;

public class Person {
    public int personId;
    public String fullName;
    public String email;
    public String roleType;
    public int totalPoints;

    public Person(int personId, String fullName, String email, String roleType, int totalPoints) {
        this.personId = personId;
        this.fullName = fullName;
        this.email = email;
        this.roleType = roleType;
        this.totalPoints = totalPoints;
    }
}
