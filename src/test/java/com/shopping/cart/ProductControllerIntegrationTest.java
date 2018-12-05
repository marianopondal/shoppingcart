package com.shopping.cart;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopping.cart.model.Product;
import com.shopping.cart.service.ProductService;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@AutoConfigureMockMvc
//@SqlGroup({
        //@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:data.sql"),
        //@Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:afterTestRun.sql")
//})
public class ProductControllerIntegrationTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductService productService;
/*
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Before
    public void setUp() {
        jdbcTemplate.execute("insert into Product (id, name, price, stock)\n" +
                "values(1,'GoPro Hero 5 Black', 279.95, 10)");
        jdbcTemplate.execute("insert into Product (id, name, price, stock)\n" +
                "values(2,'Samsung Galaxy Tab 8', 129.99, 20)");
        jdbcTemplate.execute("insert into Product (id, name, price, stock)\n" +
                "values(3,'Apple MacBook Pro 13.3', 349.95, 5)");
    }*/

    @Test
    public void findById_returnsPruduct() {
        ResponseEntity<Product> response = this.testRestTemplate.getForEntity("/products/{id}", Product.class, "1");
        assertThat(response.getBody().getName(), equalTo("GoPro Hero 5 Black"));
    }

    @Test
    public void findAll_returnsPruducts() throws Exception {
        List<Product> productList = this.productService.findAll();

        this.mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(productList.size())));
    }

    @Test
    public void create() throws Exception {
        Product product = new Product("Test Product", 550.2, 50);

        this.mockMvc.perform(post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(product)))
                .andExpect(status().isCreated());
    }

    @Test
    public void update() throws Exception {
        Product product = new Product("Test Product2", 550.2, 50);
        product.setId(new Long(2));

        this.mockMvc.perform(put("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(product)))
                .andExpect(status().isOk());
    }
}
