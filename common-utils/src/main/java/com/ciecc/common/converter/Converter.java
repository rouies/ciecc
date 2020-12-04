package com.ciecc.common.converter;

import com.ciecc.common.reflect.ClassReflectUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/*
 * Name      : Converter
 * Creator   : louis zhang
 * Function  : 类型转换器
 * Date      : 2016-1-18
 */
public class Converter {

    public static  Converter INSTANCE = new Converter();

    /**
     * 类型转换执行器
     */
    private class Executor {
        public Executor(Direction direction,IConverter converter){
            this.direction = direction;
            this.converter = converter;
        }

        private Direction direction;
        private IConverter converter;

        public Object execute(Object obj){
            return direction.execute(obj,converter);
        }
    }

    /**
     * 转换器封装
     */
    private class ConverterInfo{

        public ConverterInfo(Class<?> sourecClass,Class<?> targetClass,IConverter converter){
            this.sourecClass = sourecClass;
            this.targetClass = targetClass;
            this.converter = converter;
        }

        private Class<?> sourecClass;
        private Class<?> targetClass;
        private IConverter converter;
    }

    private ReadWriteLock rwLock = new ReentrantReadWriteLock();

    /**
     * 索引与类型对应关系
     */
    private Map<Class<?>,Integer> indexMapping = new HashMap<Class<?>,Integer>();

    /**
     * 转换器信息集合
     */
    private List<ConverterInfo> converterInfos = new ArrayList<ConverterInfo>();

    /**
     * 执行器邻接矩阵
     */
    private Executor[][] executors = null;

    /*
     * MethodName : rebuildExecutors
     * MethodType : instance
     * Creator    : louis zhang
     * Function   : 重建邻接矩阵
     */
    private void rebuildExecutors(){
        //初始化邻接矩阵
        this.executors = new Executor[this.indexMapping.size()][this.indexMapping.size()];
        for(ConverterInfo converter : this.converterInfos){
            Class fromClass = converter.sourecClass;
            Class toClass = converter.targetClass;
            this.executors[this.indexMapping.get(fromClass)][this.indexMapping.get(toClass)] = new Executor(Direction.TO,converter.converter);
            this.executors[this.indexMapping.get(toClass)][this.indexMapping.get(fromClass)] = new Executor(Direction.FROM,converter.converter);
        }
    }

    /*
     * MethodName : register
     * MethodType : instance
     * Creator    : louis zhang
     * Function   : 实现转换器注册,每次调用会重新构建邻接矩阵，因此尽量一次全部注册
     * Arguments  : IConverter[] converters     => 要注册的转换器
     */
    public void register(IConverter...converters) throws ConvertException{
        this.rwLock.writeLock().lock();
        try {
            int index = 0;
            this.indexMapping.clear();
            for(IConverter converter: converters){
                Type[] types = ClassReflectUtils.getGenericInterfaceType(converter, IConverter.class);
                Class fromClass = (Class) types[0];
                Class toClass = (Class) types[1];
                if(this.indexMapping.containsKey(fromClass) && this.indexMapping.containsKey(toClass)){
                    continue;
                } else {
                    if(!this.indexMapping.containsKey(fromClass)){
                        this.indexMapping.put(fromClass,index++);
                    }
                    if(!this.indexMapping.containsKey(toClass)){
                        this.indexMapping.put(toClass,index++);
                    }
                    this.converterInfos.add(new ConverterInfo(fromClass,toClass,converter));
                }
            }
           this.rebuildExecutors();
        } catch (Exception ex){
            throw new ConvertException("注册转换器失败",ex);
        } finally {
            this.rwLock.writeLock().unlock();
        }
    }

    /*
     * MethodName : convert
     * MethodType : instance
     * Creator    : louis zhang
     * Function   : 进行数据类型转换
     * Arguments  : Object source        => 要转换的数据类型
     * 				Class<T> targetClass => 要转换的对应类型
     * Return     : 转换后的类型实例
     */
    public <T> T convert(Object source,Class<T> targetClass) throws ConvertException{
        if(source == null){
            throw new ConvertException("要转换的对象不能为null");
        }
        Class<?> sourceClass = source.getClass();
        if(sourceClass == targetClass){
            return (T)source;
        }

        int sIndex = -1;
        int tIndex = -1;
        if(this.indexMapping.containsKey(sourceClass)){
            sIndex = this.indexMapping.get(sourceClass);
        } else {
            throw new ConvertException("没有找到对应类型的转换器");
        }

        if(this.indexMapping.containsKey(targetClass)){
            tIndex = this.indexMapping.get(targetClass);
        } else {
            throw new ConvertException("没有找到对应类型的转换器");
        }

        Executor executor = this.executors[sIndex][tIndex];
        Object res = executor.execute(source);
        if(res == null){
            return null;
        } else {
            return (T) res;
        }
    }
}
