package ph.dev.dscommerce.controllers;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ph.dev.dscommerce.dto.ProductDTO;
import ph.dev.dscommerce.services.ProductService;

import java.util.stream.Stream;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasItems;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
class ProductControllerValidationTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService productService;

    @ParameterizedTest(name = "{0}: {3} - {4}")
    @MethodSource("invalidProducts")
    void writeShouldReturn422WhenProductIsInvalid(String method, String name, String description,
                                                  String priceProperty, String field, String message) throws Exception {
        String path = method.equals("POST") ? "/products" : "/products/1";
        String body = """
                {"name": %s, "description": %s%s}
                """.formatted(name, description, priceProperty);

        mockMvc.perform(request(HttpMethod.valueOf(method), path)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().is(422))
                .andExpect(jsonPath("$.status").value(422))
                .andExpect(jsonPath("$.message").value("Error in data validation"))
                .andExpect(jsonPath("$.path").value(path))
                .andExpect(jsonPath("$.errors[?(@.fieldName == '%s')].message".formatted(field))
                        .value(contains(message)));

        verifyNoInteractions(productService);
    }

    static Stream<Arguments> invalidProducts() {
        return Stream.of("POST", "PUT").flatMap(method -> Stream.of(
                Arguments.of(method, "\"Keyboard\"", "\"Mechanical keyboard\"", "",
                        "price", "Price is required"),
                Arguments.of(method, "\"Keyboard\"", "\"Mechanical keyboard\"", ", \"price\": null",
                        "price", "Price is required"),
                Arguments.of(method, "\"Keyboard\"", "\"Mechanical keyboard\"", ", \"price\": 0",
                        "price", "Price must be positive"),
                Arguments.of(method, "\"Keyboard\"", "\"Mechanical keyboard\"", ", \"price\": -1",
                        "price", "Price must be positive"),
                Arguments.of(method, "null", "\"Mechanical keyboard\"", ", \"price\": 10",
                        "name", "Name is required"),
                Arguments.of(method, "\"   \"", "\"Mechanical keyboard\"", ", \"price\": 10",
                        "name", "Name is required"),
                Arguments.of(method, "\"AB\"", "\"Mechanical keyboard\"", ", \"price\": 10",
                        "name", "Name must be between 3 and 80 characters long"),
                Arguments.of(method, "\"" + "A".repeat(81) + "\"", "\"Mechanical keyboard\"", ", \"price\": 10",
                        "name", "Name must be between 3 and 80 characters long"),
                Arguments.of(method, "\"Keyboard\"", "null", ", \"price\": 10",
                        "description", "Description is required"),
                Arguments.of(method, "\"Keyboard\"", "\"          \"", ", \"price\": 10",
                        "description", "Description is required"),
                Arguments.of(method, "\"Keyboard\"", "\"123456789\"", ", \"price\": 10",
                        "description", "The description must be at least 10 characters long")
        ));
    }

    @ParameterizedTest
    @ValueSource(strings = {"POST", "PUT"})
    void writeShouldReturnAllInvalidFields(String method) throws Exception {
        String path = method.equals("POST") ? "/products" : "/products/1";

        mockMvc.perform(request(HttpMethod.valueOf(method), path)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name": "", "description": "", "price": 0}
                                """))
                .andExpect(status().is(422))
                .andExpect(jsonPath("$.errors[*].fieldName").value(hasItems("name", "description", "price")));

        verifyNoInteractions(productService);
    }

    @ParameterizedTest
    @ValueSource(strings = {"ABC", "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"})
    void writeShouldAcceptValidProductAtLengthLimits(String name) throws Exception {
        ProductDTO product = new ProductDTO(1L, name, "1234567890", 0.01, null);
        when(productService.insert(any(ProductDTO.class))).thenReturn(product);
        when(productService.update(eq(1L), any(ProductDTO.class))).thenReturn(product);
        String body = """
                {"name": "%s", "description": "1234567890", "price": 0.01}
                """.formatted(name);

        mockMvc.perform(request(HttpMethod.POST, "/products")
                        .contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/products/1"))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.price").value(0.01));

        mockMvc.perform(request(HttpMethod.PUT, "/products/1")
                        .contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.price").value(0.01));
    }
}
