package Forms;

import DataBase.CrudOperations;
import DataBase.MongoDbConnection;
import Dialogs.AddStaff;
import Models.Staff;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.event.*;

public class StaffControl extends JFrame {
    private JTable table1;
    private JButton dodajUposleneButton;
    private JPanel panel1;
    private JButton btnDelete;
    private CrudOperations operations = new CrudOperations(new MongoDbConnection().getDatabase());

    public StaffControl(){

        // Set up the frame
        JFrame frame = new JFrame("Staff Table");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(panel1);
        pack();
        setSize(1000,500);
        setVisible(true);
        setLocationRelativeTo(null);

        punjenjeTabele();

        dodajUposleneButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddStaff dialog = new AddStaff(StaffControl.this);
                dialog.pack();
                dialog.setVisible(true);
                dialog.setLocationRelativeTo(null);
            }
        });


       table1.getModel().addTableModelListener(new TableModelListener() {
           @Override
           public void tableChanged(TableModelEvent e) {
               int row = e.getFirstRow();
               TableModel model = (TableModel) e.getSource();

               String Id = (String)model.getValueAt(row,0);
               String Name = (String)model.getValueAt(row,1);
               String Email = (String)model.getValueAt(row,2);
               String Role = (String)model.getValueAt(row,3);

               operations.updateStaff(Id, new Staff(null,Name,Email,null,Role,null));
           }
       });
        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = table1.getSelectedRow();
                String Id = (String)table1.getValueAt(row,0);
                operations.deleteStaff(Id);

                punjenjeTabele();
            }
        });
    }
    public static void main(String[] args) {
        // Example of creating and displaying the StaffControl JFrame
        SwingUtilities.invokeLater(() -> new StaffControl());
    }

    public void punjenjeTabele(){
        //Imena kolona za tabelu
        String[] columnNames = {"ID", "Name", "Email", "Role"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);

        //Punjenje tabele
        for (Staff staff : operations.getAllStaff()){
            Object[] row = {
                    staff.getId(),
                    staff.getName(),
                    staff.getEmail(),
                    staff.getRole()
            };
            tableModel.addRow(row);
        }

        table1.setModel(tableModel);
    }

}
