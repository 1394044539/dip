package com.wpy.dips.bean;

import lombok.Data;

@Data
public class DisData {
    private int ddataid;
    private int disasterid;
    private String tableTitle;
    private String tableTh;
    private String tableTime;
    private String tableData;
    private String updateTime;

    public DisData() {
    }

    public DisData(int ddataid, int disasterid, String tableTitle, String tableTh, String time, String data,String updateTime) {
        this.ddataid = ddataid;
        this.disasterid = disasterid;
        this.tableTitle = tableTitle;
        this.tableTh = tableTh;
        this.tableTime = time;
        this.tableData = data;
        this.updateTime=updateTime;
    }

    public DisData(int disasterid, String tableTitle, String tableTh, String time, String data,String updateTime) {
        this.disasterid = disasterid;
        this.tableTitle = tableTitle;
        this.tableTh = tableTh;
        this.tableTime = time;
        this.tableData = data;
        this.updateTime=updateTime;
    }
}
