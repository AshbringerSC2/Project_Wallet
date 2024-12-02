package WalletProject.Wallet.service;

import WalletProject.Wallet.dto.CreateWalletDto;
import WalletProject.Wallet.dto.RequestWalletDto;
import WalletProject.Wallet.dto.ResponseWalletDto;
import java.math.BigDecimal;
import java.util.UUID;

public interface WalletService {
    ResponseWalletDto processOperation(RequestWalletDto requestWalletDto);

    BigDecimal getBalance(UUID walletId);

    ResponseWalletDto createWallet(CreateWalletDto createWalletDto);
}
