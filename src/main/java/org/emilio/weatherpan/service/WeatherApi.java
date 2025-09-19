package org.emilio.weatherpan.service;

import java.util.stream.Collectors;
import org.emilio.weatherpan.config.WeatherConfig;
import org.emilio.weatherpan.model.WeatherForecastDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class WeatherApi {
    @Autowired
    WeatherConfig weatherConfig;

    @Value("${weather.api.url-base}")
    private String urlBase;

    public WeatherForecastDTO check( Double lat, Double lon ) {
        String queryParams = weatherConfig.getParameters()
                                          .stream()
                                          .collect( Collectors.joining( "&" ) );

        String fullUrl = String.format( "https://%s?latitude=%.4f&longitude=%.4f&%s",
                                        urlBase, lat, lon, queryParams );

        return RestClient.create()
                         .get()
                         .uri( fullUrl )
                         .retrieve()
                         .body( WeatherForecastDTO.class );
    }

}