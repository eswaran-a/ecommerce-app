package com.ecommerce;

import com.ecommerce.entity.User;
import com.ecommerce.request.CreateUserRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class ECommerceApplicationTests {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void contextLoads() {
    }

    @Test
    public void createUser_and_successful_login() throws URISyntaxException {
        CreateUserRequest userRequest = new CreateUserRequest("user1", "password1", "password1");
        HttpEntity<CreateUserRequest> request = new HttpEntity<>(userRequest);

        ResponseEntity<User> createResponse =
                this.restTemplate.postForEntity(new URI("http://localhost:" + port + "/api/user/create"), request, User.class);
        assertThat(createResponse.getStatusCode(), equalTo(HttpStatus.OK));

        ResponseEntity<?> userResponse = this.restTemplate.withBasicAuth("user1", "password1")
                .postForEntity(new URI("http://localhost:" + port + "/login"),"{" +
                        "    \"username\": \"user1\"," +
                        "    \"password\": \"password1\"}", Object.class);

        assertThat(userResponse.getStatusCode(), equalTo(HttpStatus.OK));
    }

    @Test
    public void unsuccessful_login() throws URISyntaxException {
        ResponseEntity<?> userResponse = this.restTemplate.withBasicAuth("user1", "password1")
                .postForEntity(new URI("http://localhost:" + port + "/login"),"{" +
                        "    \"username\": \"user11\"," +
                        "    \"password\": \"password11\"}", Object.class);

        assertThat(userResponse.getStatusCode(), equalTo(HttpStatus.FORBIDDEN));
    }
}
