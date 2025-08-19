package Forms;

import Models.Staff;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Optional;
import DataBase.*;

public class Prijava extends JFrame{
    public JPanel panel1;
    private JButton btnPrijava;
    private JTextField textEmail;
    private JTextField textPass;
    private JTextField textPassR;
    private JLabel lGreska;

    public Prijava() {
        setTitle("Prijava");
        setContentPane(panel1);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setSize(300,200);
        setVisible(true);
        setLocationRelativeTo(null);

        MongoDbConnection dbConnection = new MongoDbConnection();
        CrudOperations operations = new CrudOperations(dbConnection.getDatabase());

        btnPrijava.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                // Provjera lozinki
                if (!textPass.getText().equals(textPassR.getText())) {
                    lGreska.setText("Lozinke se ne poklapaju !!!");
                    textPass.setText("");
                    textPassR.setText("");
                    return;
                }

                // Get all staff and check credentials
                var allStaff = operations.getAllStaff();
                Optional<Staff> staff = allStaff.stream()
                        .filter(x -> x.getEmail().equals(textEmail.getText()) &&
                                x.getPassword().equals(textPass.getText()))
                        .findFirst();

                if (staff.isEmpty()) {
                    lGreska.setText("User sa " + textEmail.getText() + " nije pronađen !!!");
                    return;
                }

                //Pokretanje pocetne
                SwingUtilities.invokeLater(() -> {
                    new Pocetna(staff.orElse(null));
                });

                dispose();
            }
        });
    }
}
