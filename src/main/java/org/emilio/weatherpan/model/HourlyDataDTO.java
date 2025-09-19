package org.emilio.weatherpan.model;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class HourlyDataDTO {
    private List<String> time;
    private List<Double> temperature_2m;
    private List<Integer> relative_humidity_2m;
    private List<Integer> weather_code;
}