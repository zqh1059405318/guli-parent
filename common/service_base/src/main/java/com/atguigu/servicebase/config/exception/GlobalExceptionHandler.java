package com.atguigu.servicebase.config.exception;
import com.atguigu.commonutils.R;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

// 专门定义exception的注解
@ControllerAdvice
public class GlobalExceptionHandler {
    // 指定出现什么异常时
    @ExceptionHandler(Exception.class)
    // 不是在controller中，所以要添加上ResponseBody
    @ResponseBody
    public R error(Exception e){
        e.printStackTrace();
        return R.error().message("执行了全局异常处理器");
    }
}
