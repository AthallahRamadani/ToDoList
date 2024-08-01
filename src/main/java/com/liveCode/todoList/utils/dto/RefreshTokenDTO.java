package com.liveCode.todoList.utils.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefreshTokenDTO {
    private String refreshToken;
}
