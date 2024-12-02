package WalletProject.Wallet.Service;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import WalletProject.Wallet.dto.CreateWalletDto;
import WalletProject.Wallet.dto.RequestWalletDto;
import WalletProject.Wallet.dto.ResponseWalletDto;
import WalletProject.Wallet.enums.WalletOperation;
import WalletProject.Wallet.exception.Exception.DataException;
import WalletProject.Wallet.exception.Exception.WalletNotFoundException;
import WalletProject.Wallet.mapper.WalletMapper;
import WalletProject.Wallet.model.Wallet;
import WalletProject.Wallet.repository.WalletRepository;
import WalletProject.Wallet.service.impl.WalletServiceImpl;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class WalletServiceImplTest {

    @Mock
    private WalletRepository walletRepository;

    @Mock
    private WalletMapper walletMapper;

    @InjectMocks
    private WalletServiceImpl walletService;

    private UUID walletId;
    private Wallet wallet;
    private CreateWalletDto createWalletDto;
    private RequestWalletDto depositDto;
    private RequestWalletDto withdrawDto;
    private ResponseWalletDto responseWalletDto;

    @BeforeEach
    void setUp() {
        walletId = UUID.randomUUID();

        wallet = new Wallet();
        wallet.setWalletId(walletId);
        wallet.setBalance(BigDecimal.valueOf(100.00));

        createWalletDto = new CreateWalletDto(BigDecimal.valueOf(100.00));

        depositDto = new RequestWalletDto(
                walletId,
                WalletOperation.DEPOSIT,
                BigDecimal.valueOf(50.00)
        );

        withdrawDto = new RequestWalletDto(
                walletId,
                WalletOperation.WITHDRAW,
                BigDecimal.valueOf(50.00)
        );

        responseWalletDto = new ResponseWalletDto(walletId, BigDecimal.valueOf(100.00));
    }

    @Test
    void createWallet_WithBalance_ShouldCreateWallet() {
        when(walletRepository.save(any(Wallet.class))).thenReturn(wallet);
        when(walletMapper.toResponseDto(wallet)).thenReturn(responseWalletDto);

        ResponseWalletDto result = walletService.createWallet(createWalletDto);

        assertNotNull(result);
        assertEquals(walletId, result.walletId());
        assertEquals(BigDecimal.valueOf(100.00), result.amount());
        verify(walletRepository).save(any(Wallet.class));
    }

    @Test
    void createWallet_WithoutBalance_ShouldCreateWalletWithZeroBalance() {
        CreateWalletDto emptyBalanceDto = new CreateWalletDto(null);
        Wallet expectedWallet = new Wallet();
        expectedWallet.setBalance(BigDecimal.ZERO);

        when(walletRepository.save(any(Wallet.class))).thenReturn(expectedWallet);
        when(walletMapper.toResponseDto(expectedWallet)).thenReturn(
                new ResponseWalletDto(expectedWallet.getWalletId(), BigDecimal.ZERO)
        );

        ResponseWalletDto result = walletService.createWallet(emptyBalanceDto);

        assertNotNull(result);
        assertEquals(BigDecimal.ZERO, result.amount());
        verify(walletRepository).save(any(Wallet.class));
    }

    @Test
    void processOperation_Deposit_ShouldIncreaseBalance() {
        when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));
        when(walletRepository.save(wallet)).thenReturn(wallet);
        when(walletMapper.toResponseDto(wallet)).thenReturn(
                new ResponseWalletDto(walletId, BigDecimal.valueOf(150.00))
        );

        ResponseWalletDto result = walletService.processOperation(depositDto);

        assertNotNull(result);
        assertEquals(BigDecimal.valueOf(150.00), result.amount());
        verify(walletRepository).save(wallet);
    }

    @Test
    void processOperation_Withdraw_ShouldDecreaseBalance() {
        when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));
        when(walletRepository.save(wallet)).thenReturn(wallet);
        when(walletMapper.toResponseDto(wallet)).thenReturn(
                new ResponseWalletDto(walletId, BigDecimal.valueOf(50.00))
        );

        ResponseWalletDto result = walletService.processOperation(withdrawDto);

        assertNotNull(result);
        assertEquals(BigDecimal.valueOf(50.00), result.amount());
        verify(walletRepository).save(wallet);
    }

    @Test
    void processOperation_Withdraw_InsufficientFunds_ShouldThrowException() {
        RequestWalletDto overdraftDto = new RequestWalletDto(
                walletId,
                WalletOperation.WITHDRAW,
                BigDecimal.valueOf(200.00)
        );

        when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));

        DataException exception = assertThrows(DataException.class, () -> {
            walletService.processOperation(overdraftDto);
        });

        assertTrue(exception.getMessage().contains("Insufficient funds"));
        verify(walletRepository, never()).save(any(Wallet.class));
    }

    @Test
    void processOperation_Deposit_NegativeAmount_ShouldThrowException() {
        RequestWalletDto negativeDepositDto = new RequestWalletDto(
                walletId,
                WalletOperation.DEPOSIT,
                BigDecimal.valueOf(-50.00)
        );

        when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));

        DataException exception = assertThrows(DataException.class, () -> {
            walletService.processOperation(negativeDepositDto);
        });

        assertTrue(exception.getMessage().contains("Deposit amount cannot be negative"));
        verify(walletRepository, never()).save(any(Wallet.class));
    }

    @Test
    void getBalance_ExistingWallet_ShouldReturnBalance() {
        when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));

        BigDecimal result = walletService.getBalance(walletId);

        assertEquals(BigDecimal.valueOf(100.00), result);
        verify(walletRepository).findById(walletId);
    }

    @Test
    void getBalance_NonExistingWallet_ShouldThrowException() {
        when(walletRepository.findById(walletId)).thenReturn(Optional.empty());
        WalletNotFoundException exception = assertThrows(WalletNotFoundException.class, () -> {
            walletService.getBalance(walletId);
        });

        assertTrue(exception.getMessage().contains("Wallet not found"));
        verify(walletRepository).findById(walletId);
    }
}