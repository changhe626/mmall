package com.mmall.util;

import com.google.common.collect.Lists;
import com.mmall.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.type.JavaType;
import org.codehaus.jackson.type.TypeReference;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class JsonUtil {
    private JsonUtil(){}

    private static ObjectMapper objectMapper=new ObjectMapper();
    static {
        //序列化的配置
        //对象的虽有字段全部列入
        objectMapper.setSerializationInclusion(JsonSerialize.Inclusion.ALWAYS);
        //取消默认转换timestamps形式
        objectMapper.configure(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS,false);
        //忽略空bean转json 的错误
        objectMapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS,false);
        //所有的日期格式都统一一下的样式yyyy-MM-dd HH:mm:ss
        objectMapper.setDateFormat(new SimpleDateFormat(DateTimeUtil.STANDARD_FORMAT));

        //反序列化的配置
        //忽略在json字符串中存在,但是在java对象中不存在对应属性的情况,防止错误
        objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES,false);

    }

    /**
     * bean 变成string
     * @param obj
     * @param <T>
     * @return
     */
    public static <T>String obj2String(T obj){
        if(obj==null){
            return null;
        }
        if (obj instanceof String){
            return (String)obj;
        }else {
            try {
                return objectMapper.writeValueAsString(obj);
            } catch (IOException e) {
                e.printStackTrace();
                log.warn("parse  object to string error",e);
                return null;
            }
        }
    }


    /**
     * 返回格式化好的string
     * @param obj
     * @param <T>
     * @return
     */
    public static <T>String obj2StringPretty(T obj){
        if(obj==null){
            return null;
        }
        if (obj instanceof String){
            return (String)obj;
        }else {
            try {
                return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
            } catch (IOException e) {
                e.printStackTrace();
                log.warn("parse  object to string error",e);
                return null;
            }
        }
    }




    /**
     * string 变成bean
     * @param json
     * @param clazz
     * @param <T>
     * @return
     */
    public static<T> T string2Bean(String json,Class<T> clazz){
        if(StringUtils.isBlank(json) || clazz==null){
            return null;
        }
        if(clazz.equals(String.class)){
            return (T)json;
        }else {
            try {
                return objectMapper.readValue(json,clazz);
            } catch (IOException e) {
                e.printStackTrace();
                log.warn("parse  string to bean error",e);
                return null;
            }
        }
    }


    public static<T> T string2Bean(String json, TypeReference<T> reference){
        if(StringUtils.isBlank(json) || reference==null){
            return null;
        }
        Type type = reference.getType();
        if(type.equals(String.class)){
            return (T)json;
        }else {
            try {
                return objectMapper.readValue(json,reference);
            } catch (IOException e) {
                e.printStackTrace();
                log.warn("parse  string to bean error",e);
                return null;
            }
        }
    }

    public static<T> T string2Bean(String json,Class<?> collectionClazz,Class<?>...elementClazz){
        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(collectionClazz, elementClazz);
        try{
            return objectMapper.readValue(json,javaType);
        }catch (Exception e){
            e.printStackTrace();
            log.warn("parse  string to bean error",e);
            return null;
        }
    }




    public static void main(String[] args) {

        ArrayList<Object> list = Lists.newArrayList();
        User user1 = new User();
        user1.setId(1);
        user1.setEmail("111");
        User user2 = new User();
        user2.setId(1);
        user2.setEmail("111");
        list.add(user1);
        list.add(user2);

        String string = obj2String(list);
        System.out.println(string);

        List<User> users = string2Bean(string, new TypeReference<List<User>>() {});
        System.out.println(users);

        List<User> users2 = string2Bean(string, List.class, User.class);
        System.out.println(users2);


    }





}
