package com.github.gerdanyJr.weekit.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.gerdanyJr.weekit.model.entities.Course;
import com.github.gerdanyJr.weekit.model.exceptions.NotFoundException;
import com.github.gerdanyJr.weekit.model.req.CreateCourseReq;
import com.github.gerdanyJr.weekit.service.CourseService;

@WebMvcTest(CourseController.class)
public class CourseControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CourseService courseService;

    private CreateCourseReq req;

    private CreateCourseReq invalid;

    private Course course;

    private List<Course> courses;

    @BeforeEach
    void setup() {
        req = new CreateCourseReq("Nome Teste", "Descrição Teste");

        invalid = new CreateCourseReq("", "");

        course = new Course(1L, "Nome Teste", "Descricao teste", List.of());

        courses = List.of(course, course);
    }

    @Test
    void givenValidCreateCourseReq_whenCreateCourse_thenReturn201() throws Exception {
        when(courseService.createCourse(any(CreateCourseReq.class)))
                .thenReturn(course);

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders
                .post("/minicursos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)));

        response
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    void givenInvalidCreateCourseReq_whenCreateCourse_thenReturn400() throws Exception {
        when(courseService.createCourse(any(CreateCourseReq.class)))
                .thenReturn(course);

        ResultActions response = mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/minicursos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)));

        response
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("400"));
    }

    @Test
    void whenFindAll_thenReturn200() throws Exception {
        when(courseService.findAll())
                .thenReturn(courses);

        ResultActions response = mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/minicursos")
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8"));

        response
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers
                        .content()
                        .string(objectMapper
                                .writeValueAsString(courses)));
    }

    @Test
    void givenValidId_whenFindById_thenReturn200() throws Exception {
        when(courseService.findById(anyLong()))
                .thenReturn(course);

        ResultActions response = mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/minicursos/1")
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8"));

        response
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers
                        .content()
                        .string(objectMapper
                                .writeValueAsString(course)));
    }

    @Test
    void givenInvalidId_whenFindById_thenReturn404() throws Exception {
        when(courseService.findById(anyLong()))
                .thenThrow(NotFoundException.class);

        ResultActions response = mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/minicursos/1")
                        .accept(MediaType.APPLICATION_JSON));

        response
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status")
                        .value("NOT_FOUND"));
    }

    @Test
    void givenValidName_whenSearchByName_thenReturn200() throws Exception {
        when(courseService.searchByName(any(String.class)))
                .thenReturn(courses);

        ResultActions response = mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/minicursos/search")
                        .param("name", "Nome Teste")
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8"));

        response
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers
                        .content()
                        .string(objectMapper
                                .writeValueAsString(courses)));
    }

    @Test
    void givenInvalidName_whenSearchByName_thenReturnEmptyList() throws Exception {
        when(courseService.searchByName(any(String.class)))
                .thenReturn(List.of());

        ResultActions response = mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/minicursos/search")
                        .param("name", "Nome Inexistente")
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8"));

        response
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers
                        .content()
                        .string(objectMapper
                                .writeValueAsString(List.of())));
    }

    @Test
    void givenValidId_whenDeleteCourse_thenReturn204() throws Exception {
        ResultActions response = mockMvc
                .perform(MockMvcRequestBuilders
                        .delete("/minicursos/1")
                        .accept(MediaType.APPLICATION_JSON));

        response
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    void givenInvalidId_whenDeleteCourse_thenReturn404() throws Exception {
        doThrow(NotFoundException.class).when(courseService).delete(anyLong());

        ResultActions response = mockMvc
                .perform(MockMvcRequestBuilders
                        .delete("/minicursos/99")
                        .accept(MediaType.APPLICATION_JSON));

        response
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("NOT_FOUND"));
    }

    @Test
    void givenValidIdAndRequest_whenUpdateCourse_thenReturn200() throws Exception {
        when(courseService.update(anyLong(), any(CreateCourseReq.class)))
                .thenReturn(course);

        ResultActions response = mockMvc
                .perform(MockMvcRequestBuilders
                        .patch("/minicursos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)));

        response
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers
                        .content()
                        .string(objectMapper.writeValueAsString(course)));
    }

    @Test
    void givenInvalidId_whenUpdateCourse_thenReturn404() throws Exception {
        when(courseService.update(anyLong(), any(CreateCourseReq.class)))
                .thenThrow(NotFoundException.class);

        ResultActions response = mockMvc
                .perform(MockMvcRequestBuilders
                        .patch("/minicursos/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)));

        response
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("NOT_FOUND"));
    }

    @Test
    void givenInvalidRequest_whenUpdateCourse_thenReturn400() throws Exception {
        ResultActions response = mockMvc
                .perform(MockMvcRequestBuilders
                        .patch("/minicursos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)));

        response
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("400"));
    }

}
