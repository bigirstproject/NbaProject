
package com.sunsun.nbapic.http;

import java.util.Hashtable;

import org.apache.commons.httpclient.methods.RequestEntity;

/**
 * 请求包
 * 
 */
public interface RequestPackage {

    /**
     * GET方式
     */
    public final static int TYPE_GET = 1;

    /**
     * POST方式
     */
    public final static int TYPE_POST = 2;

    /**
     * 获取header设置信息
     * 
     * @return
     */
    public Hashtable<String, String> getRequestHeaders();

    /**
     * 获取GET方法参数
     * 
     * @return
     */
    public String getGetRequestParams();

    /**
     * 获取Post请求Entity<br/>
     * 根据不用的参数，需要将数据转换成对应RequestEntity子类
     * 
     * @return
     */
    public RequestEntity getPostRequestEntity();

    /**
     * 获取请求链接
     * 
     * @return
     */
    public String getUrl();

    /**
     * 请求类型
     * 
     * @return
     */
    public int getRequestType();

    /**
     * 获取请求参数设置，如超时时间
     * 
     * @return
     */
    public Hashtable<String, Object> getSettings();

}
