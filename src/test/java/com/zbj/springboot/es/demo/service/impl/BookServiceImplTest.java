package com.zbj.springboot.es.demo.service.impl;

import com.zbj.springboot.es.demo.vo.BookVO;
import com.zbj.springboot.es.demo.vo.BoolQueryVO;
import com.zbj.springboot.es.demo.service.BookService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
class BookServiceImplTest {

    @Autowired
    BookService bookService;

    @Test
    void addBook() {
        BookVO bookVO = new BookVO();
        bookVO.setType("three");
        bookVO.setWordCount(43000);
        bookVO.setAuthor("马士兵");
        bookVO.setTitle("设计模式");
        bookVO.setPublishDate("2019-11-06");
        String result = bookService.addBook(bookVO);
        System.out.println(result);
    }


    @Test
    void deleteBook(){
        String result = bookService.deleteBook("CmnjQG8Bk93Nzr9X-HMh");
        System.out.println(result);
    }

    @Test
    void findBookById(){
        String result = bookService.findBookById("CWm_QG8Bk93Nzr9XFnMB");
        System.out.println(result);
    }

    @Test
    void updateBook(){
        BookVO bookVO = new BookVO();
        bookVO.setId("C2nkQG8Bk93Nzr9X0XMQ");
        bookVO.setAuthor("韩顺平");
        bookVO.setTitle("Java程序设计");
        String result = bookService.update(bookVO);
        System.out.println(result);
    }

    @Test
    void boolQuery(){
        BoolQueryVO queryVO = new BoolQueryVO();
        queryVO.setAuthor("谭浩强");
        //queryVO.setTitle("C语言程序设计");
        /*queryVO.setGtWordCount(90000);
        queryVO.setLtWordCount(10);*/
        String result = bookService.boolQuery(queryVO);
        System.out.println(result);
    }
}