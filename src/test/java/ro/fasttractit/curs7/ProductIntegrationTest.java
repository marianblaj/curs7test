package ro.fasttractit.curs7;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.ReplaceOperation;
import com.github.fge.jsonpatch.diff.JsonDiff;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ro.fasttractit.curs7.entity.Product;

import java.util.List;
import java.util.Objects;

import static org.springframework.http.HttpMethod.GET;

public class ProductIntegrationTest {

    private final String BASE_URL = "http://localhost:8080/products";

    @Test
    void testGetAll() {
        Objects.requireNonNull(restTemplate().exchange(
                BASE_URL,
                GET,
                new HttpEntity<>(null),
                new ParameterizedTypeReference<List<Product>>() {
                }
        ).getBody())
                .forEach(System.out::println);

    }

    @Test
    void testTest() {
        List<Product> result =
                restTemplate().exchange(
                        BASE_URL + "/test",
                        GET,
                        new HttpEntity<>(null),
                        new ParameterizedTypeReference<List<Product>>() {
                        }
                ).getBody();
        System.out.println(result);
    }

    @Test
    void testPostNew() {
        var result = restTemplate().postForObject(
                BASE_URL,
                new Product("produs nou nout3"),
                Product.class);
        System.out.println(result);

    }

    @Test
    @SneakyThrows
    void testPut() {
        String url = UriComponentsBuilder.fromHttpUrl(BASE_URL)
                .path("/2")
                .toUriString();
        restTemplate().put(
                url,
                new Product("nume nou din put1"));
    }

    @Test
    @SneakyThrows
    void testPatch() {
        String url = UriComponentsBuilder.fromHttpUrl(BASE_URL)
                .path("/2")
                .toUriString();
        ObjectMapper mapper = new ObjectMapper();
        JsonPatch patch = JsonPatch.fromJson(mapper.readTree(
                """
                              [
                                { "op": "replace", "path": "/description", "value": "boo" }                            
                              ]
                        """
        ));
        Product product = restTemplate().patchForObject(
                url,
                patch,
                Product.class);

        Product product2 = new Product("numeDif", product.getDescription());
        JsonNode jsonNode = JsonDiff.asJson(mapper.valueToTree(product), mapper.valueToTree(product2));
        System.out.println(jsonNode);
    }


    private RestTemplate restTemplate() {
        return new RestTemplate(new HttpComponentsClientHttpRequestFactory());
    }
}
