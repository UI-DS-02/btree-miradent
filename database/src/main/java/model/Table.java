package model;

import java.util.*;

public class Table {
    public static class Column<T> {
        private String name;
        private T value;

        public Column(String name, T value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public T getValue() {
            return value;
        }

        public void setValue(T value) {
            this.value = value;
        }
    }

    public static class Row {
        private ArrayList<Column<?>> columns = new ArrayList<>();

        public ArrayList<Column<?>> getColumns() {
            return columns;
        }

    }

    //columns :names of columns
    //rows : rows
    // BTree : hashMap, cause every column has Btree for fast accessing
    // hashSetIndexes created for preventing collision
    private ArrayList<String> columnNames = new ArrayList<>();
    private ArrayList<Row> rows = new ArrayList<>();
    private HashMap<String, BPlusTree<Row>> BTrees = new HashMap<>();
    private HashMap<String, HashSet<Integer>> hashSetIndexes = new HashMap<>();
    private int columnCounts;

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

    public void setColumnNames(ArrayList<String> columnNames) {
        this.columnNames = columnNames;
    }

    public void setRows(ArrayList<Row> rows) {
        this.rows = rows;
    }

    public void setBTrees(HashMap<String, BPlusTree<Row>> BTrees) {
        this.BTrees = BTrees;
    }
}
