package com.powernode.listener;

import com.powernode.settings.pojo.TblDicValue;
import com.powernode.settings.service.TblDicValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.*;

public class ApplicationListener implements ServletContextListener {

    @Autowired
    TblDicValueService tblDicValueService;

    @Override
    public void contextInitialized(ServletContextEvent sce) {

        //必须加上此条，因为上下文初始化的时候，IOC容器并没有完成自动初始化对象。
        // 等待IOC容器初始化之后tblDicValueService就能注入进来，否则items就没有值
        //查看到BeanFactory初始化完成之后再加载进来。
        WebApplicationContextUtils.getRequiredWebApplicationContext(sce.getServletContext()).getAutowireCapableBeanFactory().autowireBean(this);

        ServletContext servletContext = sce.getServletContext();


        //接收业务层返回的值
        Map<String, Set<TblDicValue>> map = tblDicValueService.getItems();
        if (map != null) {
            //获取集合中所有的Key值
            Set<String> strings = map.keySet();
            for (String key : strings) {
                //把键值对放入servletContext域中
                //map.get(key)获取每个Set对象
                servletContext.setAttribute(key,map.get(key));
            }
        }


        //处理properties配置文件,并放入上下文域中
        //    读取 classpath 路径下的 Stage2Possibility.properties 文件
        ResourceBundle resourceBundle = ResourceBundle.getBundle("properties/Stage2Possibility");
        Enumeration<String> keys = resourceBundle.getKeys();

        Map map1 = new TreeMap();
        while (keys.hasMoreElements()){
            String s = keys.nextElement();
            String string = resourceBundle.getString(s);
            map1.put(s,string);
        }
        servletContext.setAttribute("mMap",map1);
    }



    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
