package org.emilio.weatherpan.config;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CityLocation {
    private String code;
    private String name;
    private double latitude;
    private double longitude;

}