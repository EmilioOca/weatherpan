package org.emilio.weatherpan.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Disabled
@SpringBootTest
public class WeatherApiTest {
    @Autowired WeatherApi weatherApi;

    @Test
    public void testCheckDtoIsCompliant() {
        var result = weatherApi.check( 34.6938, 135.501 );
        assertFalse( result.getHourly_units().getWeather_code().isEmpty() ) ;
    }
}
