package com.cardinity;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.cardinity.pojo.Project;
import com.cardinity.service.ProjectService;

public class ProjectControllerTest extends TaskManagerApplicationTest {

	@MockBean
	private ProjectService projectService;

	private String baseUri = "/projects/project";

	@Before
	public void setUp() {
		super.setUp();
	}

	@Test
	public void getUserProjectsShouldReturnProjects() throws Exception {
		Project project1 = new Project();
		project1.setId(1L);
		Project project2 = new Project();
		project2.setId(2L);

		when(projectService.getProjects(Mockito.anyString())).thenReturn(Lists.newArrayList(project1, project2));
		String uri = "/projects/user-projects/anyuser";
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
		String content = mvcResult.getResponse().getContentAsString();
		Project[] projectList = mapFromJson(content, Project[].class);
		assertEquals(2, projectList.length);
	}

	@Test
	public void postProjectShouldReturnProject() throws Exception {
		Project project = new Project();
		project.setName("Project Name");
		when(projectService.save(Mockito.any())).thenReturn(project);
		String inputJson = mapToJson(project);
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(baseUri).accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON).content(inputJson)).andReturn();
		int status = mvcResult.getResponse().getStatus();
		Project projectSaved = mapFromJson(mvcResult.getResponse().getContentAsString(), Project.class);
		assertEquals(200, status);
		assertEquals(projectSaved.getName(), "Project Name");
	}

	@Test
	public void putProjectShouldReturnProject() throws Exception {
		Project project = new Project();
		project.setName("Project Name");
		project.setId(1L);
		when(projectService.save(Mockito.any())).thenReturn(project);
		String inputJson = mapToJson(project);
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.put(baseUri).accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON).content(inputJson)).andReturn();
		int status = mvcResult.getResponse().getStatus();
		Project projectSaved = mapFromJson(mvcResult.getResponse().getContentAsString(), Project.class);
		assertEquals(200, status);
		assertEquals(projectSaved.getName(), "Project Name");
	}

	@Test
	public void deleteProjectShouldDeleteAndReturnProject() throws Exception {
		Project project = new Project();
		project.setName("Project Name");
		project.setId(1L);
		when(projectService.delete(Mockito.anyLong())).thenReturn(project);
		MvcResult mvcResult = mockMvc
				.perform(MockMvcRequestBuilders.delete(baseUri + "/1").accept(MediaType.APPLICATION_JSON)).andReturn();
		int status = mvcResult.getResponse().getStatus();
		Project projectDeleted = mapFromJson(mvcResult.getResponse().getContentAsString(), Project.class);
		assertEquals(200, status);
		assertEquals(projectDeleted.getName(), "Project Name");
	}

}
