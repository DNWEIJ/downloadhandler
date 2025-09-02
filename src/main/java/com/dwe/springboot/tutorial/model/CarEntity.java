package com.dwe.springboot.tutorial.model;


import jakarta.persistence.*;

@Entity
public class CarEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    String id;
    String driveDate;
    String carType;
    String person;
    int kmTotal;
    int km;

    public CarEntity() {
    }


    @Override
    public String toString() {
        return STR."\{driveDate};\{carType};\{person};\{kmTotal};\{km};";
    }

    public boolean isValid() {
        if (!driveDate.isEmpty() && !carType.isEmpty() && !person.isEmpty() && kmTotal != 0 && km != 0) {
            return true;
        } else {
            return false;
        }
    }

    public String getDriveDate() {
        return driveDate;
    }

    public void setDriveDate(String driveDate) {
        this.driveDate = driveDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getCarType() {
        return carType;
    }

    public void setCarType(String car) {
        this.carType = car;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public int getKmTotal() {
        return kmTotal;
    }

    public void setKmTotal(int kmTotal) {
        this.kmTotal = kmTotal;
    }

    public int getKm() {
        return km;
    }

    public void setKm(int km) {
        this.km = km;
    }
}