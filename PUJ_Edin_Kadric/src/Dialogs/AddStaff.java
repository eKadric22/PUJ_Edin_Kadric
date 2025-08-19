package Dialogs;

import DataBase.CrudOperations;
import DataBase.MongoDbConnection;
import Forms.StaffControl;
import jdk.jshell.ImportSnippet;

import javax.imageio.plugins.jpeg.JPEGImageReadParam;
import javax.swing.*;
import java.awt.event.*;
import java.util.Collections;

import Models.Staff;

public class AddStaff extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField tIme;
    private JTextField tPass;
    private JComboBox comboBox1;
    private JTextField tEmail;
    private CrudOperations operations = new CrudOperations(new MongoDbConnection().getDatabase());

    public AddStaff(StaffControl perent) {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        comboBox1.addItem("Staff");
        comboBox1.addItem("Menager");
        comboBox1.addItem("SuperAdmin");

        //Dodavanje uposlenika
        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Staff staff = new Staff(null, tIme.getText(), tEmail.getText(), tPass.getText(), comboBox1.getSelectedItem().toString(), Collections.emptyList());
                operations.createStaff(staff);

                //Update tabele
                perent.punjenjeTabele();
                dispose();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }
}
