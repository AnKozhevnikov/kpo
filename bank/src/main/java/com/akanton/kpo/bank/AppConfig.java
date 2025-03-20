package com.akanton.kpo.bank;

import com.akanton.kpo.bank.managers.IIdManager;
import com.akanton.kpo.bank.managers.implementations.IdManager;
import com.akanton.kpo.bank.repositories.IBankAccountRepository;
import com.akanton.kpo.bank.repositories.ICategoryRepository;
import com.akanton.kpo.bank.repositories.IOperationRepository;
import com.akanton.kpo.bank.repositories.implementations.BankAccountRepository;
import com.akanton.kpo.bank.repositories.implementations.CategoryRepository;
import com.akanton.kpo.bank.repositories.implementations.OperationRepository;
import com.akanton.kpo.bank.table.ITable;
import com.akanton.kpo.bank.table.implementations.Table;
import com.akanton.kpo.bank.ui.IUi;
import com.akanton.kpo.bank.ui.implementations.cli.CliUi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import com.akanton.kpo.bank.table.IRow;
import com.akanton.kpo.bank.table.implementations.Row;
import org.springframework.context.annotation.Scope;

@Configuration
@ComponentScan(basePackages = "com.akanton.kpo.bank")
public class AppConfig {
    @Bean
    public IIdManager BankAccountIdManager() {
        return new IdManager();
    }

    @Bean
    public IIdManager OperationIdManager() {
        return new IdManager();
    }

    @Bean
    public IIdManager CategoryIdManager() {
        return new IdManager();
    }

    @Bean
    @Scope("prototype")
    public IRow row() {
        return new Row();
    }

    @Bean
    @Scope("prototype")
    public ITable table(Iterable<String> headings) {
        return new Table(headings);
    }

    @Bean
    public IBankAccountRepository bankAccountRepository() {
        return new BankAccountRepository();
    }

    @Bean
    public IOperationRepository operationRepository() {
        return new OperationRepository();
    }

    @Bean
    public ICategoryRepository categoryRepository() {
        return new CategoryRepository();
    }

    @Bean
    public IUi ui() {
        return new CliUi();
    }
}
