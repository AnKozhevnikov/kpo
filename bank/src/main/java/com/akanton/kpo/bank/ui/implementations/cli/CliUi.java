package com.akanton.kpo.bank.ui.implementations.cli;

import com.akanton.kpo.bank.services.SaveLoadService;
import com.akanton.kpo.bank.ui.IUi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.util.Scanner;

public class CliUi implements IUi {
    @Autowired
    ApplicationContext context;

    private final Scanner scanner = new Scanner(System.in);

    public void save() {
        System.out.println("Enter the file type option: ");
        System.out.println("1. CSV");
        int option = 0;
        String type = null;
        while (option != 1) {
            option = 0;
            while (option < 1 || option > 1) {
                option = InputManager.readInt("Enter your choice: ");
                if (option < 1 || option > 1) {
                    System.out.println("Invalid choice. Please try again.");
                }
            }
            switch (option) {
                case 1:
                    type = "csv";
                    break;
            }
        }

        String pathBankAccount, pathCategory, pathOperation;

        System.out.print("Enter the path for Bank Account: ");
        pathBankAccount = scanner.nextLine();
        System.out.print("Enter the path for Category: ");
        pathCategory = scanner.nextLine();
        System.out.print("Enter the path for Operation: ");
        pathOperation = scanner.nextLine();

        SaveLoadService service = context.getBean(SaveLoadService.class);
        service.save(type, pathBankAccount, pathCategory, pathOperation);
    }

    public void load() {
        System.out.println("Enter the file type option: ");
        System.out.println("1. CSV");
        int option = 0;
        String type = null;
        while (option != 1) {
            option = 0;
            while (option < 1 || option > 1) {
                option = InputManager.readInt("Enter your choice: ");
                if (option < 1 || option > 1) {
                    System.out.println("Invalid choice. Please try again.");
                }
            }
            switch (option) {
                case 1:
                    type = "csv";
                    break;
            }
        }

        String pathBankAccount, pathCategory, pathOperation;

        System.out.print("Enter the path for Bank Account: ");
        pathBankAccount = scanner.nextLine();
        System.out.print("Enter the path for Category: ");
        pathCategory = scanner.nextLine();
        System.out.print("Enter the path for Operation: ");
        pathOperation = scanner.nextLine();

        SaveLoadService service = context.getBean(SaveLoadService.class);
        service.load(type, pathBankAccount, pathCategory, pathOperation);
    }

    public void bankManagement() {
        System.out.println("Bank Management");
        System.out.println("Choose an option:");
        System.out.println("1. Account Management");
        System.out.println("2. Category Management");
        System.out.println("3. Operation Management");
        System.out.println("4. Back");
        int option = 0;
        while (option < 1 || option > 4) {
            option = InputManager.readInt("Enter your choice: ");
            if (option < 1 || option > 4) {
                System.out.println("Invalid choice. Please try again.");
            }
        }

        switch (option) {
            case 1:
                CliAccountManagement accountManagement = context.getBean(CliAccountManagement.class);
                accountManagement.start();
                break;
            case 2:
                CliCategoryManagement categoryManagement = context.getBean(CliCategoryManagement.class);
                categoryManagement.start();
                break;
            case 3:
                CliOperationManagement operationManagement = context.getBean(CliOperationManagement.class);
                operationManagement.start();
                break;
            case 4:
                break;
        }
    }

    @Override
    public void start() {
        int option = 0;
        while (option != 4) {
            option = 0;
            System.out.println("Main menu");
            System.out.println("Choose an option:");
            System.out.println("1. Bank Management");
            System.out.println("2. Save");
            System.out.println("3. Load");
            System.out.println("4. Exit");
            while (option < 1 || option > 4) {
                option = InputManager.readInt("Enter your choice: ");
                if (option < 1 || option > 4) {
                    System.out.println("Invalid choice. Please try again.");
                }
            }
            switch (option) {
                case 1:
                    bankManagement();
                    break;
                case 2:
                    save();
                    break;
                case 3:
                    load();
                    break;
                case 4:
                    System.out.println("Exiting...");
                    break;
            }
        }
    }
}
