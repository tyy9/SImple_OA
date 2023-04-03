package com.myoa.my_oa.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerException extends RuntimeException{
    private Integer code;
    private String msg;
    public String toString() {
        return "CustomerException{" +
                "message=" + this.getMessage() +
                ", code=" + code +
                '}';
    }
}
