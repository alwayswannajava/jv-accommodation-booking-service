package com.spring.booking.accommodationbookingservice.controller;

import static com.spring.booking.accommodationbookingservice.util.Constants.ACCOMMODATION_URL;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.booking.accommodationbookingservice.dto.accommodation.AccommodationResponse;
import com.spring.booking.accommodationbookingservice.util.TestUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.internal.matchers.apachecommons.ReflectionEquals;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AccommodationControllerTest {
    protected MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp(
            @Autowired DataSource dataSource,
            @Autowired WebApplicationContext applicationContext) throws SQLException {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("db/scripts/add-accommodation-test-data.sql")
            );
        }

    }

    @AfterEach
    void tearDown(@Autowired DataSource dataSource) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("db/scripts/remove-accommodation-test-data.sql")
            );
        }
    }

    @Test
    @DisplayName("Test create() method controller")
    @WithMockUser(username = "admin", roles = "ADMIN")
    void create_ValidCreateAccommodationRequestDto_ReturnAccommodationResponse()
            throws Exception {
        String requestJson = objectMapper.writeValueAsString(TestUtil.createAccommodationRequestDto());

        MvcResult mvcResult = mockMvc.perform(
                post(ACCOMMODATION_URL)
                        .content(requestJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        AccommodationResponse expected = TestUtil.createAccommodationResponse();
        AccommodationResponse actual = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
                AccommodationResponse.class);

        EqualsBuilder.reflectionEquals(expected, actual, "id", "type", "address",
                "size", "amenities", "dailyRate", "availability");
    }
}