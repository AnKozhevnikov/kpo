package com.akanton.kpo.bank.saveload;

import com.akanton.kpo.bank.saveload.load.ILoad;
import com.akanton.kpo.bank.saveload.save.ISave;

public interface ISLFactory {
    public ISave getSave();
    public ILoad getLoad();
}
