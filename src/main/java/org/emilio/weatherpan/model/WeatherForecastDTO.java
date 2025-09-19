package org.emilio.weatherpan.model;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class WeatherForecastDTO {
    private double latitude;
    private double longitude;
    private double generationtime_ms;
    private int utc_offset_seconds;
    private String timezone;
    private String timezone_abbreviation;
    private double elevation;
    private HourlyUnitsDTO hourly_units;
    private HourlyDataDTO hourly;
}