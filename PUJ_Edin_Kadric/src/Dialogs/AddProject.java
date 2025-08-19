package Dialogs;

import DataBase.CrudOperations;
import DataBase.MongoDbConnection;
import Forms.Pocetna;
import Models.Project;
import Models.Staff;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class AddProject extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField txtName;
    private JTextField txtOpis;
    private JComboBox comboBox1;
    private Staff trenutniMenager;
    private CrudOperations operations = new CrudOperations(new MongoDbConnection().getDatabase());

    public AddProject(Project project) {

        List<Staff> menagers = operations.getAllStaff().stream()
                .filter(x->x.getRole().equals("Menager")).toList();

        for(var item : menagers){
            comboBox1.addItem(item.getEmail());
        }

        if(project!=null){
            txtName.setText(project.getName());
            txtOpis.setText(project.getDescription());
            for(var item : menagers){
                if(item.getId().equals(project.getManagerId())){
                    comboBox1.setSelectedIndex(menagers.indexOf(item));
                    trenutniMenager=item;
                }
            }
        }

        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Staff menager = null;

                for (Staff manager : menagers) {
                    if (manager.getEmail().equals(comboBox1.getSelectedItem().toString())) {
                        menager = manager;  // Return the manager's ID if email matches
                    }
                }

                if(project==null) {
                    operations.createProject(new Project(
                            null,
                            txtName.getText(),
                            txtOpis.getText(),
                            menager.getId(),
                            null));

                    if(menager.getProjectIds()!=null) {
                        menager.getProjectIds().add(operations.getAllProjects().getLast().getId());
                    }else{
                        List<String> projectIDS = new ArrayList<>();
                        projectIDS.add(operations.getAllProjects().getLast().getId());
                        menager.setProjectIds(projectIDS);
                    }
                    operations.updateStaff(menager.getId(),menager);

                    dispose();
                }else{
                    //Brisanje projekta za prenutnog menagera
                    trenutniMenager.getProjectIds().remove(project.getId());

                    //Update projekta
                    project.setName(txtName.getText());
                    project.setDescription(txtOpis.getText());
                    project.setManagerId(menager.getId());

                    //Update menagera
                    menager.getProjectIds().add(operations.getAllProjects().getLast().getId());
                    operations.updateStaff(menager.getId(),menager);

                    //Update projekta
                    operations.updateProject(project.getId(), project);
                    dispose();
                }
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
