package WalletProject.Wallet.WalletControllerTest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import WalletProject.Wallet.Controller.WalletController;
import WalletProject.Wallet.dto.CreateWalletDto;
import WalletProject.Wallet.dto.RequestWalletDto;
import WalletProject.Wallet.dto.ResponseWalletDto;
import WalletProject.Wallet.enums.WalletOperation;
import WalletProject.Wallet.service.WalletService;
import java.math.BigDecimal;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
public class WalletControllerTest {

  @Mock
  private WalletService walletService;

  @InjectMocks
  private WalletController walletController;

  private CreateWalletDto createWalletDto;
  private ResponseWalletDto responseWalletDto;
  private RequestWalletDto requestWalletDto;
  private UUID walletId;

  @BeforeEach
  void setUp() {
    walletId = UUID.randomUUID();

    createWalletDto = new CreateWalletDto(BigDecimal.valueOf(100.00));

    responseWalletDto = new ResponseWalletDto(walletId, BigDecimal.valueOf(100.00));

    requestWalletDto = new RequestWalletDto(
            walletId,
            WalletOperation.DEPOSIT,
            BigDecimal.valueOf(50.00)
    );
  }

  @Test
  void createWallet_ShouldReturnResponseWalletDto() {
    when(walletService.createWallet(createWalletDto)).thenReturn(responseWalletDto);

    ResponseEntity<ResponseWalletDto> response = walletController.createWallet(createWalletDto);

    assertEquals(ResponseEntity.ok(responseWalletDto), response);
    verify(walletService).createWallet(createWalletDto);
  }

  @Test
  void processOperation_Deposit_ShouldReturnResponseWalletDto() {
    when(walletService.processOperation(requestWalletDto)).thenReturn(responseWalletDto);

    ResponseEntity<ResponseWalletDto> response = walletController.processOperation(requestWalletDto);

    assertEquals(ResponseEntity.ok(responseWalletDto), response);
    verify(walletService).processOperation(requestWalletDto);
  }

  @Test
  void getBalance_ShouldReturnBalance() {
    BigDecimal expectedBalance = BigDecimal.valueOf(100.00);
    when(walletService.getBalance(walletId)).thenReturn(expectedBalance);

    ResponseEntity<BigDecimal> response = walletController.getBalance(walletId);

    assertEquals(ResponseEntity.ok(expectedBalance), response);
    verify(walletService).getBalance(walletId);
  }
}