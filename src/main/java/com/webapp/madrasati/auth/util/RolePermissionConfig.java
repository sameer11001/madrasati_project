package com.webapp.madrasati.auth.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class RolePermissionManager {

    private RolePermissionManager() {}

    public static final String ROLE_ADMIN = RoleAppConstant.ADMIN.getString();
    public static final String ROLE_STUDENT = RoleAppConstant.STUDENT.getString();
    public static final String ROLE_SCHOOL_MANAGER = RoleAppConstant.SMANAGER.getString();

    public static final Map<String, Set<String>> ROLE_PERMISSIONS = Map.of(
            ROLE_ADMIN, Set.of(
                    "ADMIN_ACCESS", "STUDENT_ACCESS", "SCHOOL_MANAGER_ACCESS", "MANAGE_SCHOOL", "MANAGE_POSTS",
                    "MANAGE_EVENTS", "VIEW_SCHOOL_INFO", "VIEW_EVENTS", "VIEW_POSTS", "EVENT_CREATE",
                    "EVENT_READ", "EVENT_UPDATE", "EVENT_DELETE", "POST_CREATE", "POST_READ",
                    "POST_UPDATE", "POST_DELETE"
            ),
            ROLE_SCHOOL_MANAGER, Set.of(
                    "SCHOOL_MANAGER_ACCESS", "MANAGE_SCHOOL", "MANAGE_POSTS", "MANAGE_EVENTS",
                    "VIEW_SCHOOL_INFO", "VIEW_EVENTS", "VIEW_POSTS", "EVENT_CREATE",
                    "EVENT_READ", "EVENT_UPDATE", "EVENT_DELETE", "POST_CREATE",
                    "POST_READ", "POST_UPDATE", "POST_DELETE"
            ),
            ROLE_STUDENT, Set.of(
                    "STUDENT_ACCESS", "VIEW_SCHOOL_INFO", "VIEW_EVENTS", "VIEW_POSTS",
                    "EVENT_READ", "POST_READ"
            )
    );
}
