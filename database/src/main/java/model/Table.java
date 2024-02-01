package model;

import java.util.*;

public class Table {

    public static class Column {
        private String name;
        private String value;
        private boolean digit;

        public Column(String name, String value, boolean digit) {
            this.name = name;
            this.value = value;
            this.digit = digit;
        }

        public boolean isDigit() {
            return digit;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    public static class Row {
        private ArrayList<Column> columns = new ArrayList<>();

        public ArrayList<Column> getColumns() {
            return columns;
        }

    }

    //columns :names of columns
    //rows : rows
    // BTree : hashMap, cause every column has Btree for fast accessing
    // hashSetIndexes created for preventing collision
    private final String name;
    private final ArrayList<String> columnNames = new ArrayList<>();
    private final BPlusTree<Integer> dedicatedRows = new BPlusTree<>(5);
    private ArrayList<Row> rows = new ArrayList<>();
    private HashMap<String, BPlusTree<Row>> BTrees = new HashMap<>();
    private final HashMap<String, HashSet<Integer>> hashSetIndexes = new HashMap<>();
    private final ArrayList<String> columnType = new ArrayList<>();
    public ArrayList<String> getColumnType() {
        return columnType;
    }
    private int columnCounts;

    public BPlusTree<Integer> getDedicatedRows() {
        return dedicatedRows;
    }

    public String getName() {
        return name;
    }

    public Table(String name) {
        this.name = name;
    }

    public ArrayList<String> getColumnNames() {
        return columnNames;
    }

    public int getColumnCounts() {
        return columnCounts;
    }

    public HashMap<String, BPlusTree<Row>> getBTrees() {
        return BTrees;
    }

    public HashMap<String, HashSet<Integer>> getHashSetIndexes() {
        return hashSetIndexes;
    }

    public ArrayList<Row> getRows() {
        return rows;
    }

    public void setColumnCounts(int columnCounts) {
        this.columnCounts = columnCounts;
    }

    public void setRows(ArrayList<Row> rows) {
        this.rows = rows;
    }

    public void setBTrees(HashMap<String, BPlusTree<Row>> BTrees) {
        this.BTrees = BTrees;
    }
}
