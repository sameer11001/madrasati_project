package com.webapp.madrasati.core.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.webapp.madrasati.core.model.ApiResponseBody;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.lang.management.RuntimeMXBean;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping(value = "/")
public class BaseController {

    @GetMapping
    public ApiResponseBody<Map<String, Object>> getMethodName() {
        Map<String, Object> serverInfo = new HashMap<>();
        try {
            InetAddress localHost = InetAddress.getLocalHost();
            OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();
            RuntimeMXBean runtimeBean = ManagementFactory.getRuntimeMXBean();

            serverInfo.put("hostname", localHost.getHostName());
            serverInfo.put("ip", localHost.getHostAddress());
            serverInfo.put("os", System.getProperty("os.name"));
            serverInfo.put("osVersion", System.getProperty("os.version"));
            serverInfo.put("javaVersion", System.getProperty("java.version"));
            serverInfo.put("availableProcessors", Runtime.getRuntime().availableProcessors());
            serverInfo.put("totalMemory", Runtime.getRuntime().totalMemory());
            serverInfo.put("freeMemory", Runtime.getRuntime().freeMemory());
            serverInfo.put("systemLoad", osBean.getSystemLoadAverage());
            serverInfo.put("jvmUptime", runtimeBean.getUptime());

            return ApiResponseBody.success(serverInfo, "Server information retrieved successfully");
        } catch (Exception e) {
            return ApiResponseBody.errorServer("Error retrieving server information: " + e.getMessage());
        }
    }

}
