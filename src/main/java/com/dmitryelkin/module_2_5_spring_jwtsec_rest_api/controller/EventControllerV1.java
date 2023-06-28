package com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.controller;

import com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.DTO.EventDTO;
import com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.model.Event;
import com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.service.EventServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/events/")
public class EventControllerV1 {
    private final EventServiceI service;

    @Autowired
    public EventControllerV1(EventServiceI service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<EventDTO>> getAll() {
        List<Event> items = service.getAll();
        if ((items != null) && (!items.isEmpty())) {
//            List<EventDTO> body = items.stream()
//                    .map(e -> new EventDTO(e))
//                    .toList();
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body( items.stream()
                            .map(EventDTO::new)
                            .toList() );
        } else {
            return ResponseEntity
                    .status(HttpStatus.NO_CONTENT)
                    .build();

        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventDTO> getById(@PathVariable Long id) {
        Event item;
        if ((id != null) && ((item = service.getById(id)) != null)) {
            EventDTO dto = new EventDTO(item);
            return ResponseEntity.ok(dto);
        } else {
            return ResponseEntity
                    .noContent()
                    .build();
        }
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Event> createItem(@RequestBody Event item) {
        if (item != null) {
            Event newItem = service.create(item);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(newItem);
        } else {
            return ResponseEntity
                    .noContent()
                    .build();
        }
    }

    @PutMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> update(@RequestBody Event item) {
        if (item != null && item.getId() != 0) {
            Event updatingItem = service.update(item);
            if (updatingItem != null) {
                return ResponseEntity
                        .status(HttpStatus.OK)
                        .body(updatingItem);
            } else {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("No such item for update");
            }
        }else {
            return ResponseEntity
                    .status(HttpStatus.NO_CONTENT)
                    .build();
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        if (id != null) {

            Event deletingItem = service.delete(id);

            if (deletingItem != null) {
                return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(deletingItem);
            }else {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("No such item for deleting");
            }
        }else{
            return ResponseEntity
                    .status(HttpStatus.NO_CONTENT)
                    .build();
        }
    }


}
