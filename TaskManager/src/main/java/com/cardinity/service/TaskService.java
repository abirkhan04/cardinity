package com.cardinity.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.MutableSortDefinition;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.beans.support.PropertyComparator;
import org.springframework.stereotype.Service;

import com.cardinity.pojo.Task;
import com.cardinity.pojo.User;
import com.cardinity.repositories.TaskRepository;
import com.cardinity.repositories.UserRepository;

@Service
public class TaskService {

	@Autowired
	private TaskRepository<Task> taskRepository;
	
	@Autowired
	private UserRepository<User> userRepository;

	public Task save(Task task) {
		return taskRepository.save(task);
	}

	public Task delete(Long id) {
		Optional<Task> taskOptional = taskRepository.findById(id);
		taskRepository.deleteById(id);
		return taskOptional.get();
	}

	public PagedListHolder<Task> getPagedTasks(String username, Integer pageNumber, Integer pageSize, Optional<String> search,
			Optional<String> sortBy) {
		List<Task> tasks = null;
		User user = null;
		if(username!=null) user = userRepository.findByUsername(username); 
		PagedListHolder<Task> page = new PagedListHolder<Task>();
		page.setPageSize(pageSize);
		if(user!=null) tasks = (List<Task>) taskRepository.findByUser(user);
		else tasks = (List<Task>) taskRepository.findAll();
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

}
