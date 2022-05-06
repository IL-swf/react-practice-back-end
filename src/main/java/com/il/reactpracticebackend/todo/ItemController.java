package com.il.reactpracticebackend.todo;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.HashMap;

@RestController
@CrossOrigin
@RequestMapping("/api/items")
public class ItemController {
    private final ItemRepository repository;

    public ItemController(ItemRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public ResponseEntity<Object> getAllItems() {
        return ResponseEntity.ok(repository.findAll());
    }

    @PostMapping
    public ResponseEntity<Object> postNewItem(@RequestBody Item item) {
        try {
            return ResponseEntity.ok(repository.save(item));
        } catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException(exception);
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> patchUpdateItem(@PathVariable Long id, @RequestBody HashMap<String, Object> updates) {
        Item item;

        try {
            item = repository.getById(id);
        } catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException(exception);
        } catch (EntityNotFoundException exception) {
            throw new EntityNotFoundException(exception.getMessage());
        }

        updates.forEach((key, value) -> {
            switch (key) {
                case "content" -> item.setContent((String) value);
                case "completed" -> item.setCompleted((Boolean) value);

            }
        });

        try {
            return ResponseEntity.ok(repository.save(item));
        } catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException(exception);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteItem(@PathVariable Long id) {
        try {
            repository.deleteById(id);
            return ResponseEntity.ok("Item deleted successfully");
        } catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException(exception);
        }
    }
}