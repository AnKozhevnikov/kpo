package com.anton.kpo.application.implementations;

import com.anton.kpo.application.interfaces.IAccountService;
import com.anton.kpo.domain.Account;
import com.anton.kpo.infrastructure.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AccountService implements IAccountService {
    @Autowired
    private AccountRepository accountRepository;

    @Override
    public Long createAccount(String accountName) {
        Account account = new Account(accountName, 0L);
        Account savedAccount = accountRepository.save(account);
        return savedAccount.getId();
    }

    @Override
    public String getAccountName(Long accountId) {
        return accountRepository.findById(accountId)
                .map(Account::getName)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));
    }

    @Override
    public void deposit(Long accountId, Long amount) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));
        account.setBalance(account.getBalance() + amount);
        accountRepository.save(account);
    }

    @Override
    public boolean withdraw(Long accountId, Long amount) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));
        if (account.getBalance() < amount) {
            return false;
        }
        account.setBalance(account.getBalance() - amount);
        accountRepository.save(account);
        return true;
    }

    @Override
    public Long getBalance(Long accountId) {
        return accountRepository.findById(accountId)
                .map(Account::getBalance)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));
    }

    @Override
    public Account getInfo(Long accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account not found"));
    }
}
