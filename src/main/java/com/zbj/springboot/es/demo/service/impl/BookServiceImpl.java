package com.zbj.springboot.es.demo.service.impl;

import com.zbj.springboot.es.demo.vo.BookVO;
import com.zbj.springboot.es.demo.vo.BoolQueryVO;
import com.zbj.springboot.es.demo.service.BookService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Map;

/**
 * @author ：liuanmin
 * @date ：Created in 2019/12/26
 * @description：
 */
@Slf4j
@Service
public class BookServiceImpl implements BookService {

    @Autowired
    RestHighLevelClient client;

    @Override
    public String addBook(BookVO bookVO) {
        try {
            XContentBuilder content = XContentFactory.jsonBuilder()
                    .startObject()
                    .field("type",bookVO.getType())
                    .field("word_count",bookVO.getWordCount())
                    .field("author",bookVO.getAuthor())
                    .field("title",bookVO.getTitle())
                    .field("publish_date",bookVO.getPublishDate())
                    .endObject();
            IndexRequest request = new IndexRequest("book").source(content);
            IndexResponse response = client.index(request, RequestOptions.DEFAULT);
            return response.toString();
        } catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String update(BookVO bookVO) {
        try {
            UpdateRequest request = new UpdateRequest("book",bookVO.getId());
            XContentBuilder content = XContentFactory.jsonBuilder()
                    .startObject()
                    .field("type", bookVO.getType())
                    .field("word_count", bookVO.getWordCount())
                    .field("author", bookVO.getAuthor())
                    .field("title", bookVO.getTitle())
                    .timeField("publish_date", bookVO.getPublishDate())
                    .endObject();
            request.doc(content);
            UpdateResponse response = client.update(request,RequestOptions.DEFAULT);
            return response.toString();
        } catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String findBookById(String id) {
        GetRequest request = new GetRequest("book",id);
        try {
            GetResponse response = client.get(request,RequestOptions.DEFAULT);
            Map<String,Object> map = response.getSource();
            return map.toString();
        } catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String boolQuery(BoolQueryVO queryVO) {

        BoolQueryBuilder boolBuilder = QueryBuilders.boolQuery();
        if (!StringUtils.isEmpty(queryVO.getAuthor())){
            boolBuilder.must(QueryBuilders.matchPhraseQuery("author",queryVO.getAuthor()));
        }
        if (!StringUtils.isEmpty(queryVO.getTitle())){
            boolBuilder.must(QueryBuilders.matchPhraseQuery("title",queryVO.getTitle()));
        }
        if (queryVO.getLtWordCount() != null && queryVO.getGtWordCount() != null){
            RangeQueryBuilder rangeBuilder = QueryBuilders.rangeQuery("word_count")
                    .from(queryVO.getGtWordCount())
                    .to(queryVO.getLtWordCount());
            boolBuilder.filter(rangeBuilder);
        }
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder().query(boolBuilder);
        SearchRequest request = new SearchRequest().source(sourceBuilder);
        try {
            SearchResponse response = client.search(request,RequestOptions.DEFAULT);
            return response.toString();
        } catch (IOException e){
            e.printStackTrace();
        }
        return "";
    }



    @Override
    public String deleteBook(String id) {
        try {
            DeleteRequest request = new DeleteRequest("book").id(id);
            DeleteResponse response = client.delete(request,RequestOptions.DEFAULT);
            return response.toString();
        } catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }
}
