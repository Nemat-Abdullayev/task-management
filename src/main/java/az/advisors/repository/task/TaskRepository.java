package az.advisors.repository.task;

import az.advisors.model.entity.Task;
import az.advisors.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    Task save(Task task);

    List<Task> getAllByAssignedTo(User assignedTo);

    List<Task> findAll();

}
