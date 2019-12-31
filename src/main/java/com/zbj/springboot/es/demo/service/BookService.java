package com.zbj.springboot.es.demo.service;

import com.zbj.springboot.es.demo.vo.BookVO;
import com.zbj.springboot.es.demo.vo.BoolQueryVO;

/**
 * @author ：liuanmin
 * @date ：Created in 2019/12/26
 * @description：
 */
public interface BookService {

    String addBook(BookVO bookVO);

    String update(BookVO bookVO);

    String findBookById(String id);

    String boolQuery(BoolQueryVO boolQueryVO);

    String deleteBook(String id);
}
