package com.handong.oh318.core.javaimpl;

import java.util.List;

import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.InterfaceCapable;
import org.jboss.forge.roaster.model.JavaType;
import org.jboss.forge.roaster.model.JavaUnit;
import org.jboss.forge.roaster.model.source.FieldSource;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.source.JavaInterfaceSource;
import org.jboss.forge.roaster.model.source.JavaSource;
import org.jboss.forge.roaster.model.source.MethodSource;
import org.jboss.forge.roaster.model.source.VisibilityScopedSource;

import com.handong.oh318.core.AbsExtractor;
import com.handong.oh318.model.ClassId;
import com.handong.oh318.model.ClassType;
import com.handong.oh318.model.ClassUml;
import com.handong.oh318.model.FieldInfo;
import com.handong.oh318.model.MethodInfo;
import com.handong.oh318.model.ParameterInfo;
import com.handong.oh318.model.Visibility;

public abstract class AbsJavaExtractor extends AbsExtractor {

    public AbsJavaExtractor(String extractFromPath) {
        super(extractFromPath);
    }

    @Override
    public ClassUml getUmlSource(String rawData) {
        JavaUnit unit = Roaster.parseUnit(rawData);
        JavaType<?> javaType = unit.getGoverningType();
        var uml = ClassUml.builder();
        ClassUml result;

        if (javaType instanceof JavaClassSource jc) {
            populateClassInfo(uml, jc);
            result = uml.build();

            resolveParentSuperType(jc.getSuperType(), result);
            resolveParentInterfaces(jc, result);
        } else if (javaType instanceof JavaInterfaceSource ji) {
            populateInterfaceInfo(uml, ji);
            result = uml.build();

            resolveParentInterfaces(ji, result);
        } else {
            throw new IllegalArgumentException("Unsupported Java type: " + javaType.getClass());
        }
        sourceEntries.put(result.getUmlId(), result);
        return result;
    }

    private void populateInterfaceInfo(ClassUml.ClassUmlBuilder uml, JavaInterfaceSource interfaceSource) {
        uml.classId(new ClassId(interfaceSource.getName(), interfaceSource.getPackage()));
        uml.type(ClassType.INTERFACE);
        uml.visibility(getVisibility(interfaceSource));

        // Get implemented interfaces
        uml.methods(getMethods(interfaceSource.getMethods()));
    }

    private void populateClassInfo(ClassUml.ClassUmlBuilder uml, JavaClassSource classSource) {
        var classId = new ClassId(classSource.getName(), classSource.getPackage());
        uml.visibility(getVisibility(classSource));
        uml.classId(classId);
        uml.type(getJavaType(classSource));
        // Get fields
        uml.fields(getFields(classSource.getFields()));
        // Get methods
        uml.methods(getMethods(classSource.getMethods()));
    }

    private void resolveParentInterfaces(InterfaceCapable source, ClassUml uml) {
        var ifaces = source.getInterfaces();
        for (var iface : ifaces) {
            if (iface != null && !iface.equals("java.lang.Serializable")) {
                if (sourceEntries.containsKey(iface)) {
                    uml.getParent().add(sourceEntries.get(iface));
                } else {
                    unresolwedChains.add(new ChainEntry(iface, uml));
                }
            }
        }
    }

    private void resolveParentSuperType(String superType, ClassUml uml) {
        if (superType != null && !superType.equals("java.lang.Object")) {
            if (sourceEntries.containsKey(superType)) {
                uml.getParent().add(sourceEntries.get(superType));
            } else {
                unresolwedChains.add(new ChainEntry(superType, uml));
            }
        }
    }

    private <T extends JavaSource<T>> List<FieldInfo> getFields(List<FieldSource<T>> fieldsSource) {
        return fieldsSource.stream()
                .map(it -> new FieldInfo(getVisibility(it), it.getName(), it.getType().getName(), false))
                .toList();
    }

    private <T extends JavaSource<T>> List<MethodInfo> getMethods(List<MethodSource<T>> methodsSource) {
        return methodsSource.stream()
                .filter(it -> it.isPublic())
                .map(this::getMethod).toList();
    }

    private MethodInfo getMethod(MethodSource<?> methodSource) {
        return MethodInfo.builder()
                .visibility(getVisibility(methodSource))
                .name(methodSource.getName())
                .returnType(methodSource.getReturnType().getName())
                .argTypes(methodSource.getParameters().stream()
                        .map(it -> new ParameterInfo(it.getName(), it.getType().getName())).toList())
                .build();
    }

    private Visibility getVisibility(VisibilityScopedSource<?> source) {
        if (source.isPublic()) {
            return Visibility.PUBLIC;
        } else if (source.isProtected()) {
            return Visibility.PROTECTED;
        } else if (source.isPrivate()) {
            return Visibility.PRIVATE;
        }
        return Visibility.PRIVATE;
    }

    private ClassType getJavaType(JavaClassSource javaType) {
        if (javaType.isInterface()) {
            return ClassType.INTERFACE;
        } else if (javaType.isEnum()) {
            return ClassType.ENUM;
        } else if (javaType.isAnnotation()) {
            return ClassType.ANNOTATION;
        } else if (javaType.isAbstract()) {
            return ClassType.ABSTRACT_CLASS;
        } else {
            return ClassType.CLASS;
        }
    }

}
