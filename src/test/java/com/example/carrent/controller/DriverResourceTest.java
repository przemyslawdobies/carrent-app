package com.example.carrent.controller;

import com.example.carrent.ExceptionTranslator;
import com.example.carrent.TestUtil;
import com.example.carrent.model.Driver;
import com.example.carrent.repository.DriverRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.Validator;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import javax.persistence.EntityManager;
import java.util.List;

import static com.example.carrent.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class DriverResourceTest {

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_MILEAGE = 1;
    private static final Integer UPDATED_MILEAGE = 2;

    private static final Integer DEFAULT_AGE = 1;
    private static final Integer UPDATED_AGE = 2;


    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restDriverMockMvc;

    private Driver driver;

    public static Driver createEntity(EntityManager em) {
        Driver driver = new Driver()
                .firstName(DEFAULT_FIRST_NAME)
                .lastName(DEFAULT_LAST_NAME)
                .mileage(DEFAULT_MILEAGE)
                .age(DEFAULT_AGE);
        return driver;


    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DriverResource driverResource = new DriverResource(driverRepository);
        this.restDriverMockMvc = MockMvcBuilders.standaloneSetup(driverResource)
                .setCustomArgumentResolvers(pageableArgumentResolver)
                .setControllerAdvice(exceptionTranslator)
                .setConversionService(createFormattingConversionService())
                .setMessageConverters(jacksonMessageConverter)
                .setValidator(validator).build();
    }



    @Test
    void createDriver throws Exception {
            int databaseSizeBeforeCreate = driverRepository.findAll().size();

            // Create the Driver
            restDriverMockMvc.perform(post("/api/drivers")
                    .contentType(TestUtil.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(driver)))
                    .andExpect(status().isCreated());

            // Validate the Driver in the database
            List<Driver> driverList = driverRepository.findAll();
            assertThat(driverList).hasSize(databaseSizeBeforeCreate + 1);
            Driver testDriver = driverList.get(driverList.size() - 1);
            assertThat(testDriver.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
            assertThat(testDriver.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
            assertThat(testDriver.getMileage()).isEqualTo(DEFAULT_MILEAGE);
            assertThat(testDriver.getAge()).isEqualTo(DEFAULT_AGE);
        }




    @Test
    void updateDriver() {
    }

    @Test
    void getAllDrivers() {
    }

    @Test
    void getDriver() {
    }

    @Test
    void deleteDriver() {
    }
}