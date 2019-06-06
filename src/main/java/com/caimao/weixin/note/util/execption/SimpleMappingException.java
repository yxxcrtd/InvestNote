package com.caimao.weixin.note.util.execption;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

/**
 * 重写异常处理
 */
public class SimpleMappingException extends SimpleMappingExceptionResolver {

    protected static final Logger LOGGER = LoggerFactory.getLogger(SimpleMappingException.class);

    @Override
    protected ModelAndView getModelAndView(String viewName, Exception ex) {
        LOGGER.error("运行异常：{}", ex);
        return super.getModelAndView(viewName, ex);
    }
}
