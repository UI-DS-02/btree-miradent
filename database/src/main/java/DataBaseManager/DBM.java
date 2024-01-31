package DataBaseManager;

import model.BPlusTree;
import model.Table;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;

public class DBM {
    private HashMap<String, Table> tables = new HashMap<>();
    private Table currentTable;

    public boolean insertRow(String tableName, ArrayList<String> values) {
        this.currentTable = tables.get(tableName);
        currentTable.getRows().add(new Table.Row());
        Table.Row newRow = currentTable.getRows().get(currentTable.getRows().size() - 1);
        if (values.size() == this.currentTable.getColumnCounts()) {
            for (int i = 0; i < values.size(); i++) {
                newRow.getColumns().add(new Table.Column(currentTable.getColumnNames().get(i), values.get(i), isDigit(values.get(i))));
                int index = this.makeIndexForBPTree(currentTable.getColumnNames().get(i), values.get(i));
                currentTable.getHashSetIndexes().get(currentTable.getColumnNames().get(i)).add(index);
                currentTable.getBTrees().get(currentTable.getColumnNames().get(i)).insert(index, newRow);
            }
            currentTable.getDedicatedRows().insert(currentTable.getRows().size()-1, newRow);
            //currentTable.getRows().add(newRow);
            return true;
        } else return false;
    }

    public void createTable(String name) {
        this.tables.put(name, new Table(name));
    }

    public void addColumn(String nameColumn, String tableName) {
        this.currentTable = this.tables.get(tableName);
        currentTable.setColumnCounts(currentTable.getColumnCounts() + 1);
        currentTable.getColumnNames().add(nameColumn);
        currentTable.getBTrees().put(nameColumn, new BPlusTree<>(5));
        currentTable.getHashSetIndexes().put(nameColumn, new HashSet<>());
        for (int i = 0; i < currentTable.getRows().size(); i++) {
            currentTable.getRows().get(i).getColumns().add(new Table.Column(nameColumn, null, false));
        }
    }

    public String[][] showCompleteTable(String tableName) {
        this.currentTable = this.tables.get(tableName);
        String[][] table = new String[this.currentTable.getRows().size()][this.currentTable.getColumnCounts()];
        for (int i = 0; i < currentTable.getRows().size(); i++) {
            Table.Row current = currentTable.getRows().get(i);
            for (int j = 0; j < current.getColumns().size(); j++) {
                table[i][j] = current.getColumns().get(j).getValue();
            }
        }
        return table;
    }

    public void updateRow(String tableName, String columnName, String oldValue, String newValue) {
        this.currentTable = this.tables.get(tableName);
        int columnIndex = this.currentTable.getColumnNames().indexOf(columnName);
        int index = this.getIndex(oldValue);
        this.currentTable.getBTrees().get(columnName).search(index).getColumns().get(columnIndex).setValue(newValue);
    }

    public void updateByIndex(String tableName, int index, String columnName, String newValue) {
        this.currentTable = this.tables.get(tableName);
        int columnIndex = this.currentTable.getColumnNames().indexOf(columnName);
        this.currentTable.getDedicatedRows().search(index).getColumns().get(columnIndex).setValue(newValue);
    }

    public void deletion(String tableName , int index) {
        this.currentTable = this.tables.get(tableName);
        ArrayList<String> values = new ArrayList<>();
        for (int i = 0; i < this.currentTable.getRows().get(index).getColumns().size(); i++) {
            values.add(this.currentTable.getRows().get(index).getColumns().get(i).getValue());
        }

    }

    public int getIndex(String name) {
        int index = 1;
        for (int i = 0; i < name.length(); i++) {
            index += i * name.charAt(i);
        }
        return index;
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

    public boolean isDigit(String checking) {
        boolean check = true;
        for (int i = 0; i < checking.length(); i++) {
            if (!Character.isDigit(checking.charAt(i))) {
                check = false;
                break;
            }
        }
        return check;
    }
}
