package com.zbj.springboot.es.demo.base.palant.controller;

import com.alibaba.fastjson.JSON;
import com.zbj.springboot.es.demo.base.enums.ResponseCode;
import com.zbj.springboot.es.demo.base.palant.vo.IdxVO;
import com.zbj.springboot.es.demo.base.response.ResponseResult;
import com.zbj.springboot.es.demo.base.service.BaseElasticService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author ：liuanmin
 * @date ：Created in 2019/12/26
 * @description：
 */
@RestController
@RequestMapping("/elasticIndex")
@Slf4j
public class ElasticIndexController {

    @Autowired
    BaseElasticService service;

    /**
     * 创建es索引
     * @param idxVO
     * @return
     */
    @PostMapping("/createIndex")
    public ResponseResult createIndex(@RequestBody IdxVO idxVO){
        ResponseResult responseResult = new ResponseResult();
        try {
            //索引不存在，再创建 否则不允许创建
            if (!service.isExistsIndex(idxVO.getIdxName())){
                String idxSQL = JSON.toJSONString(idxVO.getIdxSql());
                log.warn("idxName={},idxSQL={}",idxVO.getIdxName(),idxVO.getIdxSql());
                service.createIndex(idxVO.getIdxName(),idxSQL);
            } else {
                responseResult.setStatus(false);
                responseResult.setCode(ResponseCode.DUPLICATEKEY_ERROR_CODE.getCode());
                responseResult.setMsg(ResponseCode.DUPLICATEKEY_ERROR_CODE.getMsg());
            }
        } catch (Exception e){
            responseResult.setStatus(false);
            responseResult.setMsg(ResponseCode.ERROR.getMsg());
            responseResult.setCode(ResponseCode.ERROR.getCode());
        }
        return responseResult;
    }

    @RequestMapping("/exists/{index}")
    public ResponseResult indexExists(@PathVariable("index")String index){
        ResponseResult responseResult = new ResponseResult();
        try {
            if (!service.isExistsIndex(index)){
                log.error("index={},索引不存在",index);
                responseResult.setCode(ResponseCode.RESOURCE_NOT_EXIST.getCode());
                responseResult.setMsg(ResponseCode.RESOURCE_NOT_EXIST.getMsg());
            } else {
                responseResult.setMsg("索引已经存在,"+index);
            }
        } catch (Exception e){
            responseResult.setCode(ResponseCode.NETWORK_ERROR.getCode());
            responseResult.setMsg("调用elasticsearch失败");
            responseResult.setStatus(false);
        }
        return responseResult;
    }

    @RequestMapping("/deleteIndex/{idxName}")
    public ResponseResult deleteIndex(@PathVariable("idxName")String idxName){
        ResponseResult responseResult = new ResponseResult();
        try {
            if (!service.isExistsIndex(idxName)){
                log.error("index={},索引不存在",idxName);
                responseResult.setStatus(false);
                responseResult.setCode(ResponseCode.RESOURCE_NOT_EXIST.getCode());
                responseResult.setMsg(ResponseCode.RESOURCE_NOT_EXIST.getMsg());
            } else {
                service.deleteIndex(idxName);
            }
        } catch (Exception e){
            responseResult.setCode(ResponseCode.NETWORK_ERROR.getCode());
            responseResult.setMsg("调用elasticsearch失败");
            responseResult.setStatus(false);
        }
        return responseResult;
    }
}
