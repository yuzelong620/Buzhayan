package com.kratos.game.herphone.common;


import com.kratos.engine.framework.net.socket.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author tang he
 * @since 1.0.0
 */
@Slf4j
@Configuration
public class ExceptionHandler implements HandlerExceptionResolver {
    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
                                         Exception e) {
        String message;
        int status;
        String error;
        Long timestamp = new Date().getTime();
        String url = request.getRequestURI();
        if (e instanceof IllegalArgumentException) {
            message = e.getMessage();
            status = HttpStatus.BAD_REQUEST.value();
            error = "Bad Request";
            log.warn("Bad Request url " + url + ":" + e.getMessage());
        } else if (e instanceof BusinessException) {
            message = e.getMessage();
            status = HttpStatus.BAD_REQUEST.value();
            error = "Request";
            log.info("Business exception url " + url + ":" + e.getMessage());
        } else {
            message = "系统内部错误！";
            status = HttpStatus.INTERNAL_SERVER_ERROR.value();
            error = "Internal Server Error";
            log.error("Exception throws request url " + url + ":", e);
        }
        Map<String, Object> result = new HashMap<>();
        result.put("message", message);
        result.put("status", status);
        result.put("error", error);
        result.put("timestamp", timestamp);
        MappingJackson2JsonView jsonView = new MappingJackson2JsonView();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setView(jsonView);
        modelAndView.getModelMap().putAll(result);
        modelAndView.setStatus(HttpStatus.valueOf(status));
        return modelAndView;
    }
}