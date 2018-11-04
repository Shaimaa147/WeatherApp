package com.example.albadr.weatherapp;

import org.junit.Test;

import static org.junit.Assert.*;

public class ConversionTest {

    @Test
    public void convertToCelsius() {
        double input = 301.15;
        double output;
        double expected = 28;
        double delta= -1;

        output = Conversion.convertToCelsius(input);

        assertEquals(expected, output, delta);
    }


}