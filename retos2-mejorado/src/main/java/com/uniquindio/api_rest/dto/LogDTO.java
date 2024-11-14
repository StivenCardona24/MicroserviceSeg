package com.uniquindio.api_rest.dto;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LogDTO  {
    private String application;
    private String logType; // INFO, ERROR, DEBUG
    private String module;
    private Date timestamp;
    private String summary;
    private String description;
}
