package com.akanton.kpo.bank.saveload.load;

import com.akanton.kpo.bank.table.ITable;

public interface ILoad {
    public ITable load(String path);
}
