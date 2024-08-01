package com.liveCode.todoList.utils.response;


import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WebResponse<T> {
    private String status;
    private T body;
}
