package com.railse.hiring.workForceManagement.dto;

import lombok.Data;

@Data
public class CommentDto {
    private String user; 
    private String comment; 
    private Long timestamp; 
}