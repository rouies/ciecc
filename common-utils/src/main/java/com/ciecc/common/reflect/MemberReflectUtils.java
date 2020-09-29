package com.ciecc.common.reflect;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/*
 * Name      : MemberReflectUtils
 * Creator   : louis zhang
 * Function  : 类成员反射工具类
 * Date      : 2016-1-18
 */
public class MemberReflectUtils<T> {
	
	private T instance;
	
	private Class<T> clazz;
	
	private boolean isFieldBuffer;
	
	private boolean isMethodBuffer;
	
	private Map<String, Field> fieldBuffer;
	
	private Map<MethodInfo,Method> methodBuffer;
	
	private class MethodInfo{
				
		private Class<?>[] methodTypes;
		
		private String methodName;
		
		public Class<?>[] getMethodTypes() {
			return methodTypes;
		}

		public void setMethodTypes(Class<?>... methodTypes) {
			this.methodTypes = methodTypes;
		}

		public String getMethodName() {
			return methodName;
		}

		public void setMethodName(String methodName) {
			this.methodName = methodName;
		}
		
		@Override
		public boolean equals(Object obj) {
			boolean result = false;
			if(obj instanceof MemberReflectUtils.MethodInfo){
				@SuppressWarnings({ "unchecked", "rawtypes" })
				MethodInfo info = (MemberReflectUtils.MethodInfo) obj;
				if(this.methodName.equals(info.getMethodName())){
					Class<?>[] mts = info.getMethodTypes();
					if(mts == null){
						mts = new Class<?>[0];
					}
					if(mts.length == this.methodTypes.length){
						boolean isSuccess = true;
						for(int i=0,len = mts.length;i < len;i++){
							if(mts[i] != this.methodTypes[i]){
								isSuccess = false;
								break;
							}
						}
						result = isSuccess;
					}
				} 
			}
			return result;
		}
	}

	/*
	 * MethodName : 构造函数
	 * MethodType : instance
	 * Creator    : louis zhang
	 * Function   : 通过类成员构建对象，默认不开启缓存
	 * Arguments  : T instance  => 类成员实例
	 * Return     : 对象实例
	 */
	@SuppressWarnings("unchecked")
	public MemberReflectUtils(T instance){
		this.clazz = (Class<T>) instance.getClass();
		this.instance = instance;
		
	}

	/*
	 * MethodName : 构造函数
	 * MethodType : instance
	 * Creator    : louis zhang
	 * Function   : 通过类成员构建对象
	 * Arguments  : T instance  => 类成员实例
	 *              boolean useFieldBuffer  是否启用字段缓存
	 * 				boolean useMethodBuffer 是否启用方法缓存
	 * Return     : 对象实例
	 */
	public MemberReflectUtils(T instance,boolean useFieldBuffer,boolean useMethodBuffer){
		this(instance);
		this.isFieldBuffer = useFieldBuffer;
		this.isMethodBuffer = useMethodBuffer;
		if(isFieldBuffer){
			this.fieldBuffer = new ConcurrentHashMap<String, Field>(16);
		}
		
		if(isMethodBuffer){
			this.methodBuffer = new ConcurrentHashMap<MethodInfo,Method>(16);
		}
	}

	/*
	 * MethodName : getFieldInstance
	 * MethodType : instance
	 * Creator    : louis zhang
	 * Function   : 根据字段名获取字段实例
	 * Arguments  : String fieldName  => 字段名
	 * Return     : 字段实例
	 */
	private Field getFieldInstance(String fieldName) throws NoSuchFieldException, SecurityException {
		Field field = null;
		if(this.isFieldBuffer && this.fieldBuffer.containsKey(fieldName)){
			field = this.fieldBuffer.get(fieldName);
		} else {
			field = this.clazz.getDeclaredField(fieldName);
			if(this.isFieldBuffer){
				this.fieldBuffer.put(fieldName, field);
			}
		}
		return field;
	}

	/*
	 * MethodName : getMethodInstance
	 * MethodType : instance
	 * Creator    : louis zhang
	 * Function   : 根据方法名和方法参数列表获取方法实例
	 * Arguments  : String methodName  => 方法名
	 *              Class<?>...types   => 参数列表
	 * Return     : 方法实例
	 */
	private Method getMethodInstance(String methodName,Class<?>...types) throws NoSuchMethodException, SecurityException {
		Method method = null;
		MethodInfo info = new MethodInfo();
		info.setMethodName(methodName);
		info.setMethodTypes(types);
		if(this.isMethodBuffer && this.methodBuffer.containsKey(info)){
			method = this.methodBuffer.get(info);
		} else {
			method = this.clazz.getMethod(methodName, types);
			if(this.isMethodBuffer){
				this.methodBuffer.put(info, method);
			}
		}
		return method;
	}

	/*
	 * MethodName : getFieldValue
	 * MethodType : instance
	 * Creator    : louis zhang
	 * Function   : 根据字段名获取字段值并转换为指定的数据类型
	 * Arguments  : String fieldName  => 字段名
	 *              Class<?>  clazz  => 字段值数据类型
	 * Return     : 字段值
	 */
	@SuppressWarnings("unchecked")
	public <V> V getFieldValue(String fieldName,Class<V> clazz) throws ReflectException {
		try {
			
			return (V) getFieldValue(fieldName);
		} catch (Exception e) {
			throw new ReflectException(e.getMessage(),e);
		} 
	}

	/*
	 * MethodName : getFieldValue
	 * MethodType : instance
	 * Creator    : louis zhang
	 * Function   : 根据字段名获取字段值
	 * Arguments  : String fieldName  => 字段名
	 * Return     : 字段值
	 */
	public Object getFieldValue(String fieldName) throws ReflectException {
		try {
			Field field = this.getFieldInstance(fieldName);
			field.setAccessible(true);
			Object val = field.get(this.instance);
			return val;
		} catch (Exception e) {
			throw new ReflectException(e.getMessage(),e);
		} 
	}

	/*
	 * MethodName : setFieldValue
	 * MethodType : instance
	 * Creator    : louis zhang
	 * Function   : 给对象中的指定字段赋值
	 * Arguments  : String fieldName  => 字段名
	 *              Object value      => 字段值
	 * Return     : void
	 */
	public void setFieldValue(String fieldName,Object value) throws ReflectException {
		try {
			Field field = this.getFieldInstance(fieldName);
			field.setAccessible(true);
			field.set(this.instance, value);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ReflectException(e.getMessage(),e);
		} 
	}

	/*
	 * MethodName : 获取字段上指定类型的元数据
	 * MethodType : instance
	 * Creator    : louis zhang
	 * Function   : 给对象中的指定字段赋值
	 * Arguments  : String fieldName    => 字段名
	 *              Class<A> annotation => 元数据数据类型的Class实例
	 * Return     : 元数据实例
	 */
	public <A extends Annotation> A getFiledAnnotation(String fieldName,Class<A> annotation) throws ReflectException {
		try {
			Field field = this.getFieldInstance(fieldName);
			field.setAccessible(true);
			A result = field.getAnnotation(annotation);
			return result;
		} catch (Exception e) {
			throw new ReflectException(e.getMessage(),e);
		} 
	}

	/*
	 * MethodName : 获取字段上所有类型的元数据
	 * MethodType : instance
	 * Creator    : louis zhang
	 * Function   : 给对象中的指定字段赋值
	 * Arguments  : String fieldName    => 字段名
	 * Return     : 元数据实例数组
	 */
	public Annotation[] getFiledAnnotations(String fieldName) throws ReflectException {
		try {
			Field field = this.getFieldInstance(fieldName);
			field.setAccessible(true);
			Annotation[] result = field.getAnnotations();
			return result;
		} catch (Exception e) {
			throw new ReflectException(e.getMessage(),e);
		} 
	}

	/*
	 * MethodName : 获取类中所有字段的集合
	 * MethodType : instance
	 * Creator    : louis zhang
	 * Function   : 给对象中的指定字段赋值
	 * Arguments  :
	 * Return     : 字段名数组
	 */
	public String[] getAllFieldNames(){
		Field[] declaredFields = this.clazz.getDeclaredFields();
		String[] names = new String[declaredFields.length];
		for (int i=0,len =declaredFields.length;i<len;i++) {
			names[i] = declaredFields[i].getName();
			if(this.isFieldBuffer && !this.fieldBuffer.containsKey(names[i])){
				this.fieldBuffer.put(names[i], declaredFields[i]);
			}
		}
		return names;
	}

	/*
	 * MethodName : invoke
	 * MethodType : instance
	 * Creator    : louis zhang
	 * Function   : 执行类中指定方法
	 * Arguments  : String methodName  => 方法名
	 *              Class<?>[] types   => 方法参数类型列表
	 *              Object[] args      => 方法参数列表
	 * Return     : 执行结果
	 */
	public Object invoke(String methodName,Class<?>[] types,Object[] args) throws ReflectException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		Method method;
		try {
			method = this.getMethodInstance(methodName, types);
			
		} catch (Exception e) {
			throw new ReflectException(e.getMessage(),e);
		}
		return method.invoke(this.instance, args);
	}

	/*
	 * MethodName : invoke
	 * MethodType : instance
	 * Creator    : louis zhang
	 * Function   : 执行类中指定方法
	 * Arguments  : String   methodName  => 方法名
	 *              String[] types   => 方法参数类型类路径列表
	 *              Object[] args      => 方法参数列表
	 * Return     : 执行结果
	 */
	public Object invoke(String methodName,String[] types,Object[] args) throws ReflectException {
		Class<?>[] mtypes = null;
		Object result;
		try {
			if(types == null){
				types = new String[0];
			}
			mtypes = new Class<?>[types.length];
			for(int i=0,len = mtypes.length;i < len ;i++){
				mtypes[i] = ClassReflectUtils.getClassFromString(types[i]);
			}
			result = this.invoke(methodName, mtypes, args);
		} catch (Exception e) {
			throw new ReflectException(e.getMessage(),e);
		}
		return result;
	}

	/*
	 * MethodName : invoke
	 * MethodType : static
	 * Creator    : louis zhang
	 * Function   : 执行指定类中的指定静态方法
	 * Arguments  : Class<?> clazz       => 要执行方法的类
	 *              String   methodName  => 方法名
	 *              Class[] types       => 方法参数类型列表
	 *              Object[] args        => 方法参数列表
	 * Return     : 执行结果
	 */
	public static Object invoke(Class<?> clazz,String methodName,Class<?>[] types,Object[] args) throws ReflectException {
		Method method;
		try {
			method = clazz.getMethod(methodName, types);
		} catch (Exception e) {
			throw new ReflectException(e.getMessage(),e);
		}
		Object result;
		try {
			result = method.invoke(null, args);
		} catch (Exception e) {
			throw new ReflectException(e.getMessage(),e);
		} 
		return result;
	}

	/*
	 * MethodName : invoke
	 * MethodType : static
	 * Creator    : louis zhang
	 * Function   : 执行指定类中的指定静态方法
	 * Arguments  : Class<?> clazz       => 要执行方法的类
	 *              String   methodName  => 方法名
	 *              String[] types       => 方法参数类型类路径列表
	 *              Object[] args        => 方法参数列表
	 * Return     : 执行结果
	 */
	public static Object invoke(Class<?> clazz,String methodName,String[] types,Object[] args) throws ReflectException {
		Class<?>[] mtypes = null;
		try {
			if(types == null){
				types = new String[0];
			}
			mtypes = new Class<?>[types.length];
			for(int i=0,len = mtypes.length;i < len ;i++){
				mtypes[i] = ClassReflectUtils.getClassFromString(types[i]);
			}
		} catch (Exception e) {
			throw new ReflectException(e.getMessage(),e);
		}
		return invoke(clazz,methodName, mtypes, args);
	}

	/*
	 * MethodName : invoke
	 * MethodType : static
	 * Creator    : louis zhang
	 * Function   : 执行指定类中的指定静态方法
	 * Arguments  : String classPath       => 要执行方法的类的类路径
	 *              String   methodName    => 方法名
	 *              Class[] types          => 方法参数类型列表
	 *              Object[] args          => 方法参数列表
	 * Return     : 执行结果
	 */
	public static Object invoke(String classPath,String methodName,Class<?>[] types,Object[] args) throws ReflectException {
		Class<?> clazz;
		try {
			clazz = ClassReflectUtils.getClassFromString(classPath);
		} catch (ClassNotFoundException e) {
			throw new ReflectException(e.getMessage(),e);
		}
		return invoke(clazz,methodName,types,args);
	}

	/*
	 * MethodName : invoke
	 * MethodType : static
	 * Creator    : louis zhang
	 * Function   : 执行指定类中的指定静态方法
	 * Arguments  : Class<?> clazz       => 要执行方法的类的类路径
	 *              String   methodName  => 方法名
	 *              String[] types       => 方法参数类型类路径列表
	 *              Object[] args        => 方法参数列表
	 * Return     : 执行结果
	 */
	public static Object invoke(String classPath,String methodName,String[] types,Object[] args) throws ReflectException {
		Class<?> clazz;
		try {
			clazz = ClassReflectUtils.getClassFromString(classPath);
		} catch (ClassNotFoundException e) {
			throw new ReflectException(e.getMessage(),e);
		}
		return invoke(clazz,methodName,types,args);
	}
}
