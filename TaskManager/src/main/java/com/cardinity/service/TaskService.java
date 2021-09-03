package com.cardinity.service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.MutableSortDefinition;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.beans.support.PropertyComparator;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.cardinity.enums.Status;
import com.cardinity.pojo.Project;
import com.cardinity.pojo.Task;
import com.cardinity.pojo.User;
import com.cardinity.repositories.ProjectRepository;
import com.cardinity.repositories.TaskRepository;
import com.cardinity.repositories.UserRepository;
import com.cardinity.util.AppConstants;

@Service
public class TaskService {

	@Autowired
	private TaskRepository<Task> taskRepository;

	@Autowired
	private UserRepository<User> userRepository;

	@Autowired
	private ProjectRepository<Project> projectRepository;

	public Task save(Task task) {
		return taskRepository.save(checkUsersTask(task));
	}

	public Task delete(Long id) {
		Optional<Task> task = taskRepository.findByUserAndId(getUser(), id);
		if (task.isEmpty())
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, AppConstants.NOT_FOUND);
		taskRepository.deleteById(id);
		return task.get();
	}

	public Task checkTaskisNotClosed(Task task) {
		if (task.getId() != null)
			if (taskRepository.findById(task.getId()).get().getStatus().equals(Status.CLOSED))
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Task is closed");
		return task;
	}

	public List<Task> getTaskByProject(Long projectId) {
		return taskRepository.findByUserAndProject(getUser(), projectRepository.findById(projectId).get());
	}

	public List<Task> getTaskByStatus(Status status) {
		return taskRepository.findByUserAndStatus(getUser(), status);
	}

	public List<Task> getExpiredTask(String dateString) {
		return taskRepository.findExpiredTasks(dateStringToUtilDate(dateString), getUser().getId());
	}

	public List<Task> getTasksForUser(String username) {
		return taskRepository.findByUser(userRepository.findByUsername(username));
	}

	public List<Task> getTasks(Long projectId, Status status, String date) {
		return taskRepository.findByParams(projectId, status, dateStringToUtilDate(date), getUser().getId());
	}

	public Task getTask(Long id) {
		Optional<Task> task = taskRepository.findById(id);
		if (task.isEmpty())
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, AppConstants.NOT_FOUND);
		return taskRepository.findById(id).get();
	}

	private Date dateStringToUtilDate(String dateString) {
		LocalDate date = LocalDate.parse(dateString);
		ZoneId z = ZoneId.of("Asia/Kolkata");
		ZonedDateTime dateZ = date.atStartOfDay(z);
		return Date.from(dateZ.toInstant());
	}

	private User getUser() {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		return userRepository.findByUsername(username);
	}

	public Task addUser(Task task) {
		if (task.getUser() == null) {
			task.setUser(getUser());
		}
		return task;
	}

	private Task checkUsersTask(Task task) {
		if (task.getId() != null) {
			Optional<Task> taskOptional = taskRepository.findByUserAndId(getUser(), task.getId());
			if (taskOptional.isEmpty())
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, AppConstants.NOT_FOUND);
		}
		return task;
	}

}
