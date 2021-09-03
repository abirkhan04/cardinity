package com.cardinity.service;

import java.text.SimpleDateFormat;
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
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.cardinity.enums.Status;
import com.cardinity.pojo.Project;
import com.cardinity.pojo.Task;
import com.cardinity.pojo.User;
import com.cardinity.repositories.ProjectRepository;
import com.cardinity.repositories.TaskRepository;
import com.cardinity.repositories.UserRepository;

@Service
public class TaskService {

	@Autowired
	private TaskRepository<Task> taskRepository;

	@Autowired
	private UserRepository<User> userRepository;

	@Autowired
	private ProjectRepository<Project> projectRepository;

	public Task save(Task task) {
		return taskRepository.save(task);
	}

	public Task delete(Long id) {
		Optional<Task> taskOptional = taskRepository.findById(id);
		taskRepository.deleteById(id);
		return taskOptional.get();
	}

	public PagedListHolder<Task> getPagedTasks(String username, Integer pageNumber, Integer pageSize,
			Optional<String> search, Optional<String> sortBy) {
		List<Task> tasks = null;
		User user = null;
		if (username != null)
			user = userRepository.findByUsername(username);
		PagedListHolder<Task> page = new PagedListHolder<Task>();
		page.setPageSize(pageSize);
		if (user != null)
			tasks = (List<Task>) taskRepository.findByUser(user);
		else
			tasks = (List<Task>) taskRepository.findAll();
		if (search.isPresent() && !search.get().isEmpty()) {
			tasks = tasks.stream().filter((task) -> task.getDescription().contains(search.get()))
					.collect(Collectors.toList());
		}
		if (pageNumber * pageSize > tasks.size() + pageSize) {
			pageNumber = 0;
		}
		page.setPage(pageNumber);
		if (sortBy.isPresent()) {
			PropertyComparator.sort(tasks, new MutableSortDefinition(sortBy.get(), true, true));
		}
		page.setSource(tasks);
		return page;
	}

	public Task checkTaskisNotClosed(Task task) {
		if (task.getStatus().equals(Status.CLOSED)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Task is closed");
		}
		return task;
	}

	public List<Task> getTaskByProject(Long projectId) {
		return taskRepository.findByProject(projectRepository.findById(projectId).get());
	}

	public List<Task> getTaskByStatus(Status status) {
		return taskRepository.findByStatus(status);
	}

	public List<Task> getExpiredTask(String dateString) {
		return taskRepository.findExpiredTasks(dateStringToUtilDate(dateString));
	}

	public List<Task> getTasks(String username) {
		return taskRepository.findByUser(userRepository.findByUsername(username));
	}

	public List<Task> getTasks(Long projectId, Status status, String date) {
		return taskRepository.findByParams(projectId, status, dateStringToUtilDate(date));
	}

	private Date dateStringToUtilDate(String dateString) {
		LocalDate date = LocalDate.parse(dateString);
		ZoneId z = ZoneId.of("Asia/Kolkata");
		ZonedDateTime dateZ = date.atStartOfDay(z);
		return Date.from(dateZ.toInstant());
	}
}
