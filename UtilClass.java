import java.lang.reflect.Method;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import javax.swing.text.html.HTMLEditorKit;

public class UtilClass {
	public static void main(String[] args) {
		Class<?> classInstance = HTMLEditorKit.class;

		System.out.println("Inheritance depth: " + calculateInheritanceDepth(classInstance));
		System.out.println("Number of children : " + getNumberOfChildren(classInstance));
		System.out.println("MIF percent : " + getMIF(classInstance));
		System.out.println("AIF percent : " + getAIF(classInstance));
		System.out.println("MHF percent : " + getMHF(classInstance));
		System.out.println("AHF percent : " + getAHF(classInstance));
		System.out.println("POF percent : " + getPOF(classInstance));
	}

	public static int calculateInheritanceDepth(Class<?> classInstance) {
		int depth = 0;
		while (classInstance != null && classInstance != Object.class) {
			depth++;
			classInstance = classInstance.getSuperclass();
		}
		return depth;
	}

	public static int getNumberOfChildren(Class<?> classInstance) {
		int number = 0;
		Class<?>[] childrenClasses = classInstance.getClasses();
		number += childrenClasses.length;
		for (Class<?> child : childrenClasses) {
			number += getNumberOfChildren(child);
		}

		return number;
	}

	public static double getMIF(Class<?> classInstance) {
		Method[] declaredMethods = classInstance.getDeclaredMethods();
		Method[] allMethods = classInstance.getMethods();
		int numberOfDeclaredMethods = declaredMethods.length;
		int numberOfAllMethods = allMethods.length;
		Class<?>[] childrenClasses = classInstance.getClasses();
		for (Class<?> child : childrenClasses) {
			Method[] declaredChildMethods = child.getDeclaredMethods();
			Method[] allChildMethods = child.getMethods();
			numberOfDeclaredMethods += declaredChildMethods.length;
			numberOfAllMethods += allChildMethods.length;
		}

		return numberOfDeclaredMethods * 100 / numberOfAllMethods;
	}

	public static double getAIF(Class<?> classInstance) {
		Field[] declaredFields = classInstance.getDeclaredFields();
		Field[] allFields = classInstance.getFields();
		int numberOfDeclaredFields = declaredFields.length;
		int numberOfAllFields = allFields.length;
		Class<?>[] childrenClasses = classInstance.getClasses();
		for (Class<?> child : childrenClasses) {
			Field[] declaredChildFields = child.getDeclaredFields();
			Field[] allChildFields = child.getFields();
			numberOfDeclaredFields += declaredChildFields.length;
			numberOfAllFields += allChildFields.length;
		}

		return numberOfDeclaredFields * 100 / numberOfAllFields;
	}

	public static double getMHF(Class<?> classInstance) {
		Method[] allMethods = classInstance.getDeclaredMethods();

		int numberOfHiddenMethods = countHiddenMethods(allMethods);
		int numberOfVisibleMethods = allMethods.length - numberOfHiddenMethods;
		Class<?>[] childrenClasses = classInstance.getClasses();
		for (Class<?> child : childrenClasses) {
			Method[] childMethods = child.getDeclaredMethods();
			int hiddenMethodsCount = countHiddenMethods(childMethods);
			numberOfHiddenMethods += hiddenMethodsCount;
			numberOfVisibleMethods += childMethods.length - hiddenMethodsCount;
		}
		System.out.println("numberOfHiddenMethods : " + numberOfHiddenMethods);
		System.out.println("numberOfVisibleMethods : " + numberOfVisibleMethods);
		return numberOfHiddenMethods * 100 / numberOfVisibleMethods;
	}

	public static double getAHF(Class<?> classInstance) {
		Field[] allFields = classInstance.getDeclaredFields();
		int numberOfHiddenFields = countHiddenFields(allFields);
		int numberOfVisibleFields = allFields.length - numberOfHiddenFields;

		Class<?>[] childrenClasses = classInstance.getClasses();
		for (Class<?> child : childrenClasses) {
			Field[] childFields = child.getFields();
			int hiddenFieldsCount = countHiddenFields(childFields);
			numberOfHiddenFields += hiddenFieldsCount;
			numberOfVisibleFields += childFields.length - hiddenFieldsCount;
		}
		System.out.println("numberOfHiddenFields : " + numberOfHiddenFields);
		System.out.println("numberOfVisibleFields : " + numberOfVisibleFields);
		return numberOfHiddenFields * 100 / numberOfVisibleFields;
	}

	public static double getPOF(Class<?> classInstance) {
		Method[] allDeclaredMethods = classInstance.getDeclaredMethods();
		Method[] allMethods = classInstance.getMethods();
		int inheritedCount = allMethods.length - allDeclaredMethods.length;
		return (inheritedCount * 100) / (allDeclaredMethods.length * calculateInheritanceDepth(classInstance));
	}

	private static int countHiddenMethods(Method[] methods) {
		int count = 0;
		for (Method method : methods) {
			if (Modifier.isPrivate(method.getModifiers())) {
				count++;
			}
		}

		return count;
	}

	private static int countHiddenFields(Field[] fields) {
		int count = 0;
		for (Field field : fields) {
			if (Modifier.isPrivate(field.getModifiers())) {
				count++;
			}
		}

		return count;
	}
}