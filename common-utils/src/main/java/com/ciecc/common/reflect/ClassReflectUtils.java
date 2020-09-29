package com.ciecc.common.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;


/*
 * Name      : ClassReflectUtils
 * Creator   : louis zhang
 * Function  : 相关Class功能反射工具类
 * Date      : 2016-1-18
 */
public class ClassReflectUtils {

	/**
	 * 基础数据类型反射注入
	 */
	private static HashMap<String, Class<?>> baseType = new HashMap<String, Class<?>>();
	
	static {
		baseType.put("int", int.class);
		baseType.put("short", short.class);
		baseType.put("byte", byte.class);
		baseType.put("boolean", boolean.class);
		baseType.put("long", long.class);
		baseType.put("float", float.class);
		baseType.put("double", double.class);
		baseType.put("char", char.class);
	}

	/*
	 * MethodName : getClassFromString
	 * MethodType : instance
	 * Creator    : louis zhang
	 * Function   : 根据ClassPath获取对应Class对象实例实例
	 * Arguments  : String path  => 类路径
	 * Return     : 对应的Class实例
	 */
	public static Class<?> getClassFromString(String path) throws ClassNotFoundException{
		if(baseType.containsKey(path)){
			return baseType.get(path);
		} else {
			return Class.forName(path);
		}
	}

	/*
	 * MethodName : getInstance
	 * MethodType : instance
	 * Creator    : louis zhang
	 * Function   : 根据Class对象使用无参默认构造函数创建对象
	 * Arguments  : Class<T> clazz  => Class实例
	 * Return     : 对象实例
	 */
	public static <T> T getInstance(Class<T> clazz) throws ReflectException{
		try {
			return clazz.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
			throw new ReflectException(e.getMessage(),e);
		} 
	}


	/*
	 * MethodName : getInstance
	 * MethodType : instance
	 * Creator    : louis zhang
	 * Function   : 根据ClassPath路径，使用无参默认构造函数创建对象
	 * Arguments  : String classPath  => 类路径
	 * Return     : 对象实例
	 */
	public static Object getInstance(String classPath) throws ReflectException{
		try {
			Class<?> clazz = getClassFromString(classPath);
			return clazz.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
			throw new ReflectException(e.getMessage(),e);
		} 
	}

	/*
	 * MethodName : getInstance
	 * MethodType : instance
	 * Creator    : louis zhang
	 * Function   : 根据Class实例，使用指定构造函数创建对象
	 * Arguments  : Class<T> clazz    =>  Class实例
	 *              Class<?>[] types  =>  构造函数参数类型列表
	 *              Object[] args     =>  构造函数参数列表
	 * Return     : 对象实例
	 */
	public static <T> T getInstance(Class<T> clazz,Class<?>[] types,Object[] args) throws ReflectException{
		try {
			Constructor<T> constructor = clazz.getConstructor(types);
			if(types == null){
				types = new Class[0];
			}
			if(args == null){
				args = new Object[0];
			}
			return constructor.newInstance(args);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ReflectException(e.getMessage(),e);
		}
	}


	/*
	 * MethodName : getInstance
	 * MethodType : instance
	 * Creator    : louis zhang
	 * Function   : 根据ClassPath实例，使用指定构造函数创建对象
	 * Arguments  : String classPath  =>  类路径
	 *              Class<?>[] types  =>  构造函数参数类型列表
	 *              Object[] args     =>  构造函数参数列表
	 * Return     : 对象实例
	 */
	public static Object getInstance(String classPath,Class<?>[] types,Object[] args) throws ReflectException{
		try {
			Class<?> clazz = ClassReflectUtils.getClassFromString(classPath);
			if(types == null){
				types = new Class[0];
			}
			if(args == null){
				args = new Object[0];
			}
			return ClassReflectUtils.getInstance(clazz, types, args);
		} catch (Exception e) {
			throw new ReflectException(e.getMessage(),e);
		}
	}

	/*
	 * MethodName : getInstance
	 * MethodType : instance
	 * Creator    : louis zhang
	 * Function   : 根据ClassPath实例，使用指定构造函数创建对象
	 * Arguments  : Class<T> clazz    =>  Class实例
	 *              String[] types    =>  构造函数参数类型ClassPath列表
	 *              Object[] args     =>  构造函数参数列表
	 * Return     : 对象实例
	 */
	public static <T> T getInstance(Class<T> clazz,String[] types,Object[] args) throws ReflectException{
		try {
			Class<?>[] argsType = null;
			if(types == null){
				argsType = new Class[0];
			} else {
				argsType = new Class[types.length];
			}
			if(args == null){
				args = new Object[0];
			}
			for (int i = 0,len= types.length; i < len; i++) {
				argsType[i] = ClassReflectUtils.getClassFromString(types[i]);
			}
			return ClassReflectUtils.getInstance(clazz, argsType, args);
		} catch (Exception e) {
			throw new ReflectException(e.getMessage(),e);
		}
	}

	/*
	 * MethodName : getInstance
	 * MethodType : instance
	 * Creator    : louis zhang
	 * Function   : 根据ClassPath实例，使用指定构造函数创建对象
	 * Arguments  : String classPath  =>  类路径
	 *              String[] types    =>  构造函数参数类型ClassPath列表
	 *              Object[] args     =>  构造函数参数列表
	 * Return     : 对象实例
	 */
	public static Object getInstance(String classPath,String[] types,Object[] args) throws ReflectException{
		try {
			Class<?> clazz = ClassReflectUtils.getClassFromString(classPath);
			return ClassReflectUtils.getInstance(clazz, types, args);
		} catch (ClassNotFoundException e) {
			throw new ReflectException(e.getMessage(),e);
		}
			
	}

	/*
	 * MethodName : getGenericInterfaceType
	 * MethodType : instance
	 * Creator    : louis zhang
	 * Function   : 获取类中实现的指定接口的泛型参数类型
	 * Arguments  : Class<?> clazz            =>  Class实例
	 *              Class<T> interfaceClass   =>  要提取参数的接口类型
	 * Return     : 类型列表
	 */
	public static <T>  Type[]  getGenericInterfaceType(Class<?> clazz,Class<T> interfaceClass){
		Type[] result = null;
		Type[] itypes = clazz.getGenericInterfaces();
		for (Type type: itypes) {
			ParameterizedType ptype = (ParameterizedType)type;
			if(ptype.getRawType()  == interfaceClass){
				result = ptype.getActualTypeArguments();
				break;
			}
		}
		return result;
	}

	/*
	 * MethodName : getGenericInterfaceType
	 * MethodType : instance
	 * Creator    : louis zhang
	 * Function   : 获取类中实现的指定接口的泛型参数类型
	 * Arguments  : String classPath          =>  类路径
	 *              Class<T> interfaceClass   =>  要提取参数的接口类型
	 * Return     : 类型列表
	 */
	public static <T>  Type[]  getGenericInterfaceType(String classPath,Class<T> interfaceClass) throws ClassNotFoundException {
		Class<?> clazz = getClassFromString(classPath);
		return getGenericInterfaceType(clazz,interfaceClass);

	}

	/*
	 * MethodName : getGenericInterfaceType
	 * MethodType : instance
	 * Creator    : louis zhang
	 * Function   : 获取类中实现的指定接口的泛型参数类型
	 * Arguments  : Object obj                =>  类实例
	 *              Class<T> interfaceClass   =>  要提取参数的接口类型
	 * Return     : 类型列表
	 */
	public static <T>  Type[]  getGenericInterfaceType(Object obj,Class<T> interfaceClass){
		return getGenericInterfaceType(obj.getClass(),interfaceClass);
	}
}
