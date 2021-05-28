package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
public class IndexController {

    @GetMapping("/")
    public String rootGet() {
        return "this is get.";
    }

    @PostMapping("/")
    public String rootPost() {
        return "this is post.";
    }

    @GetMapping("/init")
    public String rootInit(){
        CommonValue.init();
        return "ok";
    }

    @PostMapping("/stub")
    public ResponseDto stubPost(@RequestBody RequestDto requestDto) {
        return ResponseDto.builder()
                          .seqNo(CommonValue.getSeqNo())
                          .paramS1(requestDto.getParamS1())
                          .paramS2(requestDto.getParamS2())
                          .processDt(LocalDateTime.now())
                          .build();
    }

    @GetMapping("/stub")
    public ResponseDto stubGet(@RequestParam("paramS1") String paramS1, @RequestParam("paramS2") String paramS2) {
        return ResponseDto.builder()
                          .seqNo(CommonValue.getSeqNo())
                          .paramS1(paramS1)
                          .paramS2(paramS2)
                          .processDt(LocalDateTime.now())
                          .build();
    }

}
