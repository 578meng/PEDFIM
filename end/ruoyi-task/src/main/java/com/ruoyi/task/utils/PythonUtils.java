package com.ruoyi.task.utils;

import org.springframework.scheduling.annotation.Async;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class PythonUtils {

    // 执行文件路径
    private String pyPath;

    // 传入参数
    private String param;

    // 执行返回信息
    private List<String> response;

    // 执行报错信息
    private List<String> error;

    // 执行头
    private final String head = "python ";

    /**
     * 无参构造
     */
    public PythonUtils(){
        this.response = new ArrayList<>();
        this.error = new ArrayList<>();
        this.pyPath = "";
        this.param = "";
    }

    /**
     * 构造函数
     * 传入执行文件路径
     * @param path
     */
    public PythonUtils(String path, String... param){
        this.response = new ArrayList<>();
        this.error = new ArrayList<>();
        this.pyPath = path+" ";
        this.param = "";
        for(String p:param){
            this.param += p+" ";
        }
    }

    /***
     * 执行函数
     * @throws IOException
     */
    public void exec() throws IOException {
        String cmd = head+pyPath+param;
        System.out.println("执行命令：");
        System.out.println(cmd);

        Process process = Runtime.getRuntime().exec(cmd);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(process.getInputStream(), Charset.forName("GBK"))
        );
        BufferedReader err = new BufferedReader(
                new InputStreamReader(process.getErrorStream(), Charset.forName("GBK"))
        );

        String str = "";
        while((str = in.readLine()) != null){
            response.add(str);
        }

        while((str = err.readLine()) != null){
            error.add(str);
        }
    }

    public List<String> getResponse() {
        return response;
    }

    public List<String> getError() {
        return error;
    }

    public String getPyPath() {
        return pyPath;
    }

    public void setPyPath(String pyPath) {
        this.pyPath = pyPath;
    }

    public String getParam() {
        return param;
    }

    /**
     * 传入单个参数
     * @param param
     */
    public void setParam(String param) {
        this.param = param;
    }

    /**
     * 传入多个参数
     * @param param
     */
    public void setParam(String... param) {
        this.param = " ";
        for(String p:param){
            this.param += p+" ";
        }
    }

    @Override
    public String toString() {
        return "PythonUtils{" +
                "\n\thead='"+head+"\'"+
                "\n\tpyPath='" + pyPath +"\'"+
                "\n\tparam='" + param +"\'"+
                "\n\tresponse=" + response +
                "\n\terror=" + error+
                "\n}";
    }


}
