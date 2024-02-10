package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.TopWords;
import ru.kata.spring.boot_security.demo.service.FunctionsService;

import java.util.Map;


@RestController
@RequestMapping("/apiFunc")
public class UsersFunctionsRestController {

    @PostMapping("/processWords")
    public ResponseEntity<Map<String, Integer>> processWords(@RequestBody TopWords topWords) {
        FunctionsService funcService = new FunctionsService();
        Map<String, Integer> result = funcService.processWords(topWords.getWord(), topWords.getLimit());
        return ResponseEntity.ok(result);
    }
}
