package com.examly.springapp;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import java.io.File;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(classes = SpringappApplication.class)
@AutoConfigureMockMvc
class SpringappFertilizerTests {

    @Autowired
    private MockMvc mockMvc;

    // ---------- Core API Tests ----------
    @Order(1)
    @Test
    void AddFertilizerReturns200() throws Exception {
        String fertilizerData = """
                {
                    "fertilizerName": "Super Grow NPK",
                    "manufacturer": "AgroTech Industries",
                    "type": "Chemical",
                    "quantity": 50,
                    "price": 1250
                }
                """;

        mockMvc.perform(post("/api/fertilizers/addFertilizer")
                        .with(jwt())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(fertilizerData)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Order(2)
    @Test
    void GetAllFertilizersReturnsArray() throws Exception {
        mockMvc.perform(get("/api/fertilizers/allFertilizers")
                        .with(jwt())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andReturn();
    }

    @Order(3)
    @Test
    void GetFertilizerByIdReturns200() throws Exception {
        mockMvc.perform(get("/api/fertilizers/1")
                        .with(jwt())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fertilizerName").exists())
                .andReturn();
    }

    @Order(4)
    @Test
    void UpdateFertilizerReturns200() throws Exception {
        String updatedData = """
                {
                    "fertilizerName": "Super Grow NPK Premium",
                    "manufacturer": "AgroTech Industries",
                    "type": "Chemical",
                    "quantity": 75,
                    "price": 1500
                }
                """;

        mockMvc.perform(put("/api/fertilizers/1")
                        .with(jwt())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedData)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quantity").value(75))
                .andReturn();
    }

    @Order(5)
    @Test
    void DeleteFertilizerReturns200() throws Exception {
        mockMvc.perform(delete("/api/fertilizers/1")
                        .with(jwt())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
    }

    // ---------- Project Structure Tests ----------
    @Test
    void ControllerDirectoryExists() {
        String directoryPath = "src/main/java/com/examly/springapp/controller";
        File directory = new File(directoryPath);
        assertTrue(directory.exists() && directory.isDirectory());
    }

    @Test
    void FertilizerControllerFileExists() {
        String filePath = "src/main/java/com/examly/springapp/controller/FertilizerController.java";
        File file = new File(filePath);
        assertTrue(file.exists() && file.isFile());
    }

    @Test
    void ModelDirectoryExists() {
        String directoryPath = "src/main/java/com/examly/springapp/model";
        File directory = new File(directoryPath);
        assertTrue(directory.exists() && directory.isDirectory());
    }

    @Test
    void FertilizerModelFileExists() {
        String filePath = "src/main/java/com/examly/springapp/model/Fertilizer.java";
        File file = new File(filePath);
        assertTrue(file.exists() && file.isFile());
    }

    @Test
    void RepositoryDirectoryExists() {
        String directoryPath = "src/main/java/com/examly/springapp/repository";
        File directory = new File(directoryPath);
        assertTrue(directory.exists() && directory.isDirectory());
    }

    @Test
    void ServiceDirectoryExists() {
        String directoryPath = "src/main/java/com/examly/springapp/service";
        File directory = new File(directoryPath);
        assertTrue(directory.exists() && directory.isDirectory());
    }

    @Test
    void FertilizerServiceClassExists() {
        checkClassExists("com.examly.springapp.service.FertilizerService");
    }

    @Test
    void FertilizerModelClassExists() {
        checkClassExists("com.examly.springapp.model.Fertilizer");
    }

    @Test
    void FertilizerModelHasFertilizerNameField() {
        checkFieldExists("com.examly.springapp.model.Fertilizer", "fertilizerName");
    }

    @Test
    void FertilizerModelHasManufacturerField() {
        checkFieldExists("com.examly.springapp.model.Fertilizer", "manufacturer");
    }

    @Test
    void FertilizerModelHasTypeField() {
        checkFieldExists("com.examly.springapp.model.Fertilizer", "type");
    }

    @Test
    void FertilizerModelHasQuantityField() {
        checkFieldExists("com.examly.springapp.model.Fertilizer", "quantity");
    }

    @Test
    void FertilizerModelHasPriceField() {
        checkFieldExists("com.examly.springapp.model.Fertilizer", "price");
    }

    @Test
    void FertilizerRepoExtendsJpaRepository() {
        checkClassImplementsInterface("com.examly.springapp.repository.FertilizerRepository",
                "org.springframework.data.jpa.repository.JpaRepository");
    }

    @Test
    void FertilizerNotFoundExceptionClassExists() {
        checkClassExists("com.examly.springapp.exception.FertilizerNotFoundException");
    }

    @Test
    void FertilizerNotFoundExceptionExtendsRuntimeException() {
        try {
            Class<?> clazz = Class.forName("com.examly.springapp.exception.FertilizerNotFoundException");
            assertTrue(RuntimeException.class.isAssignableFrom(clazz),
                    "FertilizerNotFoundException should extend RuntimeException");
        } catch (ClassNotFoundException e) {
            fail("FertilizerNotFoundException class does not exist.");
        }
    }

    // ---------- Helpers ----------
    private void checkClassExists(String className) {
        try {
            Class.forName(className);
        } catch (ClassNotFoundException e) {
            fail("Class " + className + " does not exist.");
        }
    }

    private void checkFieldExists(String className, String fieldName) {
        try {
            Class<?> clazz = Class.forName(className);
            clazz.getDeclaredField(fieldName);
        } catch (ClassNotFoundException | NoSuchFieldException e) {
            fail("Field " + fieldName + " in class " + className + " does not exist.");
        }
    }

    private void checkClassImplementsInterface(String className, String interfaceName) {
        try {
            Class<?> clazz = Class.forName(className);
            Class<?> interfaceClazz = Class.forName(interfaceName);
            assertTrue(interfaceClazz.isAssignableFrom(clazz));
        } catch (ClassNotFoundException e) {
            fail("Class " + className + " or interface " + interfaceName + " does not exist.");
        }
    }
}