package DataBaseManager;

import model.Table;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;

public class DBM {
    private HashMap<String, Table> tables = new HashMap<>();
    private Table currentTable;

    public boolean insertRow(String tableName, ArrayList<Objects> values) {
        this.currentTable = tables.get(tableName);
        Table.Row newRow = new Table.Row();
        if (values.size() == this.currentTable.getColumnCounts()) {
            for (int i = 0; i < values.size(); i++) {
                newRow.getColumns().set(i, new Table.Column<>(currentTable.getColumnNames().get(i), values.get(i)));
                int index = this.makeIndexForBPTree(currentTable.getColumnNames().get(i), values.get(i).toString());
                currentTable.getHashSetIndexes().get(currentTable.getColumnNames().get(i)).add(index);
                currentTable.getBTrees().get(currentTable.getColumnNames().get(i)).insert(index, newRow);
            }
            currentTable.getRows().add(newRow);
            return true;
        } else return false;
    }
    public void createTable(String name){
        this.tables.put(name,new Table());
    }
    public void addColumn(){

    }

    public boolean updateRow() {
        return false;
    }

    public boolean deletion() {
        return false;
    }

    public int makeIndexForBPTree(String column, String name) {
        int index = 1;
        for (int i = 0; i < name.length(); i++) {
            index += i * name.charAt(i);
        }
        while (currentTable.getHashSetIndexes().get(column).contains(index))
            index++;
        return index;
    }
}
