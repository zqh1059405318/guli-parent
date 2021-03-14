package com.atguigu.eduservice.client;

import com.atguigu.commonutils.R;
import org.springframework.stereotype.Component;

import java.util.List;

// 调用接口的时候，如果正确，则方法不会执行，当错误的时候才会执行实现类中的方法
@Component
public class VodFileDegradeFeignClient implements VodClient {
    @Override
    public R removeAliyunVideo(String id) {
        return R.error().message("删除视频出错，调用了熔断器");
    }

    @Override
    public R deleteBatch(List<String> videoIdList) {
        return R.error().message("删除多个视频出错，调用了熔断器");
    }
}
