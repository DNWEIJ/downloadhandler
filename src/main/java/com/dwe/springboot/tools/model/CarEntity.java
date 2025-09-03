package com.dwe.springboot.tools.model;


import jakarta.persistence.*;

@Entity
public class CarEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long id;
    String driveDate;
    String carType;
    String person;
    int kmTotal;
    int km;
    @Column(name="petrol",columnDefinition = "bit default 0")
    boolean petrol;
    @Column(name="liters",columnDefinition = "integer default 0")
    int liters;

    @Transient
    public String getLitersStr() {
        return (liters == 0 ? "" : Integer.toString(liters));
    }
    public void setLitersStr(String yearStr) {
        this.liters = (yearStr.isEmpty() ? 0 : Integer.parseInt(yearStr));
    }

    public CarEntity() {
    }


    @Override
    public String toString() {
        return driveDate + ";" + carType + ";" + person + ";" + kmTotal + ";" + km + ";" + petrol + ";"+ liters + ";";
    }

    public String toHtmlString() {
        if (petrol) {
            return person + " drove:<br/>" + carType + " at " + driveDate + "<br/>for: " + km + " km and tanked: " + liters;
        }
        return person + " drove:<br/>" + carType + " at " + driveDate + "<br/>for: " + km + " km";
    }

    public boolean isValid() {
        return !driveDate.isEmpty()
                && !carType.isEmpty()
                && !person.isEmpty()
                && kmTotal != 0
                && km != 0;
    }

    public boolean getPetrol() {
        return petrol;
    }

    public void setPetrol(boolean petrol) {
        this.petrol = petrol;
    }

    public int getLiters() {
        return liters;
    }

    public void setLiters(int liters) {
        this.liters = liters;
    }

    public String getDriveDate() {
        return driveDate;
    }

    public void setDriveDate(String driveDate) {
        this.driveDate = driveDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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