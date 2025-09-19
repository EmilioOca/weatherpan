package org.emilio.weatherpan.controller;

import static org.emilio.weatherpan.controller.WeatherController.SERVICE_UNAVAILABLE;
import static org.emilio.weatherpan.model.WeatherResultDTO.NOT_AVAILABLE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import org.emilio.weatherpan.model.WeatherForecastDTO;
import org.emilio.weatherpan.model.WeatherResultDTO;
import org.emilio.weatherpan.service.WeatherApi;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
public class WeatherControllerTest {
    static private final String base = "/api/v1/weatherpan";

    @Autowired MockMvc mockMvc;
    @MockBean WeatherApi weatherApi;

    @BeforeEach public void beforeEach() throws Exception {
        when( weatherApi.check( any(), any() ) ).thenReturn( new ObjectMapper().readValue( tokyo(), WeatherForecastDTO.class ) );
    }

    @Test public void testValidResponse() throws Exception {
        assertEquals( "Tokyo", postCity( "tokyo" ).getCity() );
        assertEquals( "Tokyo", postCity( "toKyo" ).getCity() );
        assertEquals( "Tokyo", postCity( "Tokyo" ).getCity() );
    }

    @Test public void testMissingCity() throws Exception {
        WeatherResultDTO result = postCity( "bsas" );
        assertEquals( "bsas", result.getCity() );
        assertEquals( NOT_AVAILABLE, result.getHumidity() );
    }

    @Test public void testMalformedResponse() throws Exception {
        when( weatherApi.check( any(), any() ) ).thenThrow( new RuntimeException( "Something Failed" ) );

        var result = mockMvc.perform( MockMvcRequestBuilders.get( base + "/" + "osaka" ) )
                            .andDo( MockMvcResultHandlers.print() )
                            .andExpect( MockMvcResultMatchers.status().is( 500 ) )
                            .andReturn()
                            .getResponse().getContentAsString();

        assertEquals( SERVICE_UNAVAILABLE, result );
    }

    @Test public void testEmptyRequest() throws Exception {

        var result = mockMvc.perform( MockMvcRequestBuilders.get( base + "/" ) )
                            .andDo( MockMvcResultHandlers.print() )
                            .andExpect( MockMvcResultMatchers.status().is( 404 ) )
                            .andReturn()
                            .getResponse().getContentAsString();

        assertTrue( result.isEmpty() );
    }

    @Test public void testInfo() throws Exception {

        Map<String, String> result = new ObjectMapper().readValue( mockMvc.perform( MockMvcRequestBuilders.get( base + "/info" ) )
                                                                          .andDo( MockMvcResultHandlers.print() )
                                                                          .andExpect( MockMvcResultMatchers.status().is( 200 ) )
                                                                          .andReturn()
                                                                          .getResponse()
                                                                          .getContentAsString(),
                                                                   Map.class );
        assertEquals( "available", result.get( "service" ) );
        assertTrue( result.get( "valid_cities_codes" ).contains( "tokyo" ) );
        assertEquals( base + "/info", result.get( "info" ) );
    }

    public WeatherResultDTO postCity( String city ) throws Exception {
        return new ObjectMapper().readValue( mockMvc.perform( get( base + "/" + city ) )
                                                    .andDo( print() )
                                                    .andExpect( status().is( 200 ) )
                                                    .andReturn()
                                                    .getResponse()
                                                    .getContentAsString(),
                                             WeatherResultDTO.class );
    }

    public static String tokyo() {
        return """
                {
                  "latitude": 34.7,
                  "longitude": 135.5,
                  "generationtime_ms": 0.037670135498046875,
                  "utc_offset_seconds": 0,
                  "timezone": "GMT",
                  "timezone_abbreviation": "GMT",
                  "elevation": 5,
                  "hourly_units": {
                    "time": "iso8601",
                    "temperature_2m": "°C",
                    "relative_humidity_2m": "%",
                    "weather_code": "wmo code"
                  },
                  "hourly": {
                    "time": [ "2025-09-16T23:00" ],
                    "temperature_2m": [ 26 ],
                    "relative_humidity_2m": [ 86 ],
                    "weather_code": [ 1 ]
                  }
                }""";
    }
}
