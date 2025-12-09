package com.banking.app;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest
@AutoConfigureMockMvc
public class SimpleBankingAppTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void withdrawInsufficientFunds() throws Exception {
        // Register and login
        mockMvc.perform(post("/api/register?username=testuser&password=pass"));
        // Create account
        String accNum = mockMvc.perform(post("/api/account?username=testuser"))
            .andReturn().getResponse().getContentAsString();
        // Deposit 1000
        mockMvc.perform(post("/api/deposit?accNum=" + accNum + "&amount=1000"));
        // Try to withdraw 5000
        mockMvc.perform(post("/api/withdraw?accNum=" + accNum + "&amount=5000"))
            .andExpect(content().string("Insufficient funds"));
    }
}
