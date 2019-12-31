package com.zbj.springboot.es.demo.base.palant.controller;

import com.zbj.springboot.es.demo.base.entity.ElasticEntity;
import com.zbj.springboot.es.demo.base.enums.ResponseCode;
import com.zbj.springboot.es.demo.base.palant.utils.ElasticUtil;
import com.zbj.springboot.es.demo.base.palant.vo.ElasticDataVO;
import com.zbj.springboot.es.demo.base.palant.vo.QueryVO;
import com.zbj.springboot.es.demo.base.response.ResponseResult;
import com.zbj.springboot.es.demo.base.service.BaseElasticService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author ：liuanmin
 * @date ：Created in 2019/12/27
 * @description：
 */
@RestController
@Slf4j
@RequestMapping("/elasticMgr")
public class ElasticMgrController {

    @Autowired
    BaseElasticService service;

    /**
     * 新增数据
     * {
     *     "idxName":"idx_location",
     *     "elasticEntity":{
     *         "id":"1111",
     *         "data":{
     *             "id":"2222",
     *             "flag":"D",
     *             "localCode":"9999",
     *             "localName":"重庆",
     *             "lv":9,
     *             "supLocalCode":"99998888",
     *             "url":"zbj/test.html"
     *         }
     *     }
     * }
     *
     * @param dataVO
     * @return
     */
    @PostMapping("/add")
    public ResponseResult add(@RequestBody ElasticDataVO dataVO){
        ResponseResult responseResult = new ResponseResult();
        try {
            if (StringUtils.isEmpty(dataVO.getIdxName())){
                return idxNameIsNull();
            }
            ElasticEntity entity = new ElasticEntity();
            entity.setId(dataVO.getElasticEntity().getId());
            entity.setData(dataVO.getElasticEntity().getData());

            service.insertOrUpdateOne(dataVO.getIdxName(),entity);
        } catch (Exception e){
            log.error("插入数据异常，metadataVO={},异常信息={}",dataVO.toString(),e.getMessage());
            responseResult.setStatus(false);
            responseResult.setMsg("服务忙，请稍后重试");
            responseResult.setCode(ResponseCode.ERROR.getCode());
        }
        return responseResult;
    }

    /**
     * 查询数据
     * @param queryVO
     * @return
     */
    @RequestMapping("/get")
    public ResponseResult get(@RequestBody QueryVO queryVO){
        ResponseResult responseResult = new ResponseResult();
        if (StringUtils.isEmpty(queryVO.getIdxName())){
            return idxNameIsNull();
        }
        try {
            Class<?> clazz = ElasticUtil.getClazz(queryVO.getClassName());
            Map<String,Object> params = queryVO.getQuery().get("match");
            Set<String> keys = params.keySet();
            MatchQueryBuilder queryBuilder = null;
            for (String key : keys) {
                queryBuilder = QueryBuilders.matchQuery(key,params.get(key));
            }
            if (queryBuilder != null){
                SearchSourceBuilder sourceBuilder = ElasticUtil.initSearchSourceBuilder(queryBuilder);
                List<?> data = service.search(queryVO.getIdxName(),sourceBuilder,clazz);
                responseResult.setData(data);
            }
        } catch (Exception e){
            log.error("查询数据异常,metadataVO={},异常信息={}",queryVO.toString(),e.getMessage());
            responseResult.setStatus(false);
            responseResult.setCode(ResponseCode.ERROR.getCode());
            responseResult.setMsg("服务忙，请稍后再试");
        }
        return responseResult;
    }

    /**
     * 删除数据
     * @param dataVO
     * @return
     */
    @RequestMapping("/delete")
    public ResponseResult delete(@RequestBody ElasticDataVO dataVO){
        ResponseResult responseResult = new ResponseResult();
        try {
            if (StringUtils.isEmpty(dataVO.getIdxName())){
                return idxNameIsNull();
            }
            service.deleteOne(dataVO.getIdxName(),dataVO.getElasticEntity());
        } catch (Exception e){
            log.error("删除数据异常,metadataVO={},异常信息={}",dataVO.toString(),e.getMessage());
            responseResult.setStatus(false);
            responseResult.setCode(ResponseCode.ERROR.getCode());
            responseResult.setMsg("服务忙，请稍后再试");
        }
        return responseResult;
    }

    /**
     * 当索引为空时调用
     * @return
     */
    private ResponseResult idxNameIsNull(){
        ResponseResult responseResult = new ResponseResult();
        log.warn("索引为空");
        responseResult.setStatus(false);
        responseResult.setMsg("索引为空，不允许提交");
        responseResult.setCode(ResponseCode.PARAM_ERROR_CODE.getCode());
        return responseResult;
    }
}
