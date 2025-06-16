package com.anton.kpo.application.interfaces;

import com.anton.kpo.domain.Account;

public interface IAccountService {
    public Long createAccount(String name);
    public String getAccountName(Long accountId);
    public void deposit(Long accountId, Long amount);
    public boolean withdraw(Long accountId, Long amount);
    public Long getBalance(Long accountId);
    public Account getInfo(Long accountId);
}
