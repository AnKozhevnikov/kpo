package com.akanton.kpo.bank.ui.implementations.cli;

import com.akanton.kpo.bank.domain.CategoryType;
import com.akanton.kpo.bank.domain.Operation;
import com.akanton.kpo.bank.services.BankManagerService;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

@Component
public class CliOperationManagement {
    @Autowired
    ApplicationContext context;

    private final Scanner scanner = new Scanner(System.in);

    public void addOperation() {
        int accountId = InputManager.readInt("Enter account id: ");
        int categoryId = InputManager.readInt("Enter category id: ");
        double amount = InputManager.readDouble("Enter amount: ");
        System.out.println("Enter description: ");
        String description = scanner.nextLine();
        System.out.println("Enter date (timestamp): ");
        Date date = new Date(Long.parseLong(scanner.nextLine()));

        BankManagerService service = context.getBean(BankManagerService.class);
        int id = service.addOperation(accountId, categoryId, amount, description, date);
        System.out.println("Operation created with id: " + id);
    }

    public void deleteOperation() {
        int id = InputManager.readInt("Enter operation id: ");
        BankManagerService service = context.getBean(BankManagerService.class);
        service.removeOperation(id);
        System.out.println("Operation deleted");
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
                System.out.println("You can filter operations based on their ids, account ids, category ids, amount ranges, and date ranges.");
                System.out.println("1. You'll be asked for operation ids count, then you'll need to type preferred ids.");
                System.out.println("2. You'll be asked for account ids count, then you'll need to type preferred account ids.");
                System.out.println("3. You'll be asked for category ids count, then you'll need to type preferred category ids.");
                System.out.println("4. You'll be asked for amount ranges count, then you'll need to type preferred amount ranges.");
                System.out.println("5. You'll be asked for date ranges count, then you'll need to type preferred date ranges.");
                break;
            case 2:
                break;
        }

        int operationIdsCount = InputManager.readInt("Enter operation ids count: ");
        List<Integer> operationIds = new ArrayList<>();
        for (int i = 0; i < operationIdsCount; i++) {
            operationIds.add(InputManager.readInt("Enter operation id: "));
        }

        int accountIdsCount = InputManager.readInt("Enter account ids count: ");
        List<Integer> accountIds = new ArrayList<>();
        for (int i = 0; i < accountIdsCount; i++) {
            accountIds.add(InputManager.readInt("Enter account id: "));
        }

        int categoryIdsCount = InputManager.readInt("Enter category ids count: ");
        List<Integer> categoryIds = new ArrayList<>();
        for (int i = 0; i < categoryIdsCount; i++) {
            categoryIds.add(InputManager.readInt("Enter category id: "));
        }

        int amountRangesCount = InputManager.readInt("Enter amount ranges count: ");
        List<Pair<Double, Double>> amountRanges = new ArrayList<>();
        for (int i = 0; i < amountRangesCount; i++) {
            double minAmount = InputManager.readDouble("Enter min amount: ");
            double maxAmount = InputManager.readDouble("Enter max amount: ");
            amountRanges.add(new Pair<>(minAmount, maxAmount));
        }

        int dateRangesCount = InputManager.readInt("Enter date ranges count: ");
        List<Pair<Date, Date>> dateRanges = new ArrayList<>();
        for (int i = 0; i < dateRangesCount; i++) {
            System.out.println("Enter start date (timestamp): ");
            Date startDate = new Date(Long.parseLong(scanner.nextLine()));
            System.out.println("Enter end date (timestamp): ");
            Date endDate = new Date(Long.parseLong(scanner.nextLine()));
            dateRanges.add(new Pair<>(startDate, endDate));
        }

        BankManagerService service = context.getBean(BankManagerService.class);
        Iterable<Operation> operations = service.selectOperations(operationIds, accountIds, categoryIds, amountRanges, dateRanges);
        System.out.println("====================================");
        for (Operation operation : operations) {
            System.out.println("Operation id: " + operation.getId());
            System.out.println("Account id: " + operation.getAccountId());
            System.out.println("Category id: " + operation.getCategoryId());
            System.out.println("Amount: " + operation.getAmount());
            System.out.println("Description: " + operation.getDescription());
            System.out.println("Date: " + operation.getDate());
            System.out.println("====================================");
        }
    }

    public void start() {
        System.out.println("Operation Management");
        System.out.println("1. Create Operation");
        System.out.println("2. Delete Operation");

        int option = 0;
        while (option < 1 || option > 3) {
            option = InputManager.readInt("Enter your choice: ");
            if (option < 1 || option > 3) {
                System.out.println("Invalid choice. Please try again.");
            }
        }

        switch (option) {
            case 1:
                addOperation();
                break;
            case 2:
                deleteOperation();
                break;
            case 3:
                select();
                break;
        }
    }
}