package com.akanton.kpo.bank;

import com.akanton.kpo.bank.ui.IUi;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class App
{
    public static void main( String[] args )
    {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        IUi ui = context.getBean(IUi.class);
        ui.start();
    }
}
