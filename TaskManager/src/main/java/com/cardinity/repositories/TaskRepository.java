package com.cardinity.repositories;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import com.cardinity.enums.Status;
import com.cardinity.pojo.Project;
import com.cardinity.pojo.Task;
import com.cardinity.pojo.User;

public interface TaskRepository<T> extends PagingAndSortingRepository<Task, Long> {
	List<Task> findByUser(User user);
	List<Task> findByUserAndProject(User user, Project project);
	List<Task> findByUserAndStatus(User user, Status status);

	@Query("Select t from Task t where t.dueDate < ?1 and t.user.id=?2")
	List<Task> findExpiredTasks(Date date, Long id);

	@Query("Select t from Task t where t.project.id = ?1 and t.status = ?2 and t.dueDate < ?3 and t.user.id=?4")
	List<Task> findByParams(Long projectId, Status status, Date date, Long userId);

	Optional<Task> findByUserAndId(User user, Long id);
}
