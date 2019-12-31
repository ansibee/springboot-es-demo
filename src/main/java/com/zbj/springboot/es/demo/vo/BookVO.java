package com.zbj.springboot.es.demo.vo;

import lombok.Data;

/**
 * @author ：liuanmin
 * @date ：Created in 2019/12/26
 * @description：
 */
@Data
public class BookVO {

    private String id;
    private String type;
    private Integer wordCount;
    private String author;
    private String title;
    private String publishDate;
}
