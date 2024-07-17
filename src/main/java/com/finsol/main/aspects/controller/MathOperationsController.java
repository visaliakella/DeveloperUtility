package com.finsol.main.aspects.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/math")
public class MathOperationsController {



        @PostMapping("/addition")
        public ResponseEntity<Map<String, Object>> addition(@RequestBody Map<String, String> numbers) {
            int number1 = Integer.parseInt(numbers.get("number1"));
            int number2 = Integer.parseInt(numbers.get("number2"));
            int sum = number1 + number2;

            Map<String, Object> response = new HashMap<>();
            response.put("sum", sum);
            return ResponseEntity.ok(response);
        }


    @PostMapping("/division")
    public ResponseEntity<Map<String, Object>> division(@RequestBody Map<String, String> numbers) {

            double number1 = Double.parseDouble(numbers.get("number1"));
            double number2 = Double.parseDouble(numbers.get("number2"));


            if (number2 == 0) {
                throw new ArithmeticException("Division by zero is not allowed.");
            }

            double result = number1 / number2;

            Map<String, Object> response = new HashMap<>();
            response.put("result", result);
            return ResponseEntity.ok(response);
    }
}




