package com.akanton.kpo.bank.saveload.load.concrete;

import com.akanton.kpo.bank.saveload.load.ILoad;
import com.akanton.kpo.bank.table.IRow;
import com.akanton.kpo.bank.table.ITable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;

@Component
public class CsvLoad implements ILoad {
    private final ApplicationContext context;

    @Autowired
    public CsvLoad(ApplicationContext context) {
        this.context = context;
    }

    public ITable load(String path) {
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line = reader.readLine();
            ArrayList<String> headings = new ArrayList<>(Arrays.asList(line.split(",")));
            ITable table = (ITable) context.getBean("table", headings);

            while ((line = reader.readLine()) != null) {
                String current = "";
                ArrayList<String> rowlist = new ArrayList<>();
                boolean open = false;
                for (int i = 0; i < line.length(); i++) {
                    if (line.charAt(i) == '\"') {
                        if (open) {
                            open = false;
                            rowlist.add(current);
                            current = "";
                        } else {
                            open = true;
                        }
                    } else if (open) {
                        current += line.charAt(i);
                    }
                }

                IRow row = context.getBean(IRow.class);
                for (int i = 0; i < headings.size(); i++) {
                    row.put(headings.get(i), rowlist.get(i));
                }

                table.addRow(row);
            }

            return table;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
