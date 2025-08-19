package Models;

import org.bson.Document;

public class Task {
    private String id;
    private String title;
    private String description;
    private String assignedStaffId;
    private String status; // Example: "Pending", "In Progress", "Completed"

    public Task(String id, String title, String description, String assignedStaffId, String status) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.assignedStaffId = assignedStaffId;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAssignedStaffId() {
        return assignedStaffId;
    }

    public void setAssignedStaffId(String assignedStaffId) {
        this.assignedStaffId = assignedStaffId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // Mapping for MongoDB
    public static Task fromDocument(Document document) {
        return new Task(
                document.getString("_id"),
                document.getString("title"),
                document.getString("description"),
                document.getString("assignedStaffId"),
                document.getString("status")
        );
    }

    public static Document toDocument(Task task) {
        return new Document("title", task.getTitle())
                .append("description", task.getDescription())
                .append("assignedStaffId", task.getAssignedStaffId())
                .append("status", task.getStatus());
    }
}
