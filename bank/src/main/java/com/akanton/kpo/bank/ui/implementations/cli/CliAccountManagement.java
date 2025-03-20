package com.akanton.kpo.bank.ui.implementations.cli;

import com.akanton.kpo.bank.domain.BankAccount;
import com.akanton.kpo.bank.services.BankManagerService;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Component
public class CliAccountManagement {
    @Autowired
    ApplicationContext context;

    private final Scanner scanner = new Scanner(System.in);

    public void addAccount() {
        System.out.println("Enter account name: ");
        String name = scanner.nextLine();
        BankManagerService service = context.getBean(BankManagerService.class);
        int id = service.addBankAccount(name);
        System.out.println("Account created with id: " + id);
    }

    public void deleteAccount() {
        int id = InputManager.readInt("Enter account id: ");
        BankManagerService service = context.getBean(BankManagerService.class);
        service.removeBankAccount(id);
        System.out.println("Account deleted");
    }

    public void select() {
        System.out.println("Do you want to read a select tutorial?");
        System.out.println("1. Yes");
        System.out.println("2. No");

        int option = 0;
        while (option < 1 || option > 2) {
            option = InputManager.readInt("Enter your choice: ");
            if (option < 1 || option > 2) {
                System.out.println("Invalid choice. Please try again.");
            }
        }

        switch (option) {
            case 1:
                System.out.println("You can select bank accounts based on their ids and balance forks.");
                System.out.println("1. You'll be asked for ids count, then you'll need to type preferred ids.");
                System.out.println("2. You'll be asked for balance forks count, then you'll need to type preferred balance forks.");
                break;
            case 2:
                break;
        }

        int idsCount = InputManager.readInt("Enter ids count: ");
        List<Integer> ids = new ArrayList<>();
        for (int i = 0; i < idsCount; i++) {
            ids.add(InputManager.readInt("Enter id: "));
        }

        int balanceForksCount = InputManager.readInt("Enter balance forks count: ");
        List<Pair<Double, Double>> balanceForks = new ArrayList<>();
        for (int i = 0; i < balanceForksCount; i++) {
            double min = InputManager.readDouble("Enter min balance: ");
            double max = InputManager.readDouble("Enter max balance: ");
            balanceForks.add(new Pair<>(min, max));
        }

        BankManagerService service = context.getBean(BankManagerService.class);
        Iterable<BankAccount> accounts = service.selectBankAccounts(ids, balanceForks);
        System.out.println("====================================");
        for (BankAccount account : accounts) {
            System.out.println("Account id: " + account.getId());
            System.out.println("Account name: " + account.getName());
            System.out.println("Account balance: " + account.getBalance());
            System.out.println("====================================");
        }
    }

    public void start() {
        System.out.println("Account Management");
        System.out.println("1. Create Account");
        System.out.println("2. Delete Account");
        System.out.println("3. Do Select");

        int option = 0;
        while (option < 1 || option > 3) {
            option = InputManager.readInt("Enter your choice: ");
            if (option < 1 || option > 3) {
                System.out.println("Invalid choice. Please try again.");
            }
        }

        switch (option) {
            case 1:
                addAccount();
                break;
            case 2:
                deleteAccount();
                break;
            case 3:
                select();
                break;
        }
    }
}