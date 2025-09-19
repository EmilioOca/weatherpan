package org.emilio.weatherpan.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.emilio.weatherpan.model.WeatherResultDTO;
import org.emilio.weatherpan.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping( "api/v1/weatherpan" )
@Slf4j
@Tag( name = "WeatherPan", description = "Endpoints for checking the weather in your loved city" )
public class WeatherController {
    public static final String SERVICE_UNAVAILABLE = "Service unavailable";
    @Autowired WeatherService weatherService;

    @ExceptionHandler( Exception.class )
    public ResponseEntity<String> handleIllegalArgument( Exception ex ) {
        log.error( ex.getMessage(), ex );
        return ResponseEntity.internalServerError().body( SERVICE_UNAVAILABLE );
    }

    @GetMapping( "/{city}" )
    @Operation( summary = "Get weather conditions for a suported city",
                description = "Offers weather details." )
    @ApiResponse( responseCode = "200", description = "Weather conditions retrieved sucessfuly",
                  content = @Content( schema = @Schema(implementation = WeatherResultDTO.class),
                                      examples = @ExampleObject( name = "Weather condition response",
                                                                 value = """
                                                                         { "city": "Tokyo",
                                                                           "temperature": "27.8 °C",
                                                                           "humidity": "77 %",
                                                                           "weather": "Mainly clear"
                                                                         }""" )))
    public ResponseEntity chekCity( @PathVariable String city ) {
        return ResponseEntity.ok( weatherService.checkCity( city.toLowerCase() ) );
    }

    @GetMapping( "/info" )
    public ResponseEntity info() {
        return ResponseEntity.ok( weatherService.info() );
    }

}
