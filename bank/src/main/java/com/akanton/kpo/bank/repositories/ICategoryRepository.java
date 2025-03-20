package com.akanton.kpo.bank.repositories;

import com.akanton.kpo.bank.domain.Category;
import com.akanton.kpo.bank.domain.CategoryType;
import com.akanton.kpo.bank.table.ITable;

import java.util.Collection;

public interface ICategoryRepository {
    public int addCategory(String name, CategoryType type);
    public void removeCategory(int id);
    public void load(ITable table);
    public ITable save();

    public Iterable<Category> get(Collection<Integer> ids, Collection<CategoryType> types);
}
