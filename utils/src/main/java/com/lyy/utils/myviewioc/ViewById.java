package com.lyy.utils.myviewioc;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Administrator on 2017/8/25 0025.
 */
//RUNTIME 运行时检测，CLASS编辑是butterKife 使用这个  SOURCE源码资源的时候
@Retention(RetentionPolicy.RUNTIME)
//FIELD 注解只能放在属性上 METHOD 方法上 TYPE类上 CONSTRUCTOR构造方法上
@Target(ElementType.FIELD)
public @interface ViewById {
    int value();
}
