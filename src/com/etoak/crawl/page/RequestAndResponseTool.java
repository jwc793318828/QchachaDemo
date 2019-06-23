package com.etoak.crawl.page;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

import java.io.IOException;

public class RequestAndResponseTool {


    public static Page  sendRequstAndGetResponse(String url) {
        Page page = null;
        // 1.生成 HttpClinet 对象并设置参数
        HttpClient httpClient = new HttpClient();
        // 设置 HTTP 连接超时 5s
        httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(5000);
        // 2.生成 GetMethod 对象并设置参数
        GetMethod getMethod = new GetMethod(url);
        getMethod.addRequestHeader("Cookie", "UM_distinctid=16b72c0ff50555-0d50f17e5c8578-3c604504-1fa400-16b72c0ff51660; zg_did=%7B%22did%22%3A%20%2216b72c0ff5f572-011d0e55575c9-3c604504-1fa400-16b72c0ff603ad%22%7D; _uab_collina=156099837952298970109224; acw_tc=7793462615609983826941398e8cdd0239ea250a3904a4f429fad0e6cf; QCCSESSID=mc2p8ji3nsptae2g1uab251ki2; CNZZDATA1254842228=1382632714-1560996794-%7C1561097095; Hm_lvt_3456bee468c83cc63fb5147f119f1075=1560998380,1561098787; hasShow=1; Hm_lpvt_3456bee468c83cc63fb5147f119f1075=1561098919; zg_63e87cf22c3e4816a30bfbae9ded4af2=%7B%22sid%22%3A%201561098941206%2C%22updated%22%3A%201561099006894%2C%22info%22%3A%201561098941207%2C%22superProperty%22%3A%20%22%7B%7D%22%2C%22platform%22%3A%20%22%7B%7D%22%2C%22utm%22%3A%20%22%7B%7D%22%2C%22referrerDomain%22%3A%20%22%22%7D; zg_de1d1a35bfa24ce29bbf2c7eb17e6c4f=%7B%22sid%22%3A%201561098787187%2C%22updated%22%3A%201561099043787%2C%22info%22%3A%201560998379364%2C%22superProperty%22%3A%20%22%7B%7D%22%2C%22platform%22%3A%20%22%7B%7D%22%2C%22utm%22%3A%20%22%7B%7D%22%2C%22referrerDomain%22%3A%20%22%22%2C%22cuid%22%3A%20%22382bbd2e7e716de67d7c57bdbbe585ab%22%7D");
        // 设置 get 请求超时 5s
        getMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 5000);
        // 设置请求重试处理
        getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
        // 3.执行 HTTP GET 请求
        try {
            int statusCode = httpClient.executeMethod(getMethod);
        // 判断访问的状态码
            if (statusCode != HttpStatus.SC_OK) {
                System.err.println("Method failed: " + getMethod.getStatusLine());
            }
        // 4.处理 HTTP 响应内容
            byte[] responseBody = getMethod.getResponseBody();// 读取为字节 数组
            String contentType = getMethod.getResponseHeader("Content-Type").getValue(); // 得到当前返回类型
            page = new Page(responseBody,url,contentType); //封装成为页面
        } catch (HttpException e) {
        // 发生致命的异常，可能是协议不对或者返回的内容有问题
            System.out.println("Please check your provided http address!");
            e.printStackTrace();
        } catch (IOException e) {
        // 发生网络异常
            e.printStackTrace();
        } finally {
        // 释放连接
            getMethod.releaseConnection();
        }
        return page;
    }
}
