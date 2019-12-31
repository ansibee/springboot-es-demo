package com.zbj.springboot.es.demo.base.service;

import com.alibaba.fastjson.JSON;
import com.zbj.springboot.es.demo.base.entity.ElasticEntity;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.IndicesOptions;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.text.html.parser.Entity;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author ：liuanmin
 * @date ：Created in 2019/12/26
 * @description：
 */
@Slf4j
@Component
public class BaseElasticService {

    @Autowired
    RestHighLevelClient client;

    /**
     * 创建索引
     * @param idxName 索引名称
     * @param idxSQL 索引描述
     */
    public void createIndex(String idxName,String idxSQL){
        try {
            if (!this.indexExists(idxName)){
                log.error("idxName={}已经存在,idxSQL={}",idxName,idxSQL);
                return;
            }
            CreateIndexRequest request = new CreateIndexRequest(idxName);
            buildSetting(request);
            request.mapping(idxSQL, XContentType.JSON);
            CreateIndexResponse response = client.indices().create(request,RequestOptions.DEFAULT);
            if (!response.isAcknowledged()){
                throw new RuntimeException("初始化失败");
            }
        } catch (Exception e){
            e.printStackTrace();
            System.exit(0);
        }
    }

    /**
     *  判断某个索引是否存在
     * @param idxName 索引名
     * @return
     * @throws Exception
     */
    public boolean indexExists(String idxName) throws Exception{
        GetIndexRequest request = new GetIndexRequest(idxName);
        request.local(false);
        request.humanReadable(true);
        request.includeDefaults(false);
        request.indicesOptions(IndicesOptions.lenientExpandOpen());
        return client.indices().exists(request, RequestOptions.DEFAULT);
    }

    /**
     * 判断某个索引是否存在
     * @param idxName 索引名
     * @return
     * @throws Exception
     */
    public boolean isExistsIndex(String idxName) throws Exception{
        return client.indices().exists(new GetIndexRequest(idxName),RequestOptions.DEFAULT);
    }

    /**
     * 设置分片
     * @param request
     */
    public void buildSetting(CreateIndexRequest request){
        request.settings(Settings.builder()
                .put("index.number_of_shards",3)
                .put("index.number_of_replicas",2)
        );
    }

    /**
     * 插入或者更新一条数据
     * @param idxName 索引名
     * @param entity 对象
     */
    public void insertOrUpdateOne(String idxName, ElasticEntity entity){
        IndexRequest request = new IndexRequest(idxName);
        log.info("Data: id={},entity={}",entity.getId(), JSON.toJSONString(entity.getData()));
        request.id(entity.getId());
        request.source(entity.getData(),XContentType.JSON);
        try {
            client.index(request,RequestOptions.DEFAULT);
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    /**
     * 批量插入数据
     * @param idxName 索引名
     * @param list 数据源
     */
    public void insertBatch(String idxName, List<ElasticEntity> list){
        BulkRequest request = new BulkRequest();
        list.forEach(item->request.add(new IndexRequest(idxName)
                .id(item.getId())
                .source(item.getData(),XContentType.JSON)
            )
        );

        try {
            client.bulk(request,RequestOptions.DEFAULT);
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    /**
     * 删除一条数据
     * @param idxName 索引名
     * @param entity 实体
     */
    public void deleteOne(String idxName, ElasticEntity entity) {
        DeleteRequest request = new DeleteRequest(idxName);
        request.id(entity.getId());
        try {
            client.delete(request,RequestOptions.DEFAULT);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 批量删除数据
     * @param idxName 索引名
     * @param idList 待删除列表
     * @param <T>
     */
    public <T> void deleteBatch(String idxName, Collection<T> idList){
        BulkRequest request = new BulkRequest();
        idList.forEach(id->request.add(new DeleteRequest(idxName,id.toString())));
        try {
            client.bulk(request,RequestOptions.DEFAULT);
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    /**
     * 查询
     * @param idxName 索引名
     * @param builder 查询参数
     * @param clazz 结果类对象
     * @param <T>
     * @return
     */
    public <T> List<T> search(String idxName, SearchSourceBuilder builder, Class<T> clazz){
        SearchRequest request = new SearchRequest(idxName);
        request.source(builder);
        try {
            SearchResponse response = client.search(request,RequestOptions.DEFAULT);
            SearchHit[] hits = response.getHits().getHits();
            List<T> res = new ArrayList<>(hits.length);
            for (SearchHit hit : hits){
                res.add(JSON.parseObject(hit.getSourceAsString(),clazz));
            }
            return res;
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    /**
     * 删除索引
     * @param idxName 索引名
     */
    public void deleteIndex(String idxName){
        try {
            if (!this.indexExists(idxName)){
                log.error("idxName={}已经存在",idxName);
                return;
            }
            client.indices().delete(new DeleteIndexRequest(idxName),RequestOptions.DEFAULT);
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public void deleteByQuery(String idxName, QueryBuilder builder){
        DeleteByQueryRequest request = new DeleteByQueryRequest(idxName);
        request.setQuery(builder);
        //设置批量操作数量，最大为10000
        request.setBatchSize(10000);
        request.setConflicts("conflicts");
        try {
            client.deleteByQuery(request,RequestOptions.DEFAULT);
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

}
