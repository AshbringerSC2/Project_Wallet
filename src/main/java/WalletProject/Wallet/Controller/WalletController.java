package WalletProject.Wallet.Controller;

import WalletProject.Wallet.dto.CreateWalletDto;
import WalletProject.Wallet.dto.RequestWalletDto;
import WalletProject.Wallet.dto.ResponseWalletDto;
import WalletProject.Wallet.service.WalletService;
import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/wallets")
@RequiredArgsConstructor
public class WalletController {
    private final WalletService walletService;

    @PostMapping("/create")
    public ResponseEntity<ResponseWalletDto> createWallet(@Valid @RequestBody CreateWalletDto createWalletDto) {
        ResponseWalletDto responseWalletDto = walletService.createWallet(createWalletDto);
        return ResponseEntity.ok(responseWalletDto);
    }
    @PutMapping
    public ResponseEntity<ResponseWalletDto> processOperation(@Valid @RequestBody RequestWalletDto requestWalletDto) {
        return ResponseEntity.ok(walletService.processOperation(requestWalletDto));
    }

    @GetMapping("/{walletId}")
    public ResponseEntity<BigDecimal> getBalance(@PathVariable UUID walletId) {
        return ResponseEntity.ok(walletService.getBalance(walletId));
    }
}
