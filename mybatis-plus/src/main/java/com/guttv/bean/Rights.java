package com.guttv.bean;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class Rights {


    /**
     *
     */
    private String code;
    /**
     *
     */
    private Integer id;

    @JsonIgnore
    private Integer pageNum = 1;
    @JsonIgnore
    private Integer pageSize = 20;
}