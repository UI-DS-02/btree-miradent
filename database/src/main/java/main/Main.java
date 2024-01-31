package main;

import DataBaseManager.DBM;
import model.Table;

import java.util.ArrayList;
import java.util.Objects;

public class Main {
    public static void main(String[] args) {
        DBM dbm = new DBM();
        dbm.createTable("hossein");
        dbm.addColumn("id", "hossein");
        dbm.addColumn("x", "hossein");
        dbm.addColumn("z", "hossein");
        dbm.addColumn("y", "hossein");
        dbm.addColumn("w", "hossein");
        ArrayList<String> s = new ArrayList<>();
        ArrayList<String> ss = new ArrayList<>();
        s.add("hossein");
        s.add("Reza");
        s.add("ali");
        s.add("mousa");
        s.add("sea");
        ss.add("sea");
        ss.add("mousa");
        ss.add("ali");
        ss.add("Reza");
        ss.add("hossein");
        ArrayList<String> bb = new ArrayList<>();
        bb.add("mohsen");
        bb.add("poor");
        bb.add("is");
        bb.add("like");
        bb.add("donkey");
        dbm.insertRow("hossein", s);
        dbm.insertRow("hossein",bb);
        dbm.insertRow("hossein", ss);
        dbm.updateRow("hossein", "id", "sea", "forest");
        dbm.updateByIndex("hossein", 2, "w", "hossein2");
        dbm.deletion("hossein", 0);
        String[][] sss = dbm.showCompleteTable("hossein");

        for (int i = 0; i < sss.length; i++) {
            for (int j = 0; j < sss[i].length; j++) {
                System.out.print(sss[i][j] + " ");
            }
            System.out.println();
        }
        sss = dbm.rangeOfRows("hossein",0,1);
        System.out.println();
        System.out.println();
        for (int i = 0; i < sss.length; i++) {
            for (int j = 0; j < sss[i].length; j++) {
                System.out.print(sss[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
        System.out.println();
        System.out.println(dbm.selectRow("hossein",1));
        System.out.println(dbm.selectRow("hossein","y","like"));
        System.out.println(dbm.selectBlock("hossein",0,"id"));
    }
}
