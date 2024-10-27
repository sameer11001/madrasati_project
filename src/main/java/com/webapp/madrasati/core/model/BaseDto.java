package com.webapp.madrasati.core.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class BaseDto {
    private Long id;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}
