package cn.gdpu.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class Msg {
    private boolean success;
    private String msg;
    private Object data;

    // 构造方法
    public Msg() {}

    public Msg(boolean success, String msg) {
        this.success = success;
        this.msg = msg;
    }

    public Msg(boolean success, String msg, Object data) {
        this.success = success;
        this.msg = msg;
        this.data = data;
    }

    // Getter 和 Setter 方法
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    // 静态工厂方法
    public static Object empty() {
        Map<String, Object> map = new TreeMap<>();
        map.put("code", 0);
        map.put("count", 0);
        map.put("data", new ArrayList<>());
        map.put("msg", "搜索结果为空");
        return JSON.toJSON(map);
    }

    public static Object ok() {
        JSONObject json = new JSONObject();
        json.put("msg", "ok");
        return JSON.toJSON(json);
    }

    public static Object fail() {
        JSONObject json = new JSONObject();
        json.put("msg", "fail");
        return JSON.toJSON(json);
    }

    public static Object msg(String type, Object msg) {
        Map<String, Object> result = new HashMap<>();
        result.put(type, msg);
        return JSON.toJSON(result);
    }

    public static Object msg(Integer code, String type, Object msg) {
        Map<String, Object> result = new HashMap<>();
        result.put("code", code);
        result.put(type, msg);
        return JSON.toJSON(result);
    }

    // 未登录时返回code -1
    public static void unlogin(HttpServletResponse response) {
        try {
            Map<String, Integer> result = new HashMap<>();
            result.put("code", -1);
            String jsonStr = JSON.toJSONString(result);
            response.setContentType("application/json; charset=utf-8");
            response.getWriter().print(jsonStr);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Object msg(Object msg) {
        JSONObject json = new JSONObject();
        json.put("msg", msg);
        return JSON.toJSON(json);
    }

    // 修复后的fail方法
    public static Msg fail(String message) {
        Msg msg = new Msg();
        msg.setSuccess(false);
        msg.setMsg(message);
        return msg;
    }

    // 修复后的success方法
    public static Msg success(String message) {
        Msg msg = new Msg();
        msg.setSuccess(true);
        msg.setMsg(message);
        return msg;
    }

    // 返回成功并携带数据
    public static Msg success(String message, Object data) {
        Msg msg = new Msg();
        msg.setSuccess(true);
        msg.setMsg(message);
        msg.setData(data);
        return msg;
    }
}