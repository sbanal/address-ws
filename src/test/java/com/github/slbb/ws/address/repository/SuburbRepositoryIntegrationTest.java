package com.github.slbb.ws.address.repository;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.jayway.jsonpath.JsonPath;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

@Getter
@Setter
class SearchEmbeddedResult {
    private List<Suburb> suburbs;
}

@Getter
@Setter
class SearchResult {
    @JsonProperty("_embedded")
    private SearchEmbeddedResult embedded;
    private Page page;
}


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class SuburbRepositoryIntegrationTest {

    @LocalServerPort
    private int serverPort;
    TestRestTemplate adminRestTemplate = new TestRestTemplate("apiuser", "test");

    @Test
    void getSuburbById(@Autowired TestRestTemplate restTemplate) {
        ResponseEntity<Suburb> response = restTemplate.getForEntity("/address/suburbs/1", Suburb.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getName()).isEqualTo("Truganina");
        assertThat(response.getBody().getPostCode()).isEqualTo(3029);
    }

    @Test
    void searchSuburbByName(@Autowired TestRestTemplate restTemplate) throws JsonProcessingException {
        ResponseEntity<String> response = restTemplate.getForEntity("/address/suburbs/search/name?name=Tru", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        int totalItems = JsonPath.read(response.getBody(), "$.page.totalElements");
        List<String> suburbNames = JsonPath.read(response.getBody(), "$._embedded.suburbs[*].name");
        assertThat(totalItems).isEqualTo(1);
        assertThat(suburbNames).hasSize(1);
        assertThat(suburbNames).contains("Truganina");
    }

    @Test
    void searchSuburbByPostCode(@Autowired TestRestTemplate restTemplate) throws JsonProcessingException {
        ResponseEntity<String> response = restTemplate.getForEntity("/address/suburbs/search/postCode?code=3029", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        System.out.println("response:" + response.getBody());
        int totalItems = JsonPath.read(response.getBody(), "$.page.totalElements");
        List<String> suburbNames = JsonPath.read(response.getBody(), "$._embedded.suburbs[*].name");
        assertThat(totalItems).isEqualTo(3);
        assertThat(suburbNames).hasSize(3);
        assertThat(suburbNames).contains("Truganina");
        assertThat(suburbNames).contains("Tarneit");
        assertThat(suburbNames).contains("Hoppers Crossing");
    }

    @Test
    void createSuburb() throws JsonProcessingException {
        String suburbName = "some new name " + new Random().nextInt(10000);
        Suburb suburb = new Suburb();
        suburb.setName(suburbName);
        suburb.setPostCode(2020);

        String url = "http://localhost:"+ serverPort + "/address/suburbs";
        ResponseEntity<String> response = adminRestTemplate.postForEntity(url, suburb, String.class, Arrays.asList());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getHeaders().getLocation()).isNotNull();

        ResponseEntity<Suburb> suburbResponse = adminRestTemplate.getForEntity(response.getHeaders().getLocation(), Suburb.class);
        assertThat(suburbResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(suburbResponse.getBody().getName()).isEqualTo(suburbName);
        assertThat(suburbResponse.getBody().getPostCode()).isEqualTo(2020);
    }

    @Test
    void updateSuburb() throws JsonProcessingException {
        Suburb suburb = new Suburb();
        suburb.setName("SomeNewNameHere!");
        suburb.setPostCode(1010);

        String url = "http://localhost:"+ serverPort + "/address/suburbs/4";
        adminRestTemplate.put(url, suburb, Arrays.asList());

        ResponseEntity<Suburb> suburbResponse = adminRestTemplate.getForEntity(url, Suburb.class);
        assertThat(suburbResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(suburbResponse.getBody().getName()).isEqualTo("SomeNewNameHere!");
        assertThat(suburbResponse.getBody().getPostCode()).isEqualTo(1010);
    }

}
