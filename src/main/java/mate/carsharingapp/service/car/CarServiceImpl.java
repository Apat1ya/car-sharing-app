package mate.carsharingapp.service.car;

import lombok.RequiredArgsConstructor;
import mate.carsharingapp.dto.car.CarRequestDto;
import mate.carsharingapp.dto.car.CarResponseDto;
import mate.carsharingapp.exception.EntityNotFoundException;
import mate.carsharingapp.mapper.CarMapper;
import mate.carsharingapp.model.car.Car;
import mate.carsharingapp.repository.car.CarRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CarServiceImpl implements CarService {
    private final CarRepository carRepository;
    private final CarMapper carMapper;

    @Override
    public CarResponseDto create(CarRequestDto requestDto) {
        Car car = carMapper.toModel(requestDto);
        carRepository.save(car);
        return carMapper.toResponseDto(car);
    }

    @Override
    public Page<CarResponseDto> findAll(Pageable pageable) {
        return carRepository.findAll(pageable)
                .map(carMapper::toResponseDto);
    }

    @Override
    public CarResponseDto findById(Long id) {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Car not found by id: " + id));
        return carMapper.toResponseDto(car);
    }

    @Override
    public CarResponseDto update(Long id, CarRequestDto requestDto) {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Car not found by id: " + id));
        carMapper.updateCarFromDto(requestDto, car);
        carRepository.save(car);
        return carMapper.toResponseDto(car);
    }

    @Override
    public void delete(Long id) {
        carRepository.deleteById(id);
    }
}
