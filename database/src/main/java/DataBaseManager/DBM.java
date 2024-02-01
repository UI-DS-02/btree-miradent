package DataBaseManager;

import model.BPlusTree;
import model.Table;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;

public class DBM {
    private final HashMap<String, Table> tables = new HashMap<>();

    public HashMap<String, Table> getTables() {
        return tables;
    }

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
            currentTable.getDedicatedRows().insert(currentTable.getRows().size() - 1, currentTable.getRows().size() - 1);
            //currentTable.getRows().add(newRow);
            return true;
        } else return false;
    }

    public void createTable(String name) {
        this.tables.put(name, new Table(name));
    }

    public void addColumn(String nameColumn, String tableName, String type) {
        this.currentTable = this.tables.get(tableName);
        currentTable.getColumnType().add(type);
        currentTable.setColumnCounts(currentTable.getColumnCounts() + 1);
        currentTable.getColumnNames().add(nameColumn);
        currentTable.getBTrees().put(nameColumn, new BPlusTree<>(5));
        currentTable.getHashSetIndexes().put(nameColumn, new HashSet<>());
        for (int i = 0; i < currentTable.getRows().size(); i++) {
            currentTable.getRows().get(i).getColumns().add(new Table.Column(nameColumn, null, false));
        }
    }

    public String[][] rangeOfRows(String tableName, int low, int high) {
        this.currentTable = this.tables.get(tableName);
        String[][] answer = new String[high - low + 1][this.currentTable.getColumnCounts()];
        for (int i = this.currentTable.getDedicatedRows().search(low); i <= this.currentTable.getDedicatedRows().search(high); i++) {
            for (int j = 0; j < this.currentTable.getColumnCounts(); j++) {
                answer[i - low][j] = this.currentTable.getRows().get(i).getColumns().get(j).getValue();
            }
        }
        return answer;
    }


    public String[][] showCompleteTable(String tableName) {
        this.currentTable = this.tables.get(tableName);
        String[][] table = new String[this.currentTable.getRows().size()+1][this.currentTable.getColumnCounts()+1];
        table[0][0] = "ID";
        for (int j = 1; j <= currentTable.getColumnNames().size(); j++) {
            table[0][j] = currentTable.getColumnNames().get(j-1);
        }
        for (int i = 1; i <= currentTable.getRows().size(); i++) {
            Table.Row current = currentTable.getRows().get(i-1);
            table[i][0] = String.valueOf(i-1);
            for (int j = 1; j <= current.getColumns().size(); j++) {
                table[i][j] = current.getColumns().get(j-1).getValue();
            }
        }
        return table;
    }

    public void updateRow(String tableName, String columnName, String oldValue, String newValue) {
        this.currentTable = this.tables.get(tableName);
        int columnIndex = this.currentTable.getColumnNames().indexOf(columnName);
        int index = this.getIndex(oldValue);
        int newIndex = this.getIndex(newValue);
        this.currentTable.getBTrees().get(columnName).search(index).getColumns().get(columnIndex).setValue(newValue);
        this.currentTable.getBTrees().get(columnName).insert(newIndex, this.currentTable.getBTrees().get(columnName).search(index));
        this.currentTable.getBTrees().get(columnName).delete(index);
        this.currentTable.getHashSetIndexes().get(columnName).remove(index);
        this.currentTable.getHashSetIndexes().get(columnName).add(newIndex);
    }

    public void updateByIndex(String tableName, int index, String columnName, String newValue) {
        this.currentTable = this.tables.get(tableName);
        int columnIndex = this.currentTable.getColumnNames().indexOf(columnName);
        int newIndex = this.getIndex(newValue);
        int preIndex = this.getIndex(this.currentTable.getRows().get(index).getColumns().get(columnIndex).getValue());
        this.currentTable.getRows().get(index).getColumns().get(columnIndex).setValue(newValue);
        this.currentTable.getHashSetIndexes().get(columnName).remove(preIndex);
        this.currentTable.getHashSetIndexes().get(columnName).add(newIndex);
        this.currentTable.getBTrees().get(columnName).insert(newIndex, this.currentTable.getRows().get(index));
        this.currentTable.getBTrees().get(columnName).delete(preIndex);
    }

    public void deletion(String tableName, int index) {
        this.currentTable = this.tables.get(tableName);
        ArrayList<String> values = new ArrayList<>();
        for (int i = 0; i < this.currentTable.getRows().get(index).getColumns().size(); i++) {
            values.add(this.currentTable.getRows().get(index).getColumns().get(i).getValue());
        }
        /*for (int i = index; i < this.currentTable.getRows().size() - 1; i++) {
            this.currentTable.getDedicatedRows().delete(i);
            this.currentTable.getDedicatedRows().insert(i, i);
        }*/
        this.currentTable.getDedicatedRows().delete(this.currentTable.getRows().size() - 1);
        this.currentTable.getRows().remove(index);
        for (int i = 0; i < values.size(); i++) {
            if (values.get(i) != null) {
                int removingIndex = this.getIndex(values.get(i));
                this.currentTable.getBTrees().get(this.currentTable.getColumnNames().get(i)).delete(removingIndex);
                this.currentTable.getHashSetIndexes().get(this.currentTable.getColumnNames().get(i)).remove(removingIndex);
            }
        }
    }

    public String selectRow(String tableName, int index) {
        this.currentTable = this.tables.get(tableName);
        StringBuilder answer = new StringBuilder();
        for (int i = 0; i < this.currentTable.getColumnCounts(); i++) {
            answer.append(this.currentTable.getRows().get(index).getColumns().get(i).getValue()).append(" ");
        }
        return answer.toString();
    }

    public String selectRow(String tableName, String columnName, String value) {
        this.currentTable = this.tables.get(tableName);
        StringBuilder answer = new StringBuilder();
        Table.Row out = this.currentTable.getBTrees().get(columnName).search(this.getIndex(value));
        for (int i = 0; i < this.currentTable.getColumnCounts(); i++) {
            answer.append(out.getColumns().get(i).getValue()).append(" ");
        }
        return answer.toString();
    }

    public String selectBlock(String tableName, int index, String columnName) {
        this.currentTable = this.tables.get(tableName);
        String answer = "";
        for (int i = 0; i < this.currentTable.getColumnCounts(); i++) {
            if (Objects.equals(this.currentTable.getRows().get(index).getColumns().get(i).getName(), columnName))
                answer = this.currentTable.getRows().get(index).getColumns().get(i).getValue();
        }
        return answer;
    }


    public int getIndex(String name) {
        int index = 1;
        for (int i = 0; i < name.length(); i++) {
            index += i * name.charAt(i);
        }
        return index;
    }

    private int makeIndexForBPTree(String column, String name) {
        int index = 1;
        for (int i = 0; i < name.length(); i++) {
            index += i * name.charAt(i);
        }
        while (currentTable.getHashSetIndexes().get(column).contains(index))
            index++;
        return index;
    }

    private boolean isDigit(String checking) {
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
