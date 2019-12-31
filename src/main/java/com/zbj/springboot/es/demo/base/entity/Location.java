package com.zbj.springboot.es.demo.base.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder(toBuilder=true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Location {

    private Long locationId;

    private String flag;

    private String localCode;

    private String localName;

    private Integer lv;

    private String supLocalCode;

    private String url;

    public Location(Long locationId, String localName, String supLocalCode, String url, Integer lv) {
        this.locationId=locationId;
        this.localName = localName;
        this.supLocalCode = supLocalCode;
        this.url = url;
        this.lv=lv;
    }

    public Location(String localCode, String localName, String supLocalCode, String url, Integer lv) {
        super();
        this.localCode = localCode;
        this.localName = localName;
        this.supLocalCode = supLocalCode;
        this.url = url;
        this.lv=lv;
    }

    public Location(String localCode, String localName, String supLocalCode, String url, Integer lv,String flag) {
        super();
        this.localCode = localCode;
        this.localName = localName;
        this.supLocalCode = supLocalCode;
        this.url = url;
        this.lv=lv;
        this.flag=flag;
    }

}