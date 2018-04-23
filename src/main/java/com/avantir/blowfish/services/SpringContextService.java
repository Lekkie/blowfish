package com.avantir.blowfish.services;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * Created by lekanomotayo on 04/01/2018.
 */

@Service
public class SpringContextService implements ApplicationContextAware {

    @Autowired
    private static ApplicationContext context;

    public void setApplicationContext(ApplicationContext context) throws BeansException {
        this.context = context;
    }
    public static ApplicationContext getApplicationContext() {
        return context;
    }
}
