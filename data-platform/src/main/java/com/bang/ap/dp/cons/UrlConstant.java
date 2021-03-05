package com.bang.ap.dp.cons;

public class UrlConstant {
    /**
     * 查询监控点列表
     */
    public static final String URL_VIDEO_CAMERA_LIST="/api/resource/v2/camera/search";
    /**
     * 分页获取监控点资源
     */
    public static final String URL_VIDEO_CAMERA_PAGE="/api/resource/v1/cameras";
    /**
     * 获取监控点预览取流URLv2
     */
    public static final String URL_VIDEO_PREVIEW="/api/video/v2/cameras/previewURLs";
    /**
     * 获取监控点回放取流URLv2
     */
    public static final String URL_VIDEO_PALYBACK="/api/video/v2/cameras/playbackURLs";
    /**
     * 查询编码设备列表v2
     */
    public static final String URL_VIDEO_ENCODEDEVICE="/api/resource/v2/encodeDevice/search";





    /**
     * 按照事件类型订阅事件
     */
    public static final String URL_EVENT_SUBSCRIBE="/api/eventService/v1/eventSubscriptionByEventTypes";

    /**
     * 查询事件订阅信息
     */
    public static final String URL_EVENT_SUBSCRIBED_LIST_="/api/eventService/v1/eventSubscriptionView";

    /**
     * 按事件类型取消订阅
     */
    public static final String URL_EVENT_UNSUBSCRIBE_="/api/eventService/v1/eventUnSubscriptionByEventTypes";




    /**
     * 按条件查询人脸抓拍事件
     */
    public static final String URL_FACE_EVENT_NORMAL_="/api/frs/v1/event/face_capture/search";
    /**
     * 按条件查询重点人员事件
     */
    public static final String URL_FACE_EVENT_IMPORTANT_="/api/frs/v1/event/black/search";
    /**
     * 按条件查询陌生人事件
     */
    public static final String URL_FACE_EVENT_STRANGE_="/api/frs/v1/event/stranger/search";
    /**
     * 按条件查询高频人员识别事件
     */
    public static final String URL_FACE_EVENT_HITHFREQUENCY_="/api/frs/v1/event/high_frequency/search";

    /**
     * 人脸服务图片下载
     */
    public static final String URL_FACE_PICTURE_DOWN_="/api/frs/v1/application/picture";

    /**
     * 以图搜图
     */
    public static final String URL_FACE_PICTURE_CAPTURESEARCH="/api/frs/v1/application/captureSearch";


    /**
     * 分页查询动环环境量
     */
    public static final String URL_SENSOR_VALUE="/api/pems/v1/sensor/search";

    /**
     * 分页查询消防监测数据
     */
    public static final String URL_FIRESENSOR_VALUE="/api/fpms/v1/minitor/data/search";



}
