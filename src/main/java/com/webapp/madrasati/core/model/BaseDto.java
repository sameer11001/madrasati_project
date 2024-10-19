package com.webapp.madrasati.core.model;

import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public abstract class BaseDto {
    private Long id;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}
