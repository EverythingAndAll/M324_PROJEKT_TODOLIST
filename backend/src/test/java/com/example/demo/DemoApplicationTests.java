package com.example.demo;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class DemoApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskService taskService;

    @BeforeEach
    void setup() {
        taskService.clearAll();
    }

    @Test
    void contextLoads() {
        assertNotNull(taskService, "TaskService sollte korrekt injiziert werden.");
    }

    @Test
    void taskListStartsEmpty() throws Exception {
        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void createTaskAddsItemToTaskList() throws Exception {
        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"taskdescription\":\"Milch kaufen\",\"priority\":\"HIGH\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.taskdescription").value("Milch kaufen"))
                .andExpect(jsonPath("$.priority").value("HIGH"))
                .andExpect(jsonPath("$.completed").value(false));

        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].taskdescription").value("Milch kaufen"));
    }

    @Test
    void taskListContainsAllExpectedFieldsAfterLoad() throws Exception {
        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"taskdescription\":\"Bericht schreiben\",\"priority\":\"LOW\",\"dueDate\":\"2026-04-10\"}"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].taskdescription").value("Bericht schreiben"))
                .andExpect(jsonPath("$[0].priority").value("LOW"))
                .andExpect(jsonPath("$[0].dueDate").value("2026-04-10"))
                .andExpect(jsonPath("$[0].creationDate").isNotEmpty())
                .andExpect(jsonPath("$[0].completed").value(false));
    }

    @Test
    void completedButtonMarksTaskAsDone() throws Exception {
        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"taskdescription\":\"Pruefung lernen\",\"priority\":\"MEDIUM\"}"))
                .andExpect(status().isOk());

        mockMvc.perform(put("/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"taskdescription\":\"Pruefung lernen\",\"priority\":\"MEDIUM\",\"completed\":true}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.completed").value(true));

        mockMvc.perform(get("/tasks?completed=true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].taskdescription").value("Pruefung lernen"));
    }

    @Test
    void deleteTaskRemovesTaskFromList() throws Exception {
        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"taskdescription\":\"Altlast entfernen\",\"priority\":\"LOW\"}"))
                .andExpect(status().isOk());

        mockMvc.perform(delete("/tasks/1"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void emptyDescriptionsAreRejectedWithErrorMessage() throws Exception {
        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"taskdescription\":\"   \",\"priority\":\"LOW\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Die Aufgabenbeschreibung darf nicht leer sein."));
    }

    @Test
    void duplicateDescriptionsAreRejectedWithErrorMessage() throws Exception {
        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"taskdescription\":\"Doppelt\",\"priority\":\"LOW\"}"))
                .andExpect(status().isOk());

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"taskdescription\":\"doppelt\",\"priority\":\"HIGH\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Eine Aufgabe mit derselben Beschreibung existiert bereits."));
    }

    @Test
    void tasksCanBeFilteredByPriorityAndSearchText() throws Exception {
        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"taskdescription\":\"M324 Dokumentation\",\"priority\":\"HIGH\"}"))
                .andExpect(status().isOk());

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"taskdescription\":\"Einkaufen\",\"priority\":\"LOW\"}"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/tasks?priority=HIGH&search=M324"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].taskdescription").value("M324 Dokumentation"));
    }

    @Test
    void invalidUpdatesReturnNotFoundForMissingTask() throws Exception {
        mockMvc.perform(put("/tasks/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"taskdescription\":\"Existiert nicht\",\"priority\":\"LOW\",\"completed\":true}"))
                .andExpect(status().isNotFound());
    }

    @Test
    void clearEndpointEmptiesListAndResetsIds() throws Exception {
        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"taskdescription\":\"Task A\",\"priority\":\"LOW\"}"))
                .andExpect(status().isOk());

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"taskdescription\":\"Task B\",\"priority\":\"HIGH\"}"))
                .andExpect(status().isOk());

        mockMvc.perform(post("/clear"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"taskdescription\":\"Neu nach Reset\",\"priority\":\"MEDIUM\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void defaultPriorityFallsBackToMedium() throws Exception {
        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"taskdescription\":\"Ohne Prioritaet\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.priority").value("MEDIUM"));
    }
}
