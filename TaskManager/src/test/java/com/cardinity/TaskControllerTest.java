package com.cardinity;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.LinkedMultiValueMap;

import com.cardinity.enums.Status;
import com.cardinity.pojo.Project;
import com.cardinity.pojo.Task;
import com.cardinity.service.TaskService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = TaskManagerApplication.class)
@WebAppConfiguration
public class TaskControllerTest extends TaskManagerApplicationTest {

	@MockBean
	private TaskService taskService;

	private String baseUri = "/tasks/task";

	@Before
	public void setUp() {
		super.setUp();
	}


	@Test
	public void getTasksOnSearchShouldReturnTasks() throws Exception {
		Task task1 = new Task();
		task1.setId(1L);
		Task task2 = new Task();
		task2.setId(2L);

		when(taskService.getTasks(Mockito.anyLong(), Mockito.any(), Mockito.anyString()))
				.thenReturn(Lists.newArrayList(task1, task2));
		String uri = "/tasks/tasks";
		LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
		requestParams.add("project-id", "1");
		requestParams.add("status", "OPEN");
		requestParams.add("date", "2010-01-01");

		MvcResult mvcResult = mockMvc
				.perform(MockMvcRequestBuilders.get(uri).params(requestParams).accept(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
		String content = mvcResult.getResponse().getContentAsString();
		Task[] taskList = mapFromJson(content, Task[].class);
		assertEquals(2, taskList.length);
	}

	@Test
	public void postTaskShouldReturnTask() throws Exception {
		Task task = new Task();
		task.setDescription("Task Description");
		task.setProject(new Project());
		task.setStatus(Status.OPEN);
		when(taskService.save(Mockito.any())).thenReturn(task);
		String inputJson = mapToJson(task);
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(baseUri).accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON).content(inputJson)).andReturn();
		int status = mvcResult.getResponse().getStatus();
		Task taskSaved = mapFromJson(mvcResult.getResponse().getContentAsString(), Task.class);
		assertEquals(200, status);
		assertEquals(taskSaved.getDescription(), "Task Description");
	}

	@Test
	public void putTaskShouldReturnTask() throws Exception {
		Task task = new Task();
		task.setDescription("Task Description");
		task.setId(1L);
		task.setProject(new Project());
		task.setStatus(Status.OPEN);
		when(taskService.save(Mockito.any())).thenReturn(task);
		String inputJson = mapToJson(task);
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.put(baseUri).accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON).content(inputJson)).andReturn();
		int status = mvcResult.getResponse().getStatus();
		Task taskSaved = mapFromJson(mvcResult.getResponse().getContentAsString(), Task.class);
		assertEquals(200, status);
		assertEquals(taskSaved.getDescription(), "Task Description");
	}

	@Test
	public void deleteTaskShouldDeleteAndReturnTask() throws Exception {
		Task task = new Task();
		task.setDescription("Task Description");
		task.setId(1L);
		when(taskService.delete(Mockito.anyLong())).thenReturn(task);
		MvcResult mvcResult = mockMvc
				.perform(MockMvcRequestBuilders.delete(baseUri + "/1").accept(MediaType.APPLICATION_JSON)).andReturn();
		int status = mvcResult.getResponse().getStatus();
		Task taskDeleted = mapFromJson(mvcResult.getResponse().getContentAsString(), Task.class);
		assertEquals(200, status);
		assertEquals(taskDeleted.getDescription(), "Task Description");
	}

}
