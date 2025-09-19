package org.emilio.weatherpan.model;

import java.util.Map;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class WeatherResultDTO {
    public static final String NOT_AVAILABLE = "not available";
    private String city;
    private String temperature;
    private String humidity;
    private String weather;

    public static WeatherResultDTO from( String city, WeatherForecastDTO forecast, Map<Integer, String> weatherMap ) {
        WeatherResultDTO weather = new WeatherResultDTO();
        weather.city = city;
        weather.temperature = forecast.getHourly().getTemperature_2m().get( 0 ) + " " + forecast.getHourly_units().getTemperature_2m();
        weather.humidity = forecast.getHourly().getRelative_humidity_2m().get( 0 ) + " " + forecast.getHourly_units().getRelative_humidity_2m();
        weather.weather = weatherMap.get( forecast.getHourly().getWeather_code().get( 0 ) );
        return weather;
    }
    public static WeatherResultDTO missingLocation( String missingCode ) {
        WeatherResultDTO weather = new WeatherResultDTO();
        weather.city = missingCode;
        weather.temperature = NOT_AVAILABLE;
        weather.humidity = NOT_AVAILABLE;
        weather.weather = NOT_AVAILABLE;

        return weather;
    }
}