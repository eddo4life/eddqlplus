package view;

import com.formdev.flatlaf.FlatDarculaLaf;
import controller.library.EddoLibrary;
import dao.mysql.MySQLConnection;
import eddql.launch.LoadData;
import view.tools.Tools;

import javax.swing.*;

public class MainEddQL {
    private static boolean restart = false;

    public static void main(String[] args) {
        new EddoLibrary();// toInstantiateTheLibraryObject
        if (!MainEddQL.restart) {
            if (!new MySQLConnection().getDbName().isBlank()) {
                new LoadData(null);
                MainEddQL.restart = true;
            }
        }

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    //new LookAndFeel(LookAndFeel.look);
                    try {
                        //UIManager.setLookAndFeel( new FlatLightLaf() );
                        //UIManager.setLookAndFeel( new FlatDarkLaf() );
                        UIManager.setLookAndFeel(new FlatDarculaLaf());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    new Tools();
                    new Home();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }
}
