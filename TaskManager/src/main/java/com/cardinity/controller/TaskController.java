package com.cardinity.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.cardinity.enums.Status;
import com.cardinity.pojo.Task;
import com.cardinity.service.TaskService;

@RestController
@Validated
@RequestMapping(value = "tasks")
public class TaskController {

	@Autowired
	private TaskService taskService;

	@GetMapping(value = "user-tasks/{username}")
	public List<Task> gettasksForUser(@PathVariable String username) {
		return taskService.getTasksForUser(username);
	}

	@GetMapping(value = "task/{id}")
	public Task getTask(@PathVariable String id) {
		return taskService.getTask(Long.valueOf(id));
	}

	@GetMapping(value = "tasks")
	public List<Task> getTasks(@RequestParam("project-id") String projectId, @RequestParam String status,
			@RequestParam String date) {
		return taskService.getTasks(Long.valueOf(projectId), Status.valueOf(status), date);
	}

	@GetMapping(value = "tasks/{project-id}")
	public List<Task> getTasksByProject(@PathVariable("project-id") String projectId) {
		return taskService.getTaskByProject(Long.valueOf(projectId));
	}

	@GetMapping(value = "tasks-of/{status}")
	public List<Task> getTasksByStatus(@PathVariable String status) {
		return taskService.getTaskByStatus(Status.valueOf(status));
	}

	@GetMapping(value = "expired-tasks/{date}")
	public List<Task> getTasksByDate(@PathVariable String date) {
		return taskService.getExpiredTask(date);
	}

	@PostMapping(value = "task")
	public @ResponseBody Task postTask(@Valid @RequestBody Task task) {
		return taskService.save(taskService.addUser(task));
	}

	@PutMapping(value = "task")
	public @ResponseBody Task puttask(@Valid @RequestBody Task task) throws Exception {
		return taskService.save(taskService.checkTaskisNotClosed(task));
	}

	@DeleteMapping(value = "task/{id}")
	public @ResponseBody Task deletetask(@PathVariable String id) throws Exception {
		return taskService.delete(Long.valueOf(id));
	}

}
