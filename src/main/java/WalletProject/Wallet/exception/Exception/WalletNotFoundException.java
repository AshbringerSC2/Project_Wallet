package WalletProject.Wallet.exception.Exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WalletNotFoundException extends RuntimeException {
    public WalletNotFoundException(final String message) {
        super(message);
        log.error(message);
    }
}
