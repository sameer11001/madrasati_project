package com.webapp.madrasati.school_group.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import com.webapp.madrasati.core.model.BaseCollection;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Document(collection = "groups")
@CompoundIndex(name = "school_groupPost_index", def = "{'schoolId': 1, 'groupPostIds': 1}")

public class Group extends BaseCollection {

    @Builder.Default
    private List<ObjectId> groupPostIds = new ArrayList<>();
    private UUID schoolId;
    private String SchoolImagePath;
}
