package com.atguigu.servicebase.config.exception;
import com.atguigu.commonutils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

// 本类为统一异常处理器类
// 专门定义exception的注解
@ControllerAdvice
// 往日志里写日常
@Slf4j
public class GlobalExceptionHandler {
    // 指定出现什么异常时
    // 不是在controller中，所以要添加上ResponseBody，才可以去抓响应信息中的包
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public R error(Exception e){
        e.printStackTrace();
        return R.error().message("执行了全局异常处理器");
    }

    // 自定义某类异常，如果异常为算数异常，就会执行这个异常。
    @ExceptionHandler(ArithmeticException.class)
    @ResponseBody
    public R error(ArithmeticException e){
        e.printStackTrace();
        return R.error().message("执行了自定义的异常");
    }

    // 自定义某类异常，如果异常为自己指定的异常，就会执行这个异常。
    @ExceptionHandler(GuliException.class)
    @ResponseBody
    public R error(GuliException e){
        // 写到日志里面去
        log.error(e.getMsg());
        e.printStackTrace();
        return R.error().message(e.getMsg()).code(e.getCode());
    }


}
