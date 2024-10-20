package com.webapp.madrasati.auth.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("v1/admin")
@CrossOrigin
@PreAuthorize("hasRole('ADMIN')")
@AllArgsConstructor
public class AdminController {

}
