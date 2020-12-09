package com.harry.videowatermark.interceptor;

import com.harry.videowatermark.model.ApiResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 描述:
 *
 * @author mengweibo@kanzhun.com
 * @create 2020/8/4
 */
@Slf4j
@ControllerAdvice
public class WebExceptionController {

    @ResponseBody
    @ExceptionHandler(Exception.class)
    public ApiResult customException(Exception e) {
        log.error("ExceptionController.customException", e);
        return ApiResult.failed();
    }


}

