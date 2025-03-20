package com.akanton.kpo.bank.saveload.save.concrete;

import com.akanton.kpo.bank.saveload.save.ISave;
import com.akanton.kpo.bank.table.IRow;
import com.akanton.kpo.bank.table.ITable;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

@Component
public class CsvSave implements ISave {
    public CsvSave() {
    }

    public void save(ITable table, String path) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
            Iterable<String> headings = table.getHeadings();
            List<String> headingList = (List<String>) headings;
            for (int i = 0; i < headingList.size(); i++) {
                writer.write(headingList.get(i));
                if (i < headingList.size() - 1) {
                    writer.write(",");
                }
            }
            writer.write("\n");
            for (IRow row : table) {
                List<String> rowList = new ArrayList<String>();
                for (String heading : headingList) {
                    rowList.add(row.get(heading));
                }
                for (int i = 0; i < rowList.size(); i++) {
                    writer.write("\"" + rowList.get(i) + "\"");
                    if (i < rowList.size() - 1) {
                        writer.write(",");
                    }
                }
                writer.write("\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
