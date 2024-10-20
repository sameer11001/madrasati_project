package com.webapp.madrasati.school.controller;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/v1/schoolTest")
@RestController
@AllArgsConstructor
@Profile("dev")
public class SchoolTestController {

}
