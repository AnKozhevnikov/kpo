package com.akanton.kpo.bank.factories;

import com.akanton.kpo.bank.domain.Category;
import com.akanton.kpo.bank.domain.CategoryType;
import com.akanton.kpo.bank.managers.IIdManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class CategoryFactory {
    private IIdManager idManager;

    @Autowired
    public CategoryFactory(@Qualifier("CategoryIdManager") IIdManager idManager) {
        this.idManager = idManager;
    }

    public Category createCategory(String name, CategoryType type) {
        return new Category(idManager.getNextId(), name, type);
    }
}
