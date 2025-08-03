package com.railse.hiring.workForceManagement.model1;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActivityLog {
    private long timestamp;
    private String event;
}
