package mate.carsharingapp.controller;

import static mate.carsharingapp.util.TestUtil.createFirstCarDto;
import static mate.carsharingapp.util.TestUtil.createFirstCarRequestDto;
import static mate.carsharingapp.util.TestUtil.createSecondCarDto;
import static mate.carsharingapp.util.TestUtil.createThirdCarDto;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import mate.carsharingapp.dto.car.CarResponseDto;
import mate.carsharingapp.service.car.CarServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc(addFilters = false)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
public class CarControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @InjectMocks
    private CarServiceImpl carService;

    @Test
    @DisplayName("return all cars")
    @Sql(scripts = "classpath:database/add-three-cars.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/delete-three-cars.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @WithMockUser(username = "user", roles = "CUSTOMER")
    void getAll_ThreeCars_Ok() throws Exception {
        MvcResult result = mockMvc.perform(get("/cars")
                .param("page", "0")
                .param("size", "10")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        List<CarResponseDto> cars = List.of(
                createFirstCarDto(),
                createSecondCarDto(),
                createThirdCarDto()
        );
        String json = result.getResponse().getContentAsString();
        JsonNode content = objectMapper.readTree(json).get("content");
        JavaType type = objectMapper.getTypeFactory()
                .constructCollectionType(List.class, CarResponseDto.class);
        List<CarResponseDto> actual = objectMapper.readValue(content.toString(),type);

        assertThat(actual).containsExactlyInAnyOrderElementsOf(cars);
    }

    @Test
    @DisplayName("find car by valid id")
    @WithMockUser(username = "user", roles = "CUSTOMER")
    @Sql(scripts = "classpath:database/add-three-cars.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/delete-three-cars.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findById_WithValidId() throws Exception {
        MvcResult result = mockMvc.perform(get("/cars/2")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        CarResponseDto expected = createSecondCarDto();
        String json = result.getResponse().getContentAsString();
        CarResponseDto actual = objectMapper.readValue(json, CarResponseDto.class);
        assertThat(expected).usingRecursiveComparison().isEqualTo(actual);
    }

    @Test
    @DisplayName("find car by invalid id should return 404")
    @WithMockUser(username = "user", roles = "CUSTOMER")
    void findCar_WithInvalidId_NotOk() throws Exception {
        mockMvc.perform(get("/cars/54"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("create car with valid request")
    @WithMockUser(username = "admin", roles = "MANAGER")
    void createCar_WithValidRequest_Ok() throws Exception {
        String carRequest = objectMapper.writeValueAsString(createFirstCarRequestDto());
        CarResponseDto expected = createFirstCarDto();
        MvcResult result = mockMvc.perform(post("/cars")
                .content(carRequest)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();
        CarResponseDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), CarResponseDto.class
        );
        assertThat(expected)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(actual);
    }

    @Test
    @DisplayName("update car with valid request")
    @WithMockUser(username = "admin", roles = "MANAGER")
    @Sql(scripts = "classpath:database/add-three-cars.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/delete-three-cars.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void updateCar_WithValidRequest_Ok() throws Exception {
        String carRequest = objectMapper.writeValueAsString(createFirstCarRequestDto());
        CarResponseDto expected = createFirstCarDto();
        MvcResult result = mockMvc.perform(patch("/cars/2")
                .content(carRequest)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        CarResponseDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), CarResponseDto.class
        );
        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(expected);
    }

    @Test
    @DisplayName("delete car by valid id should return 204")
    @WithMockUser(username = "admin", roles = "MANAGER")
    @Sql(scripts = "classpath:database/add-three-cars.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/delete-three-cars.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void deleteCar_WithValidId_Ok() throws Exception {
        mockMvc.perform(delete("/cars/1"))
                .andExpect(status().isNoContent());
    }
}
