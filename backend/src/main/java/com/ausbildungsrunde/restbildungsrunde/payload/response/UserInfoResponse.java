package com.ausbildungsrunde.restbildungsrunde.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserInfoResponse {
    private Long id;
    private String username;
}
