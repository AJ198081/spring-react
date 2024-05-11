package dev.aj.springreact.controllers;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StudentRestController.class)
@AutoConfigureMockMvc
class StudentRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getAllStudents_returnsListOfStudents() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/all")
                                              .accept(MediaType.APPLICATION_JSON_VALUE))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$", Matchers.hasSize(4)))
               .andExpect(jsonPath("$[0].name", Matchers.is("Zoravar")))
               .andExpect(jsonPath("$[0].age", Matchers.is(1)))
               .andExpect(jsonPath("$[0].grade", Matchers.is("Toddler")))
               .andExpect(jsonPath("$[1].name", Matchers.is("Reet")))
               .andExpect(jsonPath("$[1].age", Matchers.is(9)))
               .andExpect(jsonPath("$[1].grade", Matchers.is("4th")))
               .andExpect(jsonPath("$[2].name", Matchers.is("Dippi")))
               .andExpect(jsonPath("$[2].age", Matchers.is(42)))
               .andExpect(jsonPath("$[2].grade", Matchers.is("Nurse")))
               .andExpect(jsonPath("$[3].name", Matchers.is("AJ")))
               .andExpect(jsonPath("$[3].age", Matchers.is(44)))
               .andExpect(jsonPath("$[3].grade", Matchers.is("Masters")));
    }

    @Test
    void hello_returnsStudentAJ() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/default-student")
                                              .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$.name", Matchers.is("AJ")))
               .andExpect(jsonPath("$.age", Matchers.is(44)))
               .andExpect(jsonPath("$.grade", Matchers.is("Masters")));
    }

    @Test
    void getStudentById_returnsStudentForId() throws Exception {
        int id = 5;
        mockMvc.perform(MockMvcRequestBuilders.get("/api/student/" + id)
                                              .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$.name", Matchers.is("Student " + id)))
               .andExpect(jsonPath("$.age", Matchers.is(id + 40)))
               .andExpect(jsonPath("$.grade", Matchers.is("Graduate")));
    }

    @Test
    void getStudentByDetails_returnsStudentWithDetails() throws Exception {
        String name = "Kaka";
        String grade = "Driver";
        int age = 42;

        mockMvc.perform(MockMvcRequestBuilders.get("/api/student/{name}/{age}/{grade}", name, age, grade)
                                              .accept(MediaType.APPLICATION_JSON))
               .andExpect(MockMvcResultMatchers.status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$.name", Matchers.is(name)))
               .andExpect(jsonPath("$.age", Matchers.is(age)))
               .andExpect(jsonPath("$.grade", Matchers.is(grade)));
    }
}
