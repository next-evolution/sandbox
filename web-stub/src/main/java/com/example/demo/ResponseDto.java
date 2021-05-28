package com.example.demo;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ResponseDto {

    private Long seqNo;

    private String paramS1;

    private String paramS2;

    private LocalDateTime processDt;

}
