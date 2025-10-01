package com.ineedhousing.new_listings_service.services;

import com.ineedhousing.new_listings_service.models.data.City;
import com.ineedhousing.new_listings_service.models.responses.CityDto;
import com.ineedhousing.new_listings_service.repositories.CityRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CityService {

    private final CityRepository cityRepository;

    public CityService(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    public List<CityDto> findAll() {
        return cityRepository.findAll()
                .stream()
                .map(CityDto::createDto)
                .toList();
    }

    public CityDto findByName(String cityName) {
        City city =  cityRepository.findByCityName(cityName).orElse(null);
        if (city != null) {
            return CityDto.createDto(city);
        }
        return null;
    }

    public CityDto addCity(CityDto cityDto) {
        City city = new City(cityDto.cityName(), cityDto.latitude(), cityDto.longitude());
        cityRepository.save(city);
        return CityDto.createDto(city);
    }

    public List<CityDto> bulkAddCity(List<CityDto> cityDtos) {
        List<City> cities = cityDtos.stream()
                .map(cityDto -> new City(cityDto.cityName(), cityDto.latitude(), cityDto.longitude()))
                .toList();
        cityRepository.saveAll(cities);

        return cities.stream()
                .map(CityDto::createDto)
                .toList();
    }

    public CityDto updateCity(CityDto cityDto) {
        City city = cityRepository.findByCityName(cityDto.cityName()).orElse(null);
        if (city == null) {
            return null;
        }

        city.setCityName(cityDto.cityName());
        city.setLatitude(cityDto.latitude());
        city.setLongitude(cityDto.longitude());
        cityRepository.save(city);
        return CityDto.createDto(city);
    }

    public void deleteCity(String cityName) {
        cityRepository.deleteByCityName(cityName);
    }
}
