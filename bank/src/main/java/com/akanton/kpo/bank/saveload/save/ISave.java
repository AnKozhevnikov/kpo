package com.akanton.kpo.bank.saveload.save;

import com.akanton.kpo.bank.table.ITable;

public interface ISave {
    public void save(ITable table, String path);
}
