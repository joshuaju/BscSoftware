package external;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import exceptions.ExceptionHandler;
import exceptions.KeywordLibraryException;

public class Keyword {
	
	private final annotations.Keyword annotation;
	private final Method method;
	
	public Keyword(Method method) throws KeywordLibraryException{
		if (!Keyword.isKeywordMethod(method)){
			throw ExceptionHandler.MethodIsNotAKeywordMethod(method.getClass().getName() + "." + method.getName());
		}
		this.annotation = method.getAnnotation(annotations.Keyword.class);		
		this.method = method;
	}
	
	public String getName(){
		return annotation.Name();
	}
	
	public String getDescription(){
		return annotation.Description();
	}
	
	public String getParameter(){
		return annotation.Parameter();
	}
	
	public String getReturn(){
		return annotation.Return();
		
	}
	
	public Class<?>[] getParameterTypes(){
		return method.getParameterTypes();
	}
	
	public Class<?> getReturntype(){
		return method.getReturnType();
	}
	
	public Object invoke(Object instance, Object...args) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		return method.invoke(instance, args);
	}
	
	@Override
	public String toString() {		
		return "[NAME] " + getName() + ", [DESC] " + getDescription() + ", [PARA] " + getParameter() + ", [RET] " + getReturn();
	}
		
	public static boolean isKeywordMethod(Method method){
		annotations.Keyword anno = method.getAnnotation(annotations.Keyword.class);
		if (anno != null){
			return true;
		} else {
			return false;
		}
	}
	
	public static boolean isNameEqual(Keyword keyword, String name){
		return keyword.getName().equals(name);
	}
	
}
