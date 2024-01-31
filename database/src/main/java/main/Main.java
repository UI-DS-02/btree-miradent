package main;

import DataBaseManager.DBM;
import model.Table;

import java.util.ArrayList;
import java.util.Objects;

public class Main {
    public static void main(String[] args) {
        DBM dbm = new DBM();
        dbm.createTable("hossein");
        dbm.addColumn("id","hossein");
        ArrayList<String> s = new ArrayList<>();
        ArrayList<String> ss = new ArrayList<>();
        ss.add("biu");
        s.add("33");
        dbm.insertRow("hossein",s);
        dbm.insertRow("hossein",ss);
        dbm.addColumn("XX","hossein");
        dbm.updateRow("hossein","id","33","67");
        dbm.updateByIndex("hossein",0,"id","52");
        String[][] sss = dbm.showCompleteTable("hossein");
        for (int i = 0; i < sss.length; i++) {
            for (int j = 0; j <sss[i].length ; j++) {
                System.out.println(sss[i][j]);
            }
        }
    }
}
