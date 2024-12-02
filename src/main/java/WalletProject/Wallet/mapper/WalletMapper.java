package WalletProject.Wallet.mapper;

import WalletProject.Wallet.dto.ResponseWalletDto;
import WalletProject.Wallet.model.Wallet;
import org.springframework.stereotype.Component;

@Component
public class WalletMapper {
    public ResponseWalletDto toResponseDto(Wallet wallet) {
        return new ResponseWalletDto(wallet.getWalletId(), wallet.getBalance());
    }
}
