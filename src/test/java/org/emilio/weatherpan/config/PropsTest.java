package org.emilio.weatherpan.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class PropsTest {
    @Autowired WeatherConfig weatherConfig;

    @Test
    void wheaderCodesTest() {
        assertEquals( "Clear sky", weatherConfig.getCodes().get( 0 ) );
    }

    @Test
    void locationsTest() {
        assertEquals( "Osaka", weatherConfig.getLocations().get( "osaka" ).getName() );
    }

    @Test
    void missigKeyFailsGently() {
        assertNull( weatherConfig.getCodes().get( 102 ) );
        assertNull( weatherConfig.getLocations().get( "buenosAires" ) );
    }

}
