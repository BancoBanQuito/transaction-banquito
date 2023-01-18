package com.banquito.transaction.Utils;
import java.io.Serializable;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RSFormat implements Serializable{
    private String message; 
    private Object data; 
}
