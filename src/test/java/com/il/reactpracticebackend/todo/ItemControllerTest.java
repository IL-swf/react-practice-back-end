package com.il.reactpracticebackend.todo;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ItemControllerTest {

    @Autowired
    ItemRepository repository;
    @Autowired
    MockMvc mvc;

    Item item0 = new Item("Feed cats");
    Item item1 = new Item("Pet cats");
    Item item2 = new Item("Love cats");
    Item item3 = new Item("Sell cats");

    ObjectMapper mapper = new ObjectMapper();


    @BeforeAll
    void init() {
        repository.saveAll(Arrays.asList(item0, item1, item2));
    }

    @Test
    @Transactional
    @Rollback
    void getAllItems() throws Exception {
        mvc.perform(get("/api/items"))
                .andExpect(jsonPath("$[0].id", is(item0.getId().intValue())))
                .andExpect(jsonPath("$[1].id", is(item1.getId().intValue())))
                .andExpect(jsonPath("$[2].id", is(item2.getId().intValue())))
                .andExpect(jsonPath("$[0].content", is(item0.getContent())))
                .andExpect(jsonPath("$[1].content", is(item1.getContent())))
                .andExpect(jsonPath("$[2].content", is(item2.getContent())))
                .andExpect(jsonPath("$[0].completed", is(false)))
                .andExpect(jsonPath("$[1].completed", is(false)))
                .andExpect(jsonPath("$[2].completed", is(false)));
    }

    @Test
    @Transactional
    @Rollback
    void postNewItem() throws Exception {

        mvc.perform(post("/api/items")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(item3)))
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.content", is(item3.getContent())))
                .andExpect(jsonPath("$.completed", is(false)));


    }

    @Test
    @Transactional
    @Rollback
    void patchUpdateItem() throws Exception {

        mvc.perform(patch("/api/items/{id}", item0.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"content": "Buy Dogs"}
                        """ ))
                .andExpect(jsonPath("$.id", is(item0.getId().intValue())))
                .andExpect(jsonPath("$.content", is("Buy Dogs")))
                .andExpect(jsonPath("$.completed", is(false)));

        mvc.perform(patch("/api/items/{id}", item1.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {"completed": true}
                        """ ))
                .andExpect(jsonPath("$.id", is(item1.getId().intValue())))
                .andExpect(jsonPath("$.content", is(item1.getContent())))
                .andExpect(jsonPath("$.completed", is(true)));
    }

    @Test
    @Transactional
    @Rollback
    void deleteItem() throws Exception {
        mvc.perform(delete("/api/items/{id}", item0.getId()))
                .andExpect(status().isOk());
        mvc.perform(get("/api/items"))
                .andExpect(jsonPath("$[0].id", is(Math.toIntExact(item1.getId()))));
    }
}