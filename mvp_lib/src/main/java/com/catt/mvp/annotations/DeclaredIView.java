package com.catt.mvp.annotations;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * @author:         支玮
 * @createDate:     2019-07-18 12:02
 * @description:
 *
 * 在V层接口类上声明此注解，
 * 只有被声明后才能被AbstractViewDelegated进行解析，
 * 如果V层接口未声明会抛出 DelegatedException，这是一个RuntimeException
 *
 * <pre>
 *      public interface IHome{
 *
 *          ....
 *
 *          @DeclaredIView
 *          interface IView{
 *
 *          }
 *
 *      }
 * <pre/>
 */
@Retention(RetentionPolicy.RUNTIME)

@Target(ElementType.TYPE)
public @interface DeclaredIView {
}
