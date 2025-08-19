package Models;

import org.bson.Document;

import java.util.List;

public class Project {
    private String id;
    private String name;
    private String description;
    private String managerId;
    private List<String> taskIds;

    public Project(String id, String name, String description, String managerId, List<String> taskIds) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.managerId = managerId;
        this.taskIds = taskIds;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getManagerId() {
        return managerId;
    }

    public void setManagerId(String managerId) {
        this.managerId = managerId;
    }

    public List<String> getTaskIds() {
        return taskIds;
    }

    public void setTaskIds(List<String> taskIds) {
        this.taskIds = taskIds;
    }

    // Mapping za MongoDB
    public static Project fromDocument(Document document) {
        return new Project(
                document.getString("_id"),
                document.getString("name"),
                document.getString("description"),
                document.getString("managerId"),
                document.getList("taskIds", String.class)
        );
    }

    public static Document toDocument(Project project) {
        return new Document("name", project.getName())
                .append("description", project.getDescription())
                .append("managerId", project.getManagerId())
                .append("taskIds", project.getTaskIds());
    }
}
