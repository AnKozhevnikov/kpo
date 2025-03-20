package com.akanton.kpo.bank.repositories.implementations;

import com.akanton.kpo.bank.domain.Operation;
import com.akanton.kpo.bank.factories.OperationFactory;
import com.akanton.kpo.bank.managers.IIdManager;
import com.akanton.kpo.bank.repositories.IOperationRepository;
import com.akanton.kpo.bank.table.IRow;
import com.akanton.kpo.bank.table.ITable;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class OperationRepository implements IOperationRepository {

    @Autowired
    private OperationFactory factory;

    @Autowired
    @Qualifier("OperationIdManager")
    private IIdManager idManager;

    @Autowired
    ApplicationContext context;

    private List<Operation> operations;

    public OperationRepository() {
        this.operations = new ArrayList<>();
    }

    @Override
    public int addOperation(int accountId, int categoryId, double amount, String description, Date date) {
        Operation operation = factory.createOperation(accountId, categoryId, amount, description, date);
        operations.add(operation);
        return operation.getId();
    }

    @Override
    public void removeOperation(int id) {
        idManager.releaseId(id);
        operations.removeIf(operation -> operation.getId() == id);
    }

    @Override
    public void load(ITable table) {
        operations.clear();
        idManager.reset();
        for (IRow row : table) {
            Operation operation = new Operation(
                    Integer.parseInt(row.get("id")),
                    Integer.parseInt(row.get("accountId")),
                    Integer.parseInt(row.get("categoryId")),
                    Double.parseDouble(row.get("amount")),
                    row.get("description"),
                    new Date(Long.parseLong(row.get("date")))
            );
            operations.add(operation);
            idManager.reserveId(operation.getId());
        }
    }

    @Override
    public ITable save() {
        List<String> headings = List.of("id", "accountId", "categoryId", "amount", "description", "date");
        ITable table = context.getBean(ITable.class, headings);
        for (Operation operation : operations) {
            IRow row = context.getBean(IRow.class);
            row.put("id", Integer.toString(operation.getId()));
            row.put("accountId", Integer.toString(operation.getAccountId()));
            row.put("categoryId", Integer.toString(operation.getCategoryId()));
            row.put("amount", Double.toString(operation.getAmount()));
            row.put("description", operation.getDescription());
            row.put("date", Long.toString(operation.getDate().getTime()));
            table.addRow(row);
        }
        return table;
    }

    @Override
    public Iterable<Operation> get(Collection<Integer> ids, Collection<Integer> accountIds, Collection<Integer> categoryIds, Collection<Pair<Double, Double>> amounts, Collection<Pair<Date, Date>> dates) {
        List<Operation> result = new ArrayList<>();
        for (Operation operation : operations) {
            if (ids != null && !ids.isEmpty() && !ids.contains(operation.getId())) {
                continue;
            }
            if (accountIds != null && !accountIds.isEmpty() && !accountIds.contains(operation.getAccountId())) {
                continue;
            }
            if (categoryIds != null && !categoryIds.isEmpty() && !categoryIds.contains(operation.getCategoryId())) {
                continue;
            }
            if (amounts != null && !amounts.isEmpty()) {
                boolean found = false;
                for (Pair<Double, Double> amountRange : amounts) {
                    if (amountRange.getKey() <= operation.getAmount() && operation.getAmount() <= amountRange.getValue()) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    continue;
                }
            }
            if (dates != null && !dates.isEmpty()) {
                boolean found = false;
                for (Pair<Date, Date> dateRange : dates) {
                    if (!operation.getDate().before(dateRange.getKey()) && !operation.getDate().after(dateRange.getValue())) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    continue;
                }
            }
            result.add(operation);
        }
        return result;
    }
}
