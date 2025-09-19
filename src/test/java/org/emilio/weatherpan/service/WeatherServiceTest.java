package org.emilio.weatherpan.service;

import static org.emilio.weatherpan.model.WeatherResultDTO.NOT_AVAILABLE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.emilio.weatherpan.controller.WeatherControllerTest;
import org.emilio.weatherpan.model.WeatherForecastDTO;
import org.emilio.weatherpan.model.WeatherResultDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;


@SpringBootTest
class WeatherServiceTest {
    @Autowired WeatherService weatherService;
    @MockBean WeatherApi weatherApi;

    @BeforeEach public void beforeEach() throws Exception {
        when( weatherApi.check( any(), any() ) ).thenReturn( new ObjectMapper().readValue( WeatherControllerTest.tokyo(), WeatherForecastDTO.class ) );
    }

    @Test
    void checkValidCity() {
        WeatherResultDTO result = weatherService.checkCity( "osaka" );
        assertEquals( "Osaka", result.getCity() );
        assertEquals( "26.0 °C", result.getTemperature() );
        assertEquals( "86 %", result.getHumidity() );
        assertEquals( "Mainly clear", result.getWeather() );
    }

    @Test
    void checkBsAsCity() {
        WeatherResultDTO result = weatherService.checkCity( "bsas" );
        assertEquals( "bsas", result.getCity() );
        assertEquals( NOT_AVAILABLE, result.getTemperature() );
        assertEquals( NOT_AVAILABLE, result.getHumidity() );
        assertEquals( NOT_AVAILABLE, result.getWeather() );
    }
}