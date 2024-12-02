package WalletProject.Wallet.exception.Exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DataException extends RuntimeException {
    public DataException(final String message) {
        super(message);
        log.error(message);
    }
}
