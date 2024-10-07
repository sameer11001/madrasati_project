package com.webapp.madrasati.school.controller;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/schoolTest/v1/")
@RestController
@AllArgsConstructor
@Profile("dev")
public class SchoolTestController {



}
