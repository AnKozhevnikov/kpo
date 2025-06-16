package com.anton.kpo.application.implementations;

import com.anton.kpo.domain.Account;
import com.anton.kpo.infrastructure.repositories.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountService accountService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createAccount_shouldSaveAccountAndReturnId() {
        // Arrange
        String accountName = "Test Account";
        Long expectedAccountId = 1L;

        Account accountToSave = new Account(accountName, 0L);
        Account savedAccount = new Account(accountName, 0L);
        savedAccount.setId(expectedAccountId);

        when(accountRepository.save(any(Account.class))).thenReturn(savedAccount);

        // Act
        Long actualAccountId = accountService.createAccount(accountName);

        // Assert
        assertEquals(expectedAccountId, actualAccountId);
        verify(accountRepository, times(1)).save(argThat(account ->
                account.getName().equals(accountName) &&
                account.getBalance().equals(0L)
        ));
    }

    @Test
    void deposit_shouldIncreaseAccountBalance() {
        // Arrange
        Long accountId = 1L;
        Long initialBalance = 100L;
        Long depositAmount = 50L;
        Account account = new Account("Test Account", initialBalance);
        account.setId(accountId);

        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));

        // Act
        accountService.deposit(accountId, depositAmount);

        // Assert
        assertEquals(initialBalance + depositAmount, account.getBalance());
        verify(accountRepository, times(1)).save(account);
    }

    @Test
    void deposit_shouldThrowException_whenAccountNotFound() {
        // Arrange
        Long accountId = 99L;
        Long depositAmount = 50L;
        when(accountRepository.findById(accountId)).thenReturn(Optional.empty());

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            accountService.deposit(accountId, depositAmount);
        });
        assertTrue(exception.getMessage().contains("Account not found"));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        verify(accountRepository, times(1)).findById(accountId);
        verify(accountRepository, never()).save(any(Account.class));
    }

    @Test
    void withdraw_shouldDecreaseAccountBalance_whenSufficientFunds() {
        // Arrange
        Long accountId = 1L;
        Long initialBalance = 100L;
        Long withdrawalAmount = 50L;
        Account account = new Account("Test Account", initialBalance);
        account.setId(accountId);

        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));

        // Act
        boolean withdrawn = accountService.withdraw(accountId, withdrawalAmount);

        // Assert
        assertTrue(withdrawn);
        assertEquals(initialBalance - withdrawalAmount, account.getBalance());
        verify(accountRepository, times(1)).save(account);
    }

    @Test
    void withdraw_shouldReturnFalse_whenInsufficientFunds() {
        // Arrange
        Long accountId = 1L;
        Long initialBalance = 20L;
        Long withdrawalAmount = 50L;
        Account account = new Account("Test Account", initialBalance);
        account.setId(accountId);

        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));

        // Act
        boolean withdrawn = accountService.withdraw(accountId, withdrawalAmount);

        // Assert
        assertFalse(withdrawn);
        assertEquals(initialBalance, account.getBalance()); // Balance should not change
        verify(accountRepository, never()).save(any(Account.class)); // Save should not be called
    }

    @Test
    void withdraw_shouldThrowException_whenAccountNotFound() {
        // Arrange
        Long accountId = 99L;
        Long withdrawalAmount = 50L;
        when(accountRepository.findById(accountId)).thenReturn(Optional.empty());

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            accountService.withdraw(accountId, withdrawalAmount);
        });
        assertTrue(exception.getMessage().contains("Account not found"));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        verify(accountRepository, times(1)).findById(accountId);
        verify(accountRepository, never()).save(any(Account.class));
    }

    @Test
    void getBalance_shouldReturnBalance_whenAccountFound() {
        // Arrange
        Long accountId = 1L;
        Long expectedBalance = 250L;
        Account account = new Account("Test Account", expectedBalance);
        account.setId(accountId);

        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));

        // Act
        Long actualBalance = accountService.getBalance(accountId);

        // Assert
        assertEquals(expectedBalance, actualBalance);
        verify(accountRepository, times(1)).findById(accountId);
    }

    @Test
    void getBalance_shouldThrowException_whenAccountNotFound() {
        // Arrange
        Long accountId = 99L;
        when(accountRepository.findById(accountId)).thenReturn(Optional.empty());

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            accountService.getBalance(accountId);
        });
        assertTrue(exception.getMessage().contains("Account not found"));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        verify(accountRepository, times(1)).findById(accountId);
    }
} 