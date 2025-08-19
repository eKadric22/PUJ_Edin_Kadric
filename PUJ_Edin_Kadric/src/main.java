import DataBase.CrudOperations;
import DataBase.MongoDbConnection;
import Forms.Prijava;
import Models.Project;
import Models.Staff;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
public class main {
    public static void main(String[] args) {

        CrudOperations operations = new CrudOperations(new MongoDbConnection().getDatabase());
        operations.deleteStaff(operations.getAllStaff().getFirst().getId());
        operations.createStaff(new Staff(null,"Edin Kadric","edinkadric@gmail.com","edin","SuperAdmin",Collections.emptyList()));

        //Pokretanje prijave
        SwingUtilities.invokeLater(() -> {
            new Prijava();
        });
    }
}
