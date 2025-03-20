package com.akanton.kpo.bank.ui.implementations.cli;

import com.akanton.kpo.bank.domain.Category;
import com.akanton.kpo.bank.domain.CategoryType;
import com.akanton.kpo.bank.services.BankManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Component
public class CliCategoryManagement {
    @Autowired
    ApplicationContext context;

    private final Scanner scanner = new Scanner(System.in);

    public void addCategory() {
        System.out.println("Enter category name: ");
        String name = scanner.nextLine();
        System.out.println("Enter category type (INCOME/EXPENSE): ");
        CategoryType type = CategoryType.valueOf(scanner.nextLine().toUpperCase());
        BankManagerService service = context.getBean(BankManagerService.class);
        int id = service.addCategory(name, type);
        System.out.println("Category created with id: " + id);
    }

    public void deleteCategory() {
        int id = InputManager.readInt("Enter category id: ");
        BankManagerService service = context.getBean(BankManagerService.class);
        service.removeCategory(id);
        System.out.println("Category deleted");
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
                System.out.println("You can select categories based on their ids and types.");
                System.out.println("1. You'll be asked for ids count, then you'll need to type preferred ids.");
                System.out.println("2. You'll be asked for types count, then you'll need to type preferred types.");
                break;
            case 2:
                break;
        }

        int idsCount = InputManager.readInt("Enter ids count: ");
        List<Integer> ids = new ArrayList<>();
        for (int i = 0; i < idsCount; i++) {
            ids.add(InputManager.readInt("Enter id: "));
        }

        int typesCount = InputManager.readInt("Enter types count: ");
        List<CategoryType> types = new ArrayList<>();
        for (int i = 0; i < typesCount; i++) {
            System.out.println("Enter type (INCOME/EXPENSE): ");
            types.add(CategoryType.valueOf(scanner.nextLine().toUpperCase()));
        }

        BankManagerService service = context.getBean(BankManagerService.class);
        Iterable<Category> categories = service.selectCategories(ids, types);
        System.out.println("====================================");
        for (Category category : categories) {
            System.out.println("Category id: " + category.getId());
            System.out.println("Category name: " + category.getName());
            System.out.println("Category type: " + category.getType());
            System.out.println("====================================");
        }
    }

    public void start() {
        System.out.println("Category Management");
        System.out.println("1. Create Category");
        System.out.println("2. Delete Category");
        System.out.println("3. Do select");

        int option = 0;
        while (option < 1 || option > 3) {
            option = InputManager.readInt("Enter your choice: ");
            if (option < 1 || option > 3) {
                System.out.println("Invalid choice. Please try again.");
            }
        }

        switch (option) {
            case 1:
                addCategory();
                break;
            case 2:
                deleteCategory();
                break;
            case 3:
                select();
                break;
        }
    }
}