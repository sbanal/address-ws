package com.github.slbb.ws.address.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Random;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class SuburbRepositoryTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void findById() throws Exception {
        this.mockMvc.perform(get("/address/suburbs/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", equalTo("Truganina")))
                .andExpect(jsonPath("$.postCode", equalTo(3029)));
    }

    @Test
    void findByNameWithUpperCase() throws Exception {
        this.mockMvc.perform(get("/address/suburbs/search/name?name={name}", "TRUG"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.suburbs", hasSize(1)))
                .andExpect(jsonPath("$._embedded.suburbs[0].name", equalTo("Truganina")))
                .andExpect(jsonPath("$._embedded.suburbs[0].postCode", equalTo(3029)));
    }

    @Test
    void findByNameWithLowerCase() throws Exception {
        this.mockMvc.perform(get("/address/suburbs/search/name?name={name}", "trug"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.suburbs", hasSize(1)))
                .andExpect(jsonPath("$._embedded.suburbs[0].name", equalTo("Truganina")))
                .andExpect(jsonPath("$._embedded.suburbs[0].postCode", equalTo(3029)));
    }

    @Test
    void findByNameWithMixedCase() throws Exception {
        this.mockMvc.perform(get("/address/suburbs/search/name?name={name}", "TrUG"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.suburbs", hasSize(1)))
                .andExpect(jsonPath("$._embedded.suburbs[0].name", equalTo("Truganina")))
                .andExpect(jsonPath("$._embedded.suburbs[0].postCode", equalTo(3029)));
    }

    @Test
    void findByNameLengthLessThanThree() throws Exception {
        this.mockMvc.perform(get("/address/suburbs/search/name?name={name}", "Tr"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void findByNameNoParameters() throws Exception {
        this.mockMvc.perform(get("/address/suburbs/search/name"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void findByPostCode() throws Exception {
        this.mockMvc.perform(get("/address/suburbs/search/postCode?code={code}", "3029"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.suburbs", hasSize(3)))
                .andExpect(jsonPath("$._embedded.suburbs[?(@.name == 'Truganina')]", hasSize(1)))
                .andExpect(jsonPath("$._embedded.suburbs[?(@.name == 'Truganina')].postCode", contains(3029)))
                .andExpect(jsonPath("$._embedded.suburbs[?(@.name == 'Tarneit')]", hasSize(1)))
                .andExpect(jsonPath("$._embedded.suburbs[?(@.name == 'Tarneit')].postCode", contains(3029)))
                .andExpect(jsonPath("$._embedded.suburbs[?(@.name == 'Hoppers Crossing')]", hasSize(1)))
                .andExpect(jsonPath("$._embedded.suburbs[?(@.name == 'Hoppers Crossing')].postCode", contains(3029)));
    }

    @Test
    void findByPostCodeWithNegativeValue() throws Exception {
        this.mockMvc.perform(get("/address/suburbs/search/postCode?code={code}", "-1"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void findByPostCodeWithMinValue() throws Exception {
        this.mockMvc.perform(get("/address/suburbs/search/postCode?code={code}", "1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.page.totalElements", equalTo(0)));
    }

    @Test
    void findByPostCodeWithMaxValue() throws Exception {
        this.mockMvc.perform(get("/address/suburbs/search/postCode?code={code}", String.valueOf(Integer.MAX_VALUE)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.page.totalElements", equalTo(0)));
    }

    @WithMockUser(username="admin",roles={"ADMIN"})
    @Test
    void deleteById() throws Exception {
        this.mockMvc.perform(delete("/address/suburbs/2"))
                .andDo(print())
                .andExpect(status().isNoContent());
        this.mockMvc.perform(get("/address/suburbs/2"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @WithMockUser(username="admin",roles={"ADMIN"})
    @Test
    void putById() throws Exception {
        Suburb suburb = new Suburb();
        suburb.setId(3);
        suburb.setName("SomeNewSuburb");
        suburb.setPostCode(1000);
        this.mockMvc.perform(put("/address/suburbs/3")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(suburb)))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @WithMockUser(username="admin",roles={"ADMIN"})
    @Test
    void createSuburb() throws Exception {
        final String suburbName = "SomeNewSuburb" + new Random().nextInt(10000);
        Suburb suburb = new Suburb();
        suburb.setName(suburbName);
        suburb.setPostCode(1001);
        MvcResult result = this.mockMvc.perform(post("/address/suburbs")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(suburb)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location")).andReturn();
        String resourceLocation = result.getResponse().getHeader("Location");
        this.mockMvc.perform(get(resourceLocation))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", equalTo(suburbName)))
                .andExpect(jsonPath("$.postCode", equalTo(1001)));
    }

}