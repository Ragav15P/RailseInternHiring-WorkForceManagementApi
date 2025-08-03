package com.railse.hiring.workForceManagement.model1;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Comment {
    private long timestamp;
    private String user;
    private String text;
}
