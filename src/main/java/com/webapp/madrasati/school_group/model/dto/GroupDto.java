package com.webapp.madrasati.school_group.model.dto;

import java.util.List;
import java.util.UUID;

import org.bson.types.ObjectId;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class GroupDto {
    private List<ObjectId> groupPostIds;
    private UUID schoolId;
}
