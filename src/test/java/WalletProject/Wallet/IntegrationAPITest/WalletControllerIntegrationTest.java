package WalletProject.Wallet.IntegrationAPITest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import WalletProject.Wallet.dto.CreateWalletDto;
import WalletProject.Wallet.dto.RequestWalletDto;
import WalletProject.Wallet.dto.ResponseWalletDto;
import WalletProject.Wallet.enums.WalletOperation;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class WalletControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createWallet_ShouldCreateWalletSuccessfully() throws Exception {
        CreateWalletDto createWalletDto = new CreateWalletDto(BigDecimal.valueOf(100.00));

        mockMvc.perform(post("/api/v1/wallets/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createWalletDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.walletId").exists())
                .andExpect(jsonPath("$.amount").value(100.00));
    }

    @Test
    void processOperation_Deposit_ShouldUpdateWalletBalance() throws Exception {
        // Create wallet
        CreateWalletDto createWalletDto = new CreateWalletDto(BigDecimal.valueOf(100.00));
        String createResponse = mockMvc.perform(post("/api/v1/wallets/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createWalletDto)))
                .andReturn().getResponse().getContentAsString();

        UUID walletId = objectMapper.readValue(createResponse, ResponseWalletDto.class).walletId();

        RequestWalletDto depositDto = new RequestWalletDto(
                walletId,
                WalletOperation.DEPOSIT,
                BigDecimal.valueOf(50.00)
        );

        mockMvc.perform(put("/api/v1/wallets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(depositDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amount").value(150.00));
    }

    @Test
    void getBalance_ShouldReturnCorrectBalance() throws Exception {
        CreateWalletDto createWalletDto = new CreateWalletDto(BigDecimal.valueOf(100.00));

        String createResponse = mockMvc.perform(post("/api/v1/wallets/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createWalletDto)))
                .andReturn().getResponse().getContentAsString();

        UUID walletId = objectMapper.readValue(createResponse, ResponseWalletDto.class).walletId();

        mockMvc.perform(get("/api/v1/wallets/{walletId}", walletId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(100.00));
    }


}