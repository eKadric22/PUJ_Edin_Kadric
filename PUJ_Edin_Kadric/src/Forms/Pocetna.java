package Forms;

import DataBase.CrudOperations;
import DataBase.MongoDbConnection;
import Dialogs.AddProject;
import Dialogs.AddTask;
import Models.Project;
import Models.Staff;
import Models.Task;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class Pocetna extends JFrame {
    public JPanel panel1;
    private JLabel lEmail;
    private JLabel lRole;
    public JTree tProjects;
    private JTable table1;
    private JButton bAddProject;
    private JButton btnUpdateProject;
    private JButton izbrisiProjekatButton;
    private JButton btnStaff;
    private JButton btnCreateTask;
    private JButton btnDeleteTask;
    private CrudOperations operations = new CrudOperations(new MongoDbConnection().getDatabase());
    private String selectedProject;
    public Staff staff;
    public Pocetna(Staff staff){

        if(staff==null) { return; }

        switch (staff.getRole()){
            case "Menager":
                bAddProject.setVisible(false);
                btnStaff.setVisible(false);
                btnUpdateProject.setVisible(false);
                izbrisiProjekatButton.setVisible(false);
                break;
            case "Staff":
                bAddProject.setVisible(false);
                btnStaff.setVisible(false);
                btnUpdateProject.setVisible(false);
                izbrisiProjekatButton.setVisible(false);
                btnCreateTask.setVisible(false);
                btnDeleteTask.setVisible(false);
                break;
            case "SuperAdmin":
                break;
        }

        this.staff = staff;

        setTitle("Staff Project Menagment System");
        setContentPane(panel1);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setSize(1000,500);
        setVisible(true);
        setLocationRelativeTo(null);

        lEmail.setText(staff.getEmail());
        lRole.setText(staff.getRole());

        fillProjectTree(tProjects,staff.getProjectIds());

        tProjects.addTreeSelectionListener(e->{

            DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tProjects.getLastSelectedPathComponent();
            if (selectedNode == null || selectedNode.isRoot()) {
                return;
            }

            selectedProject = selectedNode.getUserObject().toString();
            System.out.println("Selected Project: " + selectedProject);

            // Find the corresponding project ID (from your list or database)
            Project project = operations.getAllProjects().stream()
                    .filter(p -> p.getName().equals(selectedProject))
                    .findFirst()
                    .orElse(null);

            if (project != null) {
                fillTaskTable(table1, project.getTaskIds());
            }

        });

        bAddProject.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddProject dialog = new AddProject(null);
                dialog.pack();
                dialog.setVisible(true);
                dialog.setLocationRelativeTo(null);
            }
        });

        btnStaff.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(() -> {
                    new StaffControl();
                });
            }
        });

        btnUpdateProject.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                Project project = operations.getAllProjects().stream()
                        .filter(p -> p.getName().equals(selectedProject))
                        .findFirst()
                        .orElse(null);

                AddProject dialog = new AddProject(project);
                dialog.pack();
                dialog.setVisible(true);
                dialog.setLocationRelativeTo(null);
            }
        });
        izbrisiProjekatButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Project project = operations.getAllProjects().stream()
                        .filter(p -> p.getName().equals(selectedProject))
                        .findFirst()
                        .orElse(null);

                operations.deleteProject(project.getId());

                DefaultTreeModel model = (DefaultTreeModel) tProjects.getModel(); // Get the tree model
                TreePath selectedPath = tProjects.getSelectionPath();

                if (selectedPath != null) {
                    DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) selectedPath.getLastPathComponent();
                    model.removeNodeFromParent(selectedNode); // Remove the selected node
                }
            }
        });
        btnCreateTask.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                Project project = operations.getAllProjects().stream()
                        .filter(p -> p.getName().equals(selectedProject))
                        .findFirst()
                        .orElse(null);

                AddTask dialog = new AddTask(project);
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
                String Description = (String)model.getValueAt(row,2);
                String Staff = (String)model.getValueAt(row,3);
                String Status = (String)model.getValueAt(row,4);

                operations.updateTask(Id, new Task(null,Name,Description,Staff,Status));
            }
        });
        btnDeleteTask.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = table1.getSelectedRow();
                String Id = (String)table1.getValueAt(row,0);
                operations.deleteTask(Id);
            }
        });
    }

    public void fillProjectTree(JTree projectTree, List<String> projectsIds) {

        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Projects");

        for(var projectId : projectsIds){
            Project project = operations.getProjectById(projectId);
            DefaultMutableTreeNode projectNode = new DefaultMutableTreeNode(project.getName());
            root.add(projectNode);
        }

        // Update tree model
        DefaultTreeModel model = new DefaultTreeModel(root);
        projectTree.setModel(model);
    }

    public void fillTaskTable(JTable taskTable, List<String> taskIds) {

        if(taskIds==null || taskIds.isEmpty()) {
            String[] columnNames = {"ID", "Naziv", "Opis", "Dodjeljeni radnik", "Status"};
            DefaultTableModel model = new DefaultTableModel(columnNames, 0); // Clear existing rows

            taskTable.setModel(model);
        }else{
            String[] columnNames = {"ID", "Naziv", "Opis", "Dodjeljeni radnik", "Status"};
            DefaultTableModel model = new DefaultTableModel(columnNames, 0);

            for(var item : taskIds){

                Task task = operations.getTaskById(item);

                if(task!=null) {
                    Staff staff = operations.getStaffById(task.getAssignedStaffId());

                    Object[] row = {
                            task.getId(),
                            task.getTitle(),
                            task.getDescription(),
                            staff != null ? staff.getEmail() : null,
                            task.getStatus()
                    };
                    model.addRow(row);
                }else{
                    Project project = operations.getAllProjects().stream()
                            .filter(p -> p.getName().equals(selectedProject))
                            .findFirst()
                            .orElse(null);

                    project.getTaskIds().remove(item);

                    operations.updateProject(project.getId(),project);
                }
            }

            taskTable.setModel(model);
        }

    }
}
