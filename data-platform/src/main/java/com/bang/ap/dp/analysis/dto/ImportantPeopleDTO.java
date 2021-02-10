package com.bang.ap.dp.analysis.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImportantPeopleDTO {
    public int id;
    public String code;
    public String name;
    public String times;
    //抓拍图
    public String picture;
    //标准图
    public String picture2;

    public String dataTime;

    public String bkgUrl;
    public String snapUrl;
    public String bkgUrlBak;
    public String snapUrlBak;
    public String bkgUrlPictureNameBak;
    public String snapUrlPictureNameBak;
    public String bkgData;
    public String snapData;
    public int totalSimilar;

    public String cameraIndexcode;
    public String cameraName;
    public String deviceIndexcode;
    public String deviceName;
    public String gender;
    public String age;
    public String glass;
    public String eventTime;

    public Date createTime;
    public Date updateTime;
}
