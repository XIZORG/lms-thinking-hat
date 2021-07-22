package com.lms.hat.thinking;

import com.lms.hat.thinking.model.course.CourseRequest;
import com.lms.hat.thinking.model.course.CourseResponse;
import com.lms.hat.thinking.model.material.MaterialRequest;
import com.lms.hat.thinking.model.material.MaterialResponse;
import com.lms.hat.thinking.model.response.DefaultResponse;
import com.lms.hat.thinking.model.response.JwtResponse;
import com.lms.hat.thinking.model.skill.SkillRequest;
import com.lms.hat.thinking.model.skill.SkillResponse;
import com.lms.hat.thinking.model.task.TaskRequest;
import com.lms.hat.thinking.model.user.AuthRequest;
import com.lms.hat.thinking.model.user.RegistrationRequest;
import com.lms.hat.thinking.model.user.RoleEntity;
import com.lms.hat.thinking.model.user.UserResponse;
import com.lms.hat.thinking.repository.RoleEntityRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import javax.annotation.PostConstruct;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles({"h2db", "debug"})
class ThinkingApplicationTests {

    @LocalServerPort
    private int port;

    private static String token;

    @Autowired
    private TestRestTemplate rest;

    @Autowired
    private RoleEntityRepository roleEntityRepository;

    @PostConstruct
    void initEntities(){
        RoleEntity roleEntity1 = new RoleEntity();
        roleEntity1.setName("ROLE_USER");
        roleEntity1.setId(1L);

        RoleEntity roleEntity2 = new RoleEntity();
        roleEntity2.setName("ROLE_ADMIN");
        roleEntity2.setId(2L);

        roleEntityRepository.save(roleEntity1);
        roleEntityRepository.save(roleEntity2);
    }

    @BeforeAll
    public void setUp() {
        String login = "login2";
        String password = "password2";

        String url = "http://localhost:" + port + "/register";
        RegistrationRequest requestBody = new RegistrationRequest();
        requestBody.setLogin(login);
        requestBody.setPassword(password);
        requestBody.setEmail("milo22@gmail.com");

        ResponseEntity<UserResponse> responseEntity = rest.postForEntity(url, requestBody, UserResponse.class);
        UserResponse userResponse = responseEntity.getBody();

        assertEquals(userResponse.getLogin(),requestBody.getLogin());
        assertEquals(userResponse.getId(), 1L);
        assertEquals(userResponse.getEmail(), requestBody.getEmail());
        url = "http://localhost:" + port + "/auth";

        AuthRequest loginRequest = new AuthRequest();
        loginRequest.setLogin(login);
        loginRequest.setPassword(password);

        ResponseEntity<JwtResponse> authResponse = rest.postForEntity(url, loginRequest, JwtResponse.class);
        assertNotNull(authResponse.getBody().getAccessToken());
        token = authResponse.getBody().getAccessToken();
    }

    @Test
    void testContextLoads() {
        assertNotNull(rest);
        assertNotEquals(0, port);
    }
    
    @Test
    public void testGetUser(){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);

        String url = "http://localhost:" + port + "/api/users/1";
        ResponseEntity<UserResponse> userInfo = rest.exchange(url,
                HttpMethod.GET, new HttpEntity<>(headers), UserResponse.class);
        UserResponse userResponse = userInfo.getBody();
        assertEquals(userResponse.getId(), 1L);
        assertEquals(userResponse.getEmail(), "milo22@gmail.com");
        assertEquals(userResponse.getLogin(), "login2");
    }

    @Test
    public void testMaterial(){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);
        String url = "http://localhost:" + port + "/api/material";

        MaterialRequest materialRequest = new MaterialRequest();
        materialRequest.setName("learn programming in 10 minutes");
        materialRequest.setLink("/http:some-bla/prog");
        HttpEntity<MaterialRequest> entityReq = new HttpEntity<MaterialRequest>(materialRequest, headers);

        ResponseEntity<MaterialResponse> responseEntity = rest.exchange(url,
                HttpMethod.POST, entityReq, MaterialResponse.class);
        MaterialResponse materialResponse = responseEntity.getBody();
        assertEquals(materialResponse.getName(), materialRequest.getName());
        assertEquals(materialResponse.getLink(), materialRequest.getLink());

        url = "http://localhost:" + port + "/api/material/learn programming in 10 minutes";
        ResponseEntity<MaterialResponse> responseMaterial = rest.exchange(url,
                HttpMethod.GET, new HttpEntity<>(headers), MaterialResponse.class);
        materialResponse = responseMaterial.getBody();
        assertEquals(materialResponse.getName(), materialRequest.getName());
        assertEquals(materialResponse.getLink(), materialRequest.getLink());
    }

    @Test
    public void testSkill(){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);
        String url = "http://localhost:" + port + "/api/skill";

        SkillRequest skillRequest = new SkillRequest();
        skillRequest.setImageSource("source");
        skillRequest.setSkillName("power");

        HttpEntity<SkillRequest> entityReq = new HttpEntity<SkillRequest>(skillRequest, headers);

        ResponseEntity<SkillResponse> responseEntity = rest.exchange(url,
                HttpMethod.POST, entityReq, SkillResponse.class);
        SkillResponse skillResponse = responseEntity.getBody();
        assertEquals(skillResponse.getName(), skillRequest.getSkillName());

        url = "http://localhost:" + port + "/api/skill/power";
        ResponseEntity<SkillResponse> responseSkill = rest.exchange(url,
                HttpMethod.GET, new HttpEntity<>(headers), SkillResponse.class);
        skillResponse = responseSkill.getBody();
        assertEquals(skillResponse.getName(), skillRequest.getSkillName());
    }

    @Test
    public void testCourseLifecycle(){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);
        String url = "http://localhost:" + port + "/api/courses";

        CourseRequest courseRequest = new CourseRequest();
        courseRequest.setDescription("War is Peace; Freedom is Slavery; Ignorance is Strength");
        courseRequest.setEndDate("2021-07-16T19:13:13.328Z");
        courseRequest.setName("big brother");
        courseRequest.setStartDate("2021-06-16T19:13:13.328Z");
        courseRequest.setPassingScore("5");

        HttpEntity<CourseRequest> entityReq = new HttpEntity<>(courseRequest, headers);
        ResponseEntity<CourseResponse> responseEntity = rest.exchange(url,
                HttpMethod.POST, entityReq, CourseResponse.class);
        assertNotNull(responseEntity.getBody());
        CourseResponse courseResponse = responseEntity.getBody();
        assertEquals(courseResponse.getDescription(), courseRequest.getDescription());
        assertEquals(courseResponse.getName(), courseRequest.getName());
        assertEquals(courseResponse.getStartDate().toString(), courseRequest.getStartDate());

        url = "http://localhost:" + port + "/api/courses/1";
        responseEntity = rest.exchange(url,
                HttpMethod.GET, new HttpEntity<>(headers), CourseResponse.class);
        courseResponse = responseEntity.getBody();
        assertEquals(courseResponse.getDescription(), courseRequest.getDescription());
        assertEquals(courseResponse.getName(), courseRequest.getName());
        assertEquals(courseResponse.getStartDate().toString(), courseRequest.getStartDate());

        url = "http://localhost:" + port + "/api/courses/1/task";
        TaskRequest taskRequest = new TaskRequest();
        List<String> choices = List.of("first", "second");
        taskRequest.setChoices(choices);
        taskRequest.setName("task1");
        taskRequest.setDescription("some test task for test course");

        List<TaskRequest> taskRequests = List.of(taskRequest);

        HttpEntity<List<TaskRequest>> taskReq = new HttpEntity<>(taskRequests, headers);
        ResponseEntity<DefaultResponse> responseTask = rest.exchange(url,
                HttpMethod.POST, taskReq, DefaultResponse.class);
        DefaultResponse taskResponse = responseTask.getBody();
        System.out.println(responseTask);
        assertNotNull(taskResponse);
    }

    @Test
    public void testSecurity(){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + null);

        String url = "http://localhost:" + port + "/api/skill/text";
        ResponseEntity<SkillResponse> responseSkill = rest.exchange(url,
                HttpMethod.GET, new HttpEntity<>(headers), SkillResponse.class);
        assertEquals(responseSkill.getStatusCode(), HttpStatus.UNAUTHORIZED);

        headers.remove("Authorization");
        headers.add("Authorization", "Bearer " + token);
        responseSkill = rest.exchange(url,
                HttpMethod.GET, new HttpEntity<>(headers), SkillResponse.class);
        assertEquals(responseSkill.getStatusCode(), HttpStatus.CONFLICT);
    }
}
