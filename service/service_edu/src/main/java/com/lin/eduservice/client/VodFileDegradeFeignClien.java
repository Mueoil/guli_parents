package com.lin.eduservice.client;

import com.lin.R;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 出错后执行此方法
 */
@Component
public class VodFileDegradeFeignClien implements VodClient{
//    出错之后会执行
    @Override
    public R removeAliyunVideo(String id) {
        return R.error().message("删除视频出错了");
    }

    @Override
    public R deleteBatch(List<String> videoIdList) {
        return R.error().message("删除多个视频出错了");
    }
}
