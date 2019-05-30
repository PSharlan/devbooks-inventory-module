package com.itechart.devbooks;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Map;

import static org.hamcrest.core.StringContains.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestPropertySource(locations="classpath:test.properties")
@AutoConfigureMockMvc
@Sql(value = {"/create.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/drop.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class InventoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void getAllOrders() throws Exception {
        this.mockMvc.perform(get("/api/v1/inventory/orders"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("207")))
                .andExpect(content().string(containsString("1")))
                .andExpect(content().string(containsString("333.33")))
                .andExpect(content().string(containsString("999.99")))
                .andExpect(content().string(containsString("PENDING")))
                .andExpect(content().string(containsString("BILLED")));
    }

    @Test
    public void getAllOrdersByCustomerId() throws Exception {
        this.mockMvc.perform(get("/api/v1/inventory/customers/207/orders"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("207")))
                .andExpect(content().string(containsString("1")))
                .andExpect(content().string(containsString("333.33")))
                .andExpect(content().string(containsString("666.66")))
                .andExpect(content().string(containsString("PENDING")))
                .andExpect(content().string(containsString("BILLED")));
    }

    @Test
    public void getAllOrdersByNotExistingCustomerId() throws Exception {
        this.mockMvc.perform(get("/api/v1/inventory/customer/0/orders"))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void getOrderById() throws Exception {
        this.mockMvc.perform(get("/api/v1/inventory/orders/2"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("1")))
                .andExpect(content().string(containsString("3")))
                .andExpect(content().string(containsString("555.55")))
                .andExpect(content().string(containsString("DELIVERY")))
                .andExpect(content().string(containsString("PAID")));
    }

    @Test
    public void getOrderByNotExistingId() throws Exception {
        this.mockMvc.perform(get("/api/v1/inventory/orders/0"))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }


    @Test
    public void getCustomerOrdersByCategory() throws Exception {
        this.mockMvc.perform(get("/api/v1/inventory/customers/207/orders/categories/cat3"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("207")))
                .andExpect(content().string(containsString("333.33")))
                .andExpect(content().string(containsString("666.66")))
                .andExpect(content().string(containsString("PAID")))
                .andExpect(content().string(containsString("DELIVERY")))
                .andExpect(content().string(containsString("CANCELED")));
    }

    @Test
    public void getOrdersByCategory() throws Exception {
        this.mockMvc.perform(get("/api/v1/inventory/orders/categories/cat3"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("207")))
                .andExpect(content().string(containsString("999.99")))
                .andExpect(content().string(containsString("333.33")))
                .andExpect(content().string(containsString("666.66")))
                .andExpect(content().string(containsString("PENDING")))
                .andExpect(content().string(containsString("BILLED")))
                .andExpect(content().string(containsString("CANCELED")));
    }

    @Test
    public void createNewOrderAndDelete() throws Exception {
        MvcResult result = this.mockMvc.perform(post("/api/v1/inventory/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"customerId\":207,\"itemsCount\":3," +
                        "\"priceTotal\":555.55," +
                        "\"creation\":\"2019-02-26T16:13:10.172+0000\",\"closing\":null," +
                        "\"items\":[" +
                        "{\"offerId\":1,\"name\":\"TestOffer1\",\"description\":\"TestDescription1\",\"category\":\"cat1\",\"price\":111.11}," +
                        "{\"offerId\":2,\"name\":\"TestOffer2\",\"description\":\"TestDescription2\",\"category\":\"cat2\",\"price\":222.22}," +
                        "{\"offerId\":2,\"name\":\"TestOffer2\",\"description\":\"TestDescription2\",\"category\":\"cat2\",\"price\":222.22}]}"))
                .andReturn();

        String foundStr = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        Map<String,Object> map = mapper.readValue(foundStr, Map.class);
        int id = (Integer)map.get("id");

        this.mockMvc.perform(get("/api/v1/inventory/orders/" + id))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("207")))
                .andExpect(content().string(containsString("3")))
                .andExpect(content().string(containsString("555.55")))
                .andExpect(content().string(containsString("PENDING")))
                .andExpect(content().string(containsString("BILLED")))
                .andExpect(content().string(containsString("TestOffer1")))
                .andExpect(content().string(containsString("TestOffer2")));

        this.mockMvc.perform(delete("/api/v1/inventory/orders/" + id))
                .andDo(print())
                .andExpect(status().isOk());

        this.mockMvc.perform(get("/api/v1/inventory/orders/" + id))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }
}
