package org.emilio.weatherpan.service;

import java.util.LinkedHashMap;
import java.util.Map;
import org.emilio.weatherpan.config.CityLocation;
import org.emilio.weatherpan.config.WeatherConfig;
import org.emilio.weatherpan.model.WeatherResultDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WeatherService {
    @Autowired WeatherApi weatherApi;
    @Autowired WeatherConfig weatherConfig;

    public WeatherResultDTO checkCity( String cityCode ) {
        CityLocation location = weatherConfig.getLocations().get( cityCode );
        if ( location == null ) {
            return WeatherResultDTO.missingLocation( cityCode );
        }
        return WeatherResultDTO.from( location.getName(),
                                      weatherApi.check( location.getLatitude(), location.getLatitude() ),
                                      weatherConfig.getCodes() ) ;
    }

    public Map info() {
        LinkedHashMap<String, String> response = new LinkedHashMap<>();
        response.put(  "service", "available" );
        response.put(  "valid_cities_codes", String.join( ", ", weatherConfig.getLocations().keySet() ) );
        response.put(  "url", "/api/v1/weatherpan/{citiCode}" );
        response.put(  "info", "/api/v1/weatherpan/info" );
        response.put(  "swagger", "/swagger-ui/index.htmly" );

        return response;

    }
}
