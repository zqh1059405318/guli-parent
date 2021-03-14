package com.atguigu.eduservice.client;


import com.atguigu.commonutils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Component
@FeignClient(name = "service-vod", fallback = VodFileDegradeFeignClient.class)
public interface VodClient {
    // 定义调用的方法和路径
    // 如果有PathVariable注解，一定要定义名称，否则会出错
    @DeleteMapping("/eduvod/video/removeAliyunVideo/{id}")
    public R removeAliyunVideo(@PathVariable("id") String id);

    //删除多个阿里云视频的方法
    @DeleteMapping("/eduvod/video/delete-batch")
    public R deleteBatch(@RequestParam("videoIdList") List<String> videoIdList);
}
