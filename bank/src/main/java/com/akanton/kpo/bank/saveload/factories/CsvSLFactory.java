package com.akanton.kpo.bank.saveload.factories;

import com.akanton.kpo.bank.saveload.ISLFactory;
import com.akanton.kpo.bank.saveload.load.ILoad;
import com.akanton.kpo.bank.saveload.load.concrete.CsvLoad;
import com.akanton.kpo.bank.saveload.save.ISave;
import com.akanton.kpo.bank.saveload.save.concrete.CsvSave;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class CsvSLFactory implements ISLFactory {
    private final ApplicationContext context;

    @Autowired
    public CsvSLFactory(ApplicationContext context) {
        this.context = context;
    }

    public ISave getSave() {
        return new CsvSave();
    }

    public ILoad getLoad() {
        return context.getBean(CsvLoad.class);
    }
}
