/*
package com.zbj.springboot.es.demo.repository;

import com.zbj.springboot.es.demo.domain.Item;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ItemRepositoryTest {

    @Autowired
    ItemRepository repository;

    @Autowired(required = false)
    ElasticsearchTemplate elasticsearchTemplate;

    *
     * 创建索引

    @Test
    public void createIndex(){
        //创建索引，会根据Item类的@Docunment注解信息来创建
        elasticsearchTemplate.createIndex(Item.class);
        //配置映射，会根据Item类的Id，Field等字段来自动完成映射
        elasticsearchTemplate.putMapping(Item.class);
    }

    *
     * 删除索引

    @Test
    public void deleteIndex(){
        elasticsearchTemplate.deleteIndex("item");
    }

    *
     * 新增

    @Test
    public void insert(){
        Item item = new Item(1L, "小米手机7", "手机", "小米", 2999.00, "https://img12.360buyimg.com/n1/s450x450_jfs/t1/14081/40/4987/124705/5c371b20E53786645/c1f49cd69e6c7e6a.jpg");
        repository.save(item);
    }

    *
     * 批量新增

    @Test
    public void insertList(){
        List<Item> list = new ArrayList<>();
        list.add(new Item(2L, "坚果手机R1", "手机", "锤子", 3999.00, "https://img12.360buyimg.com/n1/s450x450_jfs/t1/14081/40/4987/124705/5c371b20E53786645/c1f49cd69e6c7e6a.jpg"));
        list.add(new Item(3L, "华为META20", "手机", "华为", 4999.00, "https://img12.360buyimg.com/n1/s450x450_jfs/t1/14081/40/4987/124705/5c371b20E53786645/c1f49cd69e6c7e6a.jpg"));
        list.add(new Item(4L, "iPhone X", "手机", "iPhone", 5100.00, "https://img12.360buyimg.com/n1/s450x450_jfs/t1/14081/40/4987/124705/5c371b20E53786645/c1f49cd69e6c7e6a.jpg"));
        list.add(new Item(5L, "iPhone XS", "手机", "iPhone", 5999.00, "https://img12.360buyimg.com/n1/s450x450_jfs/t1/14081/40/4987/124705/5c371b20E53786645/c1f49cd69e6c7e6a.jpg"));
        // 接收对象集合，实现批量新增
        repository.saveAll(list);
    }

    *
     * 修改

    @Test
    public void modify(){
        Item item = new Item(1L, "小米手机10000", "手机", "小米", 2999.00, "https://img12.360buyimg.com/n1/s450x450_jfs/t1/14081/40/4987/124705/5c371b20E53786645/c1f49cd69e6c7e6a.jpg");
        repository.save(item);
    }

    *
     * 删除所有

    @Test
    public void deleteAll(){
        repository.deleteAll();
    }

    *
     * 基本查询

    @Test
    public void query(){
        //查询全部，并按照价格降序排序
        Iterable<Item> itemIterable = repository.findAll(Sort.by("price").descending());
        itemIterable.forEach(item -> System.out.println("item = "+item));
    }

    @Test
    void findByPriceBetween() {
    }
}*/
