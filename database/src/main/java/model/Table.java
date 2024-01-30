package model;

import java.util.ArrayList;
import java.util.Objects;

public class Table {
    static class Column<T> {
        private String name;
        private T value;

        public Column(String name) {
            this.name = name;
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

    static class Row {
        private ArrayList<Column<?>> row = new ArrayList<>();

        public ArrayList<Column<?>> getRow() {
            return row;
        }

    }

}
