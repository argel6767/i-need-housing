package com.ineedhousing.new_listings_service.controllers;

import com.ineedhousing.new_listings_service.models.responses.CityDto;
import com.ineedhousing.new_listings_service.services.CityService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/cities")
public class CityController {

    private final CityService cityService;

    public CityController(CityService cityService) {
        this.cityService = cityService;
    }

    @GetMapping()
    public List<CityDto> findAll() {
        return cityService.findAll();
    }

    @GetMapping("/{cityName}")
    public CityDto findByCityName(@PathVariable String cityName) {
        return cityService.findByName(cityName);
    }

    @PostMapping()
    public ResponseEntity<CityDto> createCity(@RequestBody CityDto cityDto) {
       return ResponseEntity.status(HttpStatus.CREATED).body(cityService.addCity(cityDto));
    }

    @PostMapping("/bulk")
    public ResponseEntity<List<CityDto>> createCityBulk(@RequestBody List<CityDto> cityDtos) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cityService.bulkAddCity(cityDtos));
    }

    @PutMapping()
    public CityDto updateCity(@RequestBody CityDto cityDto) {
        return cityService.updateCity(cityDto);
    }

    @DeleteMapping("/{cityName}")
    public ResponseEntity<Void> deleteCity(@PathVariable String cityName) {
        cityService.deleteCity(cityName);
        return ResponseEntity.noContent().build();
    }
}
