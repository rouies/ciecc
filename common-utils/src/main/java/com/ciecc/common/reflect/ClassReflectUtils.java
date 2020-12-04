package com.ciecc.common.reflect;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;


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
	 * MethodType : static
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
	 * MethodType : static
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
	 * MethodType : static
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
	 * MethodType : static
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
	 * MethodType : static
	 * Creator    : louis zhang
	 * Function   : 获取类中实现的指定接口的泛型参数类型
	 * Arguments  : Class<?> clazz            =>  Class实例
	 *              Class<T> interfaceClass   =>  要提取参数的接口类型
	 * Return     : 类型列表
	 */
	public static <T>  Type[]  getGenericInterfaceType(Class<?> clazz,Class<T> interfaceClass){
		Type[] result = null;
		Type[] itypes = clazz.getGenericInterfaces();
		for (Type type : itypes)
		{
			ParameterizedType ptype = (ParameterizedType)type;
			if (ptype.getRawType() == interfaceClass)
			{
				result = ptype.getActualTypeArguments();
				break;
			}
		}
		return result;
	}

	/*
	 * MethodName : getGenericInterfaceType
	 * MethodType : static
	 * Creator    : louis zhang
	 * Function   : 获取类中实现的指定接口的泛型参数类型
	 * Arguments  : String classPath          =>  类路径
	 *              Class<T> interfaceClass   =>  要提取参数的接口类型
	 * Return     : 类型列表
	 */
	public static <T>  Type[]  getGenericInterfaceType(String classPath,Class<T> interfaceClass) throws ClassNotFoundException {
		Class<?> clazz = getClassFromString(classPath);
		return getGenericInterfaceType(clazz, interfaceClass);
	}

	/*
	 * MethodName : getGenericInterfaceType
	 * MethodType : static
	 * Creator    : louis zhang
	 * Function   : 获取类中实现的指定接口的泛型参数类型
	 * Arguments  : Object obj                =>  类实例
	 *              Class<T> interfaceClass   =>  要提取参数的接口类型
	 * Return     : 类型列表
	 */
	public static <T>  Type[]  getGenericInterfaceType(Object obj,Class<T> interfaceClass){
		return getGenericInterfaceType(obj.getClass(),interfaceClass);
	}


	/*
	 * MethodName : getAllClassByPackageName
	 * MethodType : static
	 * Creator    : louis zhang
	 * Function   : 获取指定包路径下的所有class
	 * Arguments  : String  packageName =>  包名
	 * 			    Boolean recursive   =>  是否包含子包中的类
	 * Return     : 包含所有class的集合
	 */
	public static List<Class<?>> getAllClassByPackageName(String packageName,boolean recursive) throws IOException {
		//转换路径
		String path = packageName.replace('.','/');
		Enumeration<URL> resources = null;
		resources = Thread.currentThread().getContextClassLoader().getResources(path);
		List<Class<?>> classes = new ArrayList<Class<?>>();
		while (resources.hasMoreElements()){
			//遍历资源
			URL url = resources.nextElement();
			String protocol = url.getProtocol();
			if("file".equals(protocol)){ //文件资源
				//获取包的物理路径
				String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
				//以文件的方式扫描整个包下的文件 并添加到集合中
				findAndAddClassesInPackageByFile(packageName, filePath, recursive,classes);
			} else if("jar".equals(protocol)){ //jar包资源
				findAndAddClassesInPackageByJar(packageName,url,recursive,classes);
			}
		}
		return classes;
	}


	public static void main(String[] args) throws IOException {
		List<Class<?>> allClassByPackageName = getAllClassByPackageName("java.io", true);
	}

	/**
	 * 递归获取指定Jar文件包路径下的所有class文件
	 * @param packageName
	 * @param url
	 * @param recursive
	 * @param classes
	 */
	private static void findAndAddClassesInPackageByJar(String packageName, URL url,boolean recursive,List<Class<?>> classes) {
		JarFile jar = null;
		String packageDirName = packageName.replace('.', '/');
		try {
			//获取jar实例
			jar = ((JarURLConnection) url.openConnection()).getJarFile();
			//从此jar包 得到一个枚举类
			Enumeration<JarEntry> entries = jar.entries();
			//同样的进行循环迭代
			while (entries.hasMoreElements()) {
				//获取jar里的一个实体 可以是目录 和一些jar包里的其他文件 如META-INF等文件
				JarEntry entry = entries.nextElement();
				String name = entry.getName();
				//如果是以/开头的
				if (name.charAt(0) == '/') {
					//获取后面的字符串
					name = name.substring(1);
				}
				//如果前半部分和定义的包名相同
				if (name.startsWith(packageDirName)) {
					int idx = name.lastIndexOf('/');
					//如果以"/"结尾 是一个包
					if (idx != -1) {
						//获取包名 把"/"替换成"."
						packageName = name.substring(0, idx).replace('/', '.');
					}
					//如果可以迭代下去 并且是一个包
					if ((idx != -1) || recursive) {
						//如果是一个.class文件 而且不是目录
						if (name.endsWith(".class") && !entry.isDirectory()) {
							//去掉后面的".class" 获取真正的类名
							String className = name.substring(packageName.length() + 1, name.length() - 6);
							try {
								//添加到classes
								classes.add(Class.forName(packageName + '.' + className));
							} catch (ClassNotFoundException e) {
								e.printStackTrace();
							}
						}
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();

		}
	}


	/**
	 * 递归获取指定文件目录下的所有class文件
	 * @param packageName
	 * @param filePath
	 * @param recursive
	 * @param classes
	 */
	private static void findAndAddClassesInPackageByFile(String packageName, String filePath, boolean recursive, List<Class<?>> classes) {
		File dir = new File(filePath);
		//不存在则返回
		if (!dir.exists() || !dir.isDirectory()) {
			return;
		}

		//如果存在 就获取包下的所有文件 包括目录
		File[] dirfiles = dir.listFiles(new FileFilter() {
			//自定义过滤规则 如果可以循环(包含子目录) 或则是以.class结尾的文件(编译好的java类文件)
			public boolean accept(File file) {
				//只抓取目录和class文件
				return (recursive && file.isDirectory()) || (file.getName().endsWith(".class"));
			}
		});
		//循环所有文件
		for (File file : dirfiles) {
			if (file.isDirectory()) {
				//递归继续解析
				findAndAddClassesInPackageByFile(packageName + "." + file.getName(),file.getAbsolutePath(),recursive,classes);
			} else {
				//如果是java类文件 去掉后面的.class 只留下类名
				String className = file.getName().substring(0, file.getName().length() - 6);
				try {
					//添加到集合中去
					classes.add(Class.forName(packageName + '.' + className));
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}

			}

		}
	}

}
