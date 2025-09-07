package com.dwe.springboot.tools.car.model;


import jakarta.persistence.*;

@Entity(name = "car_entity")
public class TripEntity {
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

    private TripEntity(Builder builder) {
        setDriveDate(builder.driveDate);
        setCarType(builder.carType);
        setPerson(builder.person);
        setKmTotal(builder.kmTotal);
        setKm(builder.km);
        setPetrol(builder.petrol);
        setLiters(builder.liters);
    }

    public TripEntity() {
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


    @Transient
    public String getLitersStr() {
        return (liters == 0 ? "" : Integer.toString(liters));
    }
    public void setLitersStr(String yearStr) {
        this.liters = (yearStr.isEmpty() ? 0 : Integer.parseInt(yearStr));
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

    public static final class Builder {
        private Long id;
        private String driveDate;
        private String carType;
        private String person;
        private int kmTotal;
        private int km;
        private boolean petrol;
        private int liters;

        private Builder() {
        }

        public static Builder newBuilder() {
            return new Builder();
        }

        public Builder id(Long val) {
            id = val;
            return this;
        }

        public Builder driveDate(String val) {
            driveDate = val;
            return this;
        }

        public Builder carType(String val) {
            carType = val;
            return this;
        }

        public Builder person(String val) {
            person = val;
            return this;
        }

        public Builder kmTotal(int val) {
            kmTotal = val;
            return this;
        }

        public Builder km(int val) {
            km = val;
            return this;
        }

        public Builder petrol(boolean val) {
            petrol = val;
            return this;
        }

        public Builder liters(int val) {
            liters = val;
            return this;
        }

        public TripEntity build() {
            return new TripEntity(this);
        }
    }
}