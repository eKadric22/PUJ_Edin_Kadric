package Models;

import org.bson.Document;

import java.util.List;

public class Staff {
    private String id;
    private String name;
    private String email;
    private String password;
    private String role;
    private List<String> projectIds;

    public Staff(String id, String name, String email, String password, String role, List<String> projectIds) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.projectIds = projectIds;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public List<String> getProjectIds() {
        return projectIds;
    }

    public void setProjectIds(List<String> projectIds) {
        this.projectIds = projectIds;
    }

    // Mapping for MongoDB
    public static Staff fromDocument(Document document) {
        return new Staff(
                document.getString("_id"),
                document.getString("name"),
                document.getString("email"),
                document.getString("password"),
                document.getString("role"),
                document.getList("projectIds", String.class));
    }

    public static Document toDocument(Staff staff) {
        return new Document("name", staff.getName())
                .append("email", staff.getEmail())
                .append("password", staff.getPassword())
                .append("role", staff.getRole())
                .append("projectIds", staff.getProjectIds());
    }

}
