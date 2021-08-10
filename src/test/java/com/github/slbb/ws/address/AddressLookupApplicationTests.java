package com.github.slbb.ws.address;

import com.github.slbb.ws.address.repository.SuburbRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class AddressLookupApplicationTests {

    @Autowired
    private SuburbRepository suburbRepository;

    @Test
    void contextLoads() {
        assertThat(suburbRepository).isNotNull();
    }

}
