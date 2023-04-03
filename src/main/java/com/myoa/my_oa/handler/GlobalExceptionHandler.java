package com.myoa.my_oa.handler;

import com.myoa.my_oa.common.R;
import com.myoa.my_oa.exception.CustomerException;
import com.myoa.my_oa.utils.ExceptionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@ResponseBody
@ControllerAdvice(annotations = {RestController.class, Controller.class})
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public R excpeionHandler(Exception e){
        e.printStackTrace();
        return R.error().message("执行全局异常");
    }

    @ExceptionHandler(CustomerException.class)
    public R CustomerExceptionHandler(CustomerException e){
        log.error(ExceptionUtil.getMessage(e));
        return R.error().message(e.getMsg()).code(e.getCode());
    }
}
