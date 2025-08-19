package Dialogs;

import DataBase.CrudOperations;
import DataBase.MongoDbConnection;
import Models.Project;
import Models.Staff;
import Models.Task;

import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class AddTask extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField txtName;
    private JTextField txtOpis;
    private JComboBox comboBox1;
    private CrudOperations operations = new CrudOperations(new MongoDbConnection().getDatabase());

    public AddTask(Project project) {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        List<Staff> Staff = operations.getAllStaff().stream()
                .filter(x->x.getRole().equals("Staff")).toList();

        for(var item : Staff){
            comboBox1.addItem(item.getEmail());
        }

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                Staff staffId = null;

                for(var item : Staff){
                    if(item.getEmail().equals(comboBox1.getSelectedItem())){
                        staffId = item;
                    }
                }

                Task newTask = new Task(null, txtName.getText(),txtOpis.getText(), staffId.getId(),"Not Started");
                operations.createTask(newTask);

                newTask = operations.getAllTasks().getLast();

                //Dodavanje taska u projekat
                if(project.getTaskIds()!=null){
                    project.getTaskIds().add(newTask.getId());
                    operations.updateProject(project.getId(), project);
                }else{
                    List<String> TaskIds = new ArrayList<>();
                    TaskIds.add(newTask.getId());
                    project.setTaskIds(TaskIds);
                    operations.updateProject(project.getId(),project);
                }

                if(staffId.getProjectIds()!=null){
                    staffId.getProjectIds().add(project.getId());
                    operations.updateStaff(staffId.getId(), staffId);
                }else{
                    List<String> listString = new ArrayList<>();
                    listString.add(project.getId());
                    staffId.setProjectIds(listString);
                    operations.updateStaff(staffId.getId(),staffId);

                }


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
