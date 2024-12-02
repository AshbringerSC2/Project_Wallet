package WalletProject.Wallet.service.impl;

import WalletProject.Wallet.dto.CreateWalletDto;
import WalletProject.Wallet.dto.RequestWalletDto;
import WalletProject.Wallet.dto.ResponseWalletDto;
import WalletProject.Wallet.exception.Exception.DataException;
import WalletProject.Wallet.exception.Exception.WalletNotFoundException;
import WalletProject.Wallet.mapper.WalletMapper;
import WalletProject.Wallet.model.Wallet;
import WalletProject.Wallet.repository.WalletRepository;
import WalletProject.Wallet.service.WalletService;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class WalletServiceImpl implements WalletService {
    private final WalletRepository walletRepository;
    private final WalletMapper walletMapper;

    @Transactional
    @Override
    public ResponseWalletDto createWallet( CreateWalletDto createWalletDto) {
        Wallet wallet = new Wallet();
        wallet.setBalance(createWalletDto.balance() != null ? createWalletDto.balance() : BigDecimal.ZERO);
        Wallet savedWallet = walletRepository.save(wallet);
        return walletMapper.toResponseDto(savedWallet);
    }
    @Transactional
    @Override
    public ResponseWalletDto processOperation(RequestWalletDto requestWalletDto) {
        Wallet wallet = walletRepository.findByIdWithLock(requestWalletDto.walletId())
                .orElseThrow(() -> new WalletNotFoundException("Wallet not found with ID: " + requestWalletDto.walletId()));

        switch (requestWalletDto.OperationType()) {
            case DEPOSIT:
                if (requestWalletDto.amount().compareTo(BigDecimal.ZERO) < 0) {
                    throw new DataException("Deposit amount cannot be negative.");
                }
                wallet.setBalance(wallet.getBalance().add(requestWalletDto.amount()));
                break;

            case WITHDRAW:
                if (wallet.getBalance().compareTo(requestWalletDto.amount()) < 0) {
                    throw new DataException("Insufficient funds for withdrawal. Current balance: " + wallet.getBalance());
                }
                wallet.setBalance(wallet.getBalance().subtract(requestWalletDto.amount()));
                break;

            default:
                throw new DataException("Invalid operation type: " + requestWalletDto.OperationType());
        }

        Wallet updatedWallet = walletRepository.save(wallet);
        return walletMapper.toResponseDto(updatedWallet);
    }


    @Override
    public BigDecimal getBalance(UUID walletId) {
        return walletRepository.findById(walletId)
                .map(Wallet::getBalance)
                .orElseThrow(() -> new WalletNotFoundException("Wallet not found with ID: " + walletId));
    }
}
