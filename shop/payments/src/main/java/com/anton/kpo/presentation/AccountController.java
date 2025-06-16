package com.anton.kpo.presentation;

import com.anton.kpo.application.interfaces.IAccountService;
import com.anton.kpo.domain.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountController {
    @Autowired
    private IAccountService accountService;

    @PostMapping("/account")
    public Long createAccount(@RequestParam String name) {
        return accountService.createAccount(name);
    }

    @PostMapping("/account/deposit")
    public void deposit(@RequestParam Long accountId, @RequestParam Long amount) {
        accountService.deposit(accountId, amount);
    }

    @GetMapping("/account/info")
    public Account getAccountInfo(@RequestParam Long accountId) {
        return accountService.getInfo(accountId);
    }
}
