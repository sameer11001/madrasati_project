package com.webapp.madrasati.auth.model.dto.res;

import java.io.Serial;
import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserPageDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1905122041950251207L;

    private String userName;
    private String school;
    private String password;
    private String image;

}
