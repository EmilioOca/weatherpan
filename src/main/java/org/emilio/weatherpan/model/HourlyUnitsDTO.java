package org.emilio.weatherpan.model;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class HourlyUnitsDTO {
    private String time;
    private String temperature_2m;
    private String relative_humidity_2m;
    private String weather_code;
}