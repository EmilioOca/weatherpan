package org.emilio.weatherpan.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties( prefix = "weather" )
public class WeatherConfig {

    private Map<Integer, String> codes = new HashMap<>();
    private Map<String, CityLocation> locations = new HashMap<>();
    private List<String> parameters = new ArrayList<>();

    public Map<Integer, String> getCodes() {
        return codes;
    }

    public Map<String, CityLocation> getLocations() {
        return locations;
    }

    public List<String> getParameters() {
        return parameters;
    }

    public void setLocations( List<CityLocation> props ) {
        locations = props.stream()
                         .collect( Collectors.toMap( CityLocation::getCode,
                                                     location -> location ) );
    }
}