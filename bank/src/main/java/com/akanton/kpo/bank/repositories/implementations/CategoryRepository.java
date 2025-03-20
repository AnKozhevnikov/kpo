package com.akanton.kpo.bank.repositories.implementations;

import com.akanton.kpo.bank.domain.Category;
import com.akanton.kpo.bank.domain.CategoryType;
import com.akanton.kpo.bank.factories.CategoryFactory;
import com.akanton.kpo.bank.managers.IIdManager;
import com.akanton.kpo.bank.repositories.ICategoryRepository;
import com.akanton.kpo.bank.table.IRow;
import com.akanton.kpo.bank.table.ITable;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class CategoryRepository implements ICategoryRepository {

    @Autowired
    private CategoryFactory factory;

    @Autowired
    @Qualifier("CategoryIdManager")
    private IIdManager idManager;

    @Autowired
    ApplicationContext context;

    private List<Category> categories;

    public CategoryRepository() {
        this.categories = new ArrayList<>();
    }

    @Override
    public int addCategory(String name, CategoryType type) {
        Category category = factory.createCategory(name, type);
        categories.add(category);
        return category.getId();
    }

    @Override
    public void removeCategory(int id) {
        idManager.releaseId(id);
        categories.removeIf(category -> category.getId() == id);
    }

    @Override
    public void load(ITable table) {
        categories.clear();
        idManager.reset();
        for (IRow row : table) {
            Category category = new Category(
                    Integer.parseInt(row.get("id")),
                    row.get("name"),
                    CategoryType.valueOf(row.get("type"))
            );
            categories.add(category);
            idManager.reserveId(category.getId());
        }
    }

    @Override
    public ITable save() {
        List<String> headings = Arrays.asList("id", "name", "type");
        ITable table = context.getBean(ITable.class, headings);
        for (Category category : categories) {
            IRow row = context.getBean(IRow.class);
            row.put("id", Integer.toString(category.getId()));
            row.put("name", category.getName());
            row.put("type", category.getType().toString());
            table.addRow(row);
        }
        return table;
    }

    @Override
    public Iterable<Category> get(Collection<Integer> ids, Collection<CategoryType> types) {
        List<Category> result = new ArrayList<>();
        for (Category category : categories) {
            if (ids != null && !ids.isEmpty() && !ids.contains(category.getId())) {
                continue;
            }
            if (types != null && !types.isEmpty() && !types.contains(category.getType())) {
                continue;
            }
            result.add(category);
        }
        return result;
    }
}