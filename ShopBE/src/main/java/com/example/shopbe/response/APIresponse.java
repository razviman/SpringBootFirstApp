package com.example.shopbe.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class APIresponse {

    private String message;
    private Object data;
}
