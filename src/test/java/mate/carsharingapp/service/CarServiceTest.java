package mate.carsharingapp.service;

import static mate.carsharingapp.util.TestUtil.createFirstCar;
import static mate.carsharingapp.util.TestUtil.createFirstCarDto;
import static mate.carsharingapp.util.TestUtil.createSecondCar;
import static mate.carsharingapp.util.TestUtil.createSecondCarDto;
import static mate.carsharingapp.util.TestUtil.createThirdCar;
import static mate.carsharingapp.util.TestUtil.createThirdCarDto;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import mate.carsharingapp.dto.car.CarRequestDto;
import mate.carsharingapp.dto.car.CarResponseDto;
import mate.carsharingapp.exception.EntityNotFoundException;
import mate.carsharingapp.mapper.CarMapper;
import mate.carsharingapp.model.car.Car;
import mate.carsharingapp.model.car.Type;
import mate.carsharingapp.repository.car.CarRepository;
import mate.carsharingapp.service.car.CarServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
public class CarServiceTest {
    @Mock
    private CarRepository carRepository;

    @Mock
    private CarMapper carMapper;

    @InjectMocks
    private CarServiceImpl carService;

    @Test
    @DisplayName("create car")
    void createCar_WithValidRequestOk() {
        CarRequestDto requestDto = new CarRequestDto();
        requestDto.setModel("test model");
        requestDto.setBrand("test brand");
        requestDto.setType(Type.valueOf("SEDAN"));
        requestDto.setDailyFee(BigDecimal.valueOf(9.99));
        Car savedCar = new Car();
        savedCar.setModel("test model");
        savedCar.setBrand("test brand");
        savedCar.setType(Type.valueOf("SEDAN"));
        savedCar.setDailyFee(BigDecimal.valueOf(9.99));
        CarResponseDto expectedCar = new CarResponseDto();
        expectedCar.setModel("test model");
        expectedCar.setBrand("test brand");
        expectedCar.setType(Type.valueOf("SEDAN"));
        expectedCar.setDailyFee(BigDecimal.valueOf(9.99));

        when(carMapper.toModel(requestDto)).thenReturn(savedCar);
        when(carRepository.save(savedCar)).thenReturn(savedCar);
        when(carMapper.toResponseDto(savedCar)).thenReturn(expectedCar);

        CarResponseDto actualDto = carService.create(requestDto);

        assertEquals(expectedCar, actualDto);
        verify(carMapper).toModel(requestDto);
        verify(carRepository).save(savedCar);
        verify(carMapper).toResponseDto(savedCar);
    }

    @Test
    @DisplayName("find all")
    void findAllCar_ThreeCars_Ok() {
        List<CarResponseDto> carDtos = List.of(createFirstCarDto(),
                createSecondCarDto(),
                createThirdCarDto()
        );
        List<Car> cars = List.of(createFirstCar(),
                createSecondCar(),
                createThirdCar());
        Pageable pageable = PageRequest.of(0,10);
        Page<Car> carPage = new PageImpl<>(cars,pageable,3);

        when(carRepository.findAll(pageable)).thenReturn(carPage);
        when(carMapper.toResponseDto(cars.get(0))).thenReturn(carDtos.get(0));
        when(carMapper.toResponseDto(cars.get(1))).thenReturn(carDtos.get(1));
        when(carMapper.toResponseDto(cars.get(2))).thenReturn(carDtos.get(2));
        Page<CarResponseDto> result = carService.findAll(pageable);

        Page<CarResponseDto> expected = new PageImpl<>(carDtos, pageable,3);
        assertEquals(expected,result);
        verify(carRepository).findAll(pageable);
        verify(carMapper).toResponseDto(cars.get(0));
        verify(carMapper).toResponseDto(cars.get(1));
        verify(carMapper).toResponseDto(cars.get(2));
    }

    @Test
    @DisplayName("find by valid id")
    void findCarById_WithValidId_OK() {
        Long carId = 1L;
        Car car = createFirstCar();
        CarResponseDto responseDto = createFirstCarDto();
        when(carRepository.findById(carId)).thenReturn(Optional.of(car));
        when(carMapper.toResponseDto(car)).thenReturn(responseDto);

        CarResponseDto actual = carService.findById(carId);

        assertEquals(actual,responseDto);
        verify(carRepository).findById(carId);
        verify(carMapper).toResponseDto(car);
    }

    @Test
    @DisplayName("find car with invalid id")
    void findCarById_WithInvalidId_NotOk() {
        Long carId = 99L;
        when(carRepository.findById(carId)).thenReturn(Optional.empty());
        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> carService.findById(carId)
        );
        assertEquals("Car not found by id: " + carId, exception.getMessage());
        verify(carRepository).findById(carId);
    }

    @Test
    @DisplayName("Delete car by valid id")
    void deleteCar_WithValidId_Ok() {
        Long carId = 1L;

        carService.delete(carId);
        verify(carRepository).deleteById(carId);
    }
}
