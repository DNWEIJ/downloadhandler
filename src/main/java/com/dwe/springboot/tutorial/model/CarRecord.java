package com.dwe.springboot.tutorial.model;

public record CarRecord(String date, String car, String person, String kidsRun, int kmTotal, int km) {

    @Override
    public String toString() {
        return date + ";" + car + ";" + person + ";" + kidsRun + ";" + kmTotal + ";" + km + ";";
    }
}