package com.zbj.springboot.es.demo.vo;

import lombok.Data;

/**
 * @author ：liuanmin
 * @date ：Created in 2019/12/26
 * @description：
 */
@Data
public class BoolQueryVO {

    private String author;
    private String title;
    private Integer gtWordCount;
    private Integer ltWordCount;

}
