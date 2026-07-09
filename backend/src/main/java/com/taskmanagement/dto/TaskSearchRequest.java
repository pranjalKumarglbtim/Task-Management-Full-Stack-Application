package com.taskmanagement.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskSearchRequest {
    private String title;
    private String status;
    private String priority;
    private Long assigneeId;
    private String sortBy = "createdAt";
    private String sortDirection = "desc";
    private int page = 0;
    private int size = 10;
}