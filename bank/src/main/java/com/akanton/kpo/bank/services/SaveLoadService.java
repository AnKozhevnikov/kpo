package com.akanton.kpo.bank.services;

import com.akanton.kpo.bank.repositories.IBankAccountRepository;
import com.akanton.kpo.bank.repositories.ICategoryRepository;
import com.akanton.kpo.bank.repositories.IOperationRepository;
import com.akanton.kpo.bank.saveload.ISLFactory;
import com.akanton.kpo.bank.saveload.factories.CsvSLFactory;
import com.akanton.kpo.bank.saveload.load.ILoad;
import com.akanton.kpo.bank.saveload.save.ISave;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class SaveLoadService {
    IBankAccountRepository bankAccountRepository;
    ICategoryRepository categoryRepository;
    IOperationRepository operationRepository;
    ApplicationContext context;

    @Autowired
    public SaveLoadService(ApplicationContext context, IBankAccountRepository bankAccountRepository, ICategoryRepository categoryRepository, IOperationRepository operationRepository) {
        this.bankAccountRepository = bankAccountRepository;
        this.categoryRepository = categoryRepository;
        this.operationRepository = operationRepository;
        this.context = context;
    }

    public void save(String type, String path1BankAccount, String pathCategory, String pathOperation) {
        ISLFactory factory = null;
        switch (type) {
            case "csv":
                factory = context.getBean(CsvSLFactory.class);
                break;
        }
        ISave saver = factory.getSave();

        saver.save(bankAccountRepository.save(), path1BankAccount);
        saver.save(categoryRepository.save(), pathCategory);
        saver.save(operationRepository.save(), pathOperation);
    }

    public void load(String type, String pathBankAccount, String pathCategory, String pathOperation) {
        ISLFactory factory = null;
        switch (type) {
            case "csv":
                factory = context.getBean(CsvSLFactory.class);
                break;
        }
        ILoad loader = factory.getLoad();

        bankAccountRepository.load(loader.load(pathBankAccount));
        categoryRepository.load(loader.load(pathCategory));
        operationRepository.load(loader.load(pathOperation));
    }
}
