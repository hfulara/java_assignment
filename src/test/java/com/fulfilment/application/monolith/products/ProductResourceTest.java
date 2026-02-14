package com.fulfilment.application.monolith.products;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.panache.common.Sort;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class ProductResourceTest {

    @Mock
    ProductRepository productRepository;

    @InjectMocks
    ProductResource resource;

    Product product;

    @BeforeEach
    void setup() {
        product = new Product();
        product.id = 4L;
        product.name = "COOKIE";
        product.stock = 50;
    }

    @Test
    void shouldReturnSingleProduct() {
        when(productRepository.findById(1L)).thenReturn(product);

        Product result = resource.getSingle(1L);

        assertEquals("COOKIE", result.name);
    }

    @Test
    void shouldThrow404WhenProductNotFound() {
        when(productRepository.findById(99L)).thenReturn(null);

        WebApplicationException ex =
                assertThrows(WebApplicationException.class,
                        () -> resource.getSingle(99L));

        assertEquals(404, ex.getResponse().getStatus());
    }


    @Test
    void shouldCreateProductSuccessfully() {
        product.id = null;

        Response response = resource.create(product);

        verify(productRepository).persist(product);
        assertEquals(201, response.getStatus());
    }

    @Test
    void shouldUpdateProductSuccessfully() {

        Product updated = new Product();
        updated.name = "Updated Cookie";
        updated.description = "Updated Cookie Desc";
        updated.stock = 5;

        when(productRepository.findById(4L)).thenReturn(product);

        Product result = resource.update(4L, updated);

        verify(productRepository).persist(product);

        assertEquals("Updated Cookie", result.name);
        assertEquals(5, result.stock);
    }

    @Test
    void shouldDeleteProductSuccessfully() {

        when(productRepository.findById(1L)).thenReturn(product);

        Response response = resource.delete(1L);

        verify(productRepository).delete(product);
        assertEquals(204, response.getStatus());
    }

    @Test
    void shouldThrow404IfProductNotFoundOnDelete() {

        when(productRepository.findById(99L)).thenReturn(null);

        WebApplicationException ex =
                assertThrows(WebApplicationException.class,
                        () -> resource.delete(99L));

        assertEquals(404, ex.getResponse().getStatus());
    }
}
