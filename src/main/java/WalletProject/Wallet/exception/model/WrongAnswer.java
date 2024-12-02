package WalletProject.Wallet.exception.model;


import java.time.LocalDateTime;

public record WrongAnswer(LocalDateTime timestamp, int status, String error, String message) {
}
