package com.webapp.madrasati.school.model.dto.res;

import com.webapp.madrasati.auth.model.dto.UserEntityDto;
import com.webapp.madrasati.school.model.dto.SchoolDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateNewSchoolDto {
    private SchoolDto school;
    private UserEntityDto user;
    private String groupId;
}
