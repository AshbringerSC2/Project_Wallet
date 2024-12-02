package WalletProject.Wallet.dto;



import java.math.BigDecimal;
import java.util.UUID;

public record ResponseWalletDto( UUID walletId,
                                 BigDecimal amount) {


}
