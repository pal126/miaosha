package com.pal.miaosha.exception;

import com.pal.miaosha.result.CodeMsg;
import com.pal.miaosha.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.net.BindException;

/**
 * 全局异常处理器
 *
 * @author pal
 */
@Slf4j
@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    public Result<String> exceptionHandler(HttpServletRequest request, Exception e) {
        if (e instanceof GlobalException) {
            GlobalException ge = (GlobalException) e;
            return Result.error(ge.getCodeMsg());
        }
        if (e instanceof BindException) {
            BindException ex = (BindException) e;
            String msg = ex.getMessage();
            return Result.error(CodeMsg.BIND_ERROR.fillArgs(msg));
        } else {
            log.error("exception:{}", e.toString());
            return Result.error(CodeMsg.SERVER_ERROR);
        }
    }
}
