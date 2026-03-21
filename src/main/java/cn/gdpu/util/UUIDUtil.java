package cn.gdpu.util;

import java.util.UUID;

/**
 * @ClassName UUIDUtil
 * @Author ttaurus
 * @Date Create in 2020/3/4 19:23
 */
public class UUIDUtil {

    private UUIDUtil() {}

    /**
     * 简单模式，简单模式为不带'-'的UUID字符串
     * @return 生成32位的uuid
     */
    public static String GeneratorUUIDOfSimple() {
        return UUID.randomUUID().toString().trim().replaceAll("-", "");
    }
}
