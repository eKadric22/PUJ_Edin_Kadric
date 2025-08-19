package DataBase;

import Models.Project;
import Models.Staff;
import Models.Task;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class CrudOperations {

    private final Repository<Staff> staffRepository;
    private final Repository<Project> projectRepository;
    private final Repository<Task> taskRepository;

    public CrudOperations(MongoDatabase database) {
        this.staffRepository = new Repository<>(database, "staff", Staff::fromDocument, Staff::toDocument);
        this.projectRepository = new Repository<>(database, "projects", Project::fromDocument, Project::toDocument);
        this.taskRepository = new Repository<>(database, "tasks", Task::fromDocument, Task::toDocument);
    }

    // CRUD za Models.Staff
    public void createStaff(Staff staff) {
        staffRepository.insert(staff);
    }

    public Staff getStaffById(String id) {
        return staffRepository.findById(id);
    }

    public List<Staff> getAllStaff() {
        return staffRepository.findAll();
    }

    public void updateStaff(String id, Staff updatedStaff) {
        staffRepository.update(id, updatedStaff);
    }

    public void deleteStaff(String id) {
        staffRepository.delete(id);
    }

    // CRUD za Models.Project
    public void createProject(Project project) {

        projectRepository.insert(project);
        String idOfCreatedProject = projectRepository.findAll().getLast().getId();
        List<Staff> SuperAdmins = staffRepository.findAll().stream().filter(x->x.getRole().equals("SuperAdmin")).toList();
        for (var item : SuperAdmins){
            item.getProjectIds().add(idOfCreatedProject);
            staffRepository.update(item.getId(),item);
        }
    }

    public Project getProjectById(String id) {
        return projectRepository.findById(id);
    }

    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    public void updateProject(String id, Project updatedProject) {
        projectRepository.update(id, updatedProject);
    }

    public void deleteProject(String id) {

        projectRepository.delete(id);
        for(var item : staffRepository.findAll()){

            if(item.getProjectIds()!=null){
                item.getProjectIds().remove(id);
                staffRepository.update(item.getId(),item);
            }
        }
    }

    // CRUD za Models.Task
    public void createTask(Task task) {
        taskRepository.insert(task);
    }

    public Task getTaskById(String id) {
        return taskRepository.findById(id);
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public void updateTask(String id, Task updatedTask) {
        taskRepository.update(id, updatedTask);
    }

    public void deleteTask(String id) {
        taskRepository.delete(id);
    }

}
