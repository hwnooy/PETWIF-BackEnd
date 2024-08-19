package org.example.petwif.web.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
//@CrossOrigin(origins = "*", allowedHeaders = "*")
public class HelloworldController {
    @RequestMapping()
    @GetMapping()
    public String main(){
        return "Hello World";
    }
}