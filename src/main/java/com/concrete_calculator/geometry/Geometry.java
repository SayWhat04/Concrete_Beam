package com.concrete_calculator.geometry;

public class Geometry {

    Section section;
    double length;

    public Geometry(Section section, double length) {
        this.section = section;
        this.length = length;
    }

    public Section getSection() {
        return section;
    }

    public double getLength() {
        return length;
    }

    public void setSection(Section section) {
        this.section = section;
    }

    public void setLength(double length) {
        this.length = length;
    }
}
