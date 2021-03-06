package com.wsw.patrickstar.handler;

import com.wsw.patrickstar.api.CommonResult;
import com.wsw.patrickstar.exception.TaskServiceException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @Author WangSongWen
 * @Date: Created in 16:41 2021/2/4
 * @Description: 全局异常处理
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = TaskServiceException.class)
    @ResponseBody
    public CommonResult taskExceptionHandler(TaskServiceException e) {
        e.printStackTrace();
        return CommonResult.failed("失败: " + e.getMessage());
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public CommonResult exceptionHandler(Exception e) {
        e.printStackTrace();
        return CommonResult.failed("失败: " + e.getMessage());
    }

}
