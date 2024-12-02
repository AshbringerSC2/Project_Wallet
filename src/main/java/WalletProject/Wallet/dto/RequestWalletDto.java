package WalletProject.Wallet.dto;

import WalletProject.Wallet.enums.WalletOperation;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.UUID;

public record RequestWalletDto(
    @NotNull UUID walletId,
    @NotNull WalletOperation OperationType,
    @Positive BigDecimal amount)
{}
