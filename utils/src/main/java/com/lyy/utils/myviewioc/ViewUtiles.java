package com.lyy.utils.myviewioc;

import android.app.Activity;
import android.view.View;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by Administrator on 2017/8/25 0025.
 */

public class ViewUtiles {

    public static void inject(Activity activity){
        inject(new ViewFinder(activity),activity);
    }

    //兼容View
    public static void inject(View view){
        inject(new ViewFinder(view),view);
    }

    //兼容fragment
    public static void inject(View view, Object object){
        inject(new ViewFinder(view),object);
    }

    public static  void inject(ViewFinder viewFinder,Object object){
        injectFiled(viewFinder,object);
        injectEvent(viewFinder,object);
    }

    /**
     * 注入属性
     * @param viewFinder
     * @param object
     */

    private static void injectFiled(ViewFinder viewFinder, Object object) {
        Class<?> clazz =object.getClass();

        Field[] fields =clazz.getDeclaredFields();

        for (Field field:fields ){
            ViewById viewById =field.getAnnotation(ViewById.class);
            if (viewById!=null){
                int viewId =viewById.value();
                View view =viewFinder.findViewById(viewId);

                if (view != null) {
                    field.setAccessible(true);
                    try {
                        field.set(object,view);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }else{
                    throw  new RuntimeException("Invalid @ViewInject for"
                                +clazz.getSimpleName()+"."+field.getName());
                }
            }
        }
    }

    //注入事件
    private static  void injectEvent(ViewFinder viewFinder,Object object){
        // 1.获取所有方法
        Class<?> clazz = object.getClass();
        Method[] methods = clazz.getDeclaredMethods();
        // 2.获取方法上面的所有id
        for (Method method : methods) {
            ViewClick onClick = method.getAnnotation(ViewClick.class);
            if (onClick != null) {
                int[] viewIds = onClick.value();
                if (viewIds.length > 0) {
                    for (int viewId : viewIds) {
                        // 3.遍历所有的id 先findViewById然后 setOnClickListener
                        View view = viewFinder.findViewById(viewId);
                        if (view != null) {
                            view.setOnClickListener(new DeclaredOnClickListener(method, object));
                        }
                    }
                }
            }
        }
    }


    private static class DeclaredOnClickListener implements View.OnClickListener {
        private Method mMethod;
        private Object mHandlerType;

        public DeclaredOnClickListener(Method method, Object handlerType) {
            mMethod = method;
            mHandlerType = handlerType;
        }

        @Override
        public void onClick(View v) {
            // 4.反射执行方法
            mMethod.setAccessible(true);
            try {
                mMethod.invoke(mHandlerType, v);
            } catch (Exception e) {
                e.printStackTrace();
                try {
                    mMethod.invoke(mHandlerType, "");
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        }
    }
}
