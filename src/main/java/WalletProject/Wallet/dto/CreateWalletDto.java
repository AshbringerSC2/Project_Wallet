package WalletProject.Wallet.dto;

import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public record CreateWalletDto(
        @Positive BigDecimal balance
) {}