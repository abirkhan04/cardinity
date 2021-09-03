package com.cardinity.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.cardinity.enums.Status;
import com.cardinity.pojo.Project;
import com.cardinity.pojo.Task;
import com.cardinity.pojo.User;

public interface TaskRepository<T> extends PagingAndSortingRepository<Task, Long> {
	List<Task> findByUser(User user);

	List<Task> findByProject(Project project);

	List<Task> findByStatus(Status status);

	@Query("Select t from Task t where t.dueDate < ?1")
	List<Task> findExpiredTasks(Date date);

	@Query("Select t from Task t where t.project.id = ?1 and t.status = ?2 and t.dueDate < ?3")
	List<Task> findByParams(Long projectId, Status status, Date date);
}
