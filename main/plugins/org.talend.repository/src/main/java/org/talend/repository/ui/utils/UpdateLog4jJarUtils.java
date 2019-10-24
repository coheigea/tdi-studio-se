package org.talend.repository.ui.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.model.general.ModuleNeeded;
import org.talend.core.model.process.IProcess;
import org.talend.core.model.process.IProcess2;
import org.talend.core.model.properties.Property;
import org.talend.core.runtime.maven.MavenUrlHelper;
import org.talend.core.runtime.process.LastGenerationInfo;
import org.talend.designer.runprocess.IRunProcessService;

public class UpdateLog4jJarUtils {

    public static boolean addLog4jToJarList2(Collection<String> jarList, boolean isSelectLog4j2) {
        List<String> moduleNeededList = new ArrayList<String>();
        List<String> moduleDeleteList = new ArrayList<String>();
        if (isSelectLog4j2) {
            boolean foundLog4j2CoreJar = false;
            boolean foundLog4j2ApiJar = false;
//            boolean foundLog4j2AdapterJar = false;
            for (String jar : jarList) {
                if (jar.matches("log4j-\\d+\\.\\d+\\.\\d+\\.jar")) { //$NON-NLS-1$
                    moduleDeleteList.add(jar);
                }
                if (jar.matches("log4j-core-\\d+\\.\\d+\\.\\d+\\.jar")) { //$NON-NLS-1$
                    foundLog4j2CoreJar = true;
                }
                if (jar.matches("log4j-api-\\d+\\.\\d+\\.\\d+\\.jar")) { //$NON-NLS-1$
                    foundLog4j2ApiJar = true;
                }
//                if (jar.matches("log4j-\\d+\\.\\d+\\-api-2.12.1.jar")) { //$NON-NLS-1$
//                    foundLog4j2AdapterJar = true;
//                }
            }
            if (!foundLog4j2CoreJar) {
                moduleNeededList.add("log4j-core-2.12.1.jar");//$NON-NLS-1$

            }
            if (!foundLog4j2ApiJar) {
                moduleNeededList.add("log4j-api-2.12.1.jar");//$NON-NLS-1$
            }
//            if (!foundLog4j2AdapterJar) {
//                moduleNeededList.add("log4j-1.2-api-2.12.1.jar");//$NON-NLS-1$
//            }

        } else {
            boolean foundLog4jJar = false;
            for (String jar : jarList) {
                if (jar.matches("log4j-\\d+\\.\\d+\\.\\d+\\.jar")) { //$NON-NLS-1$
                    foundLog4jJar = true;
                }
                if (jar.matches("log4j-core-\\d+\\.\\d+\\.\\d+\\.jar")) { //$NON-NLS-1$
                    moduleDeleteList.add(jar);
                }
                if (jar.matches("log4j-api-\\d+\\.\\d+\\.\\d+\\.jar")) { //$NON-NLS-1$
                    moduleDeleteList.add(jar);
                }
//                if (jar.matches("log4j-\\d+\\.\\d+\\-api-2.12.1.jar")) { //$NON-NLS-1$
//                    moduleDeleteList.add(jar);
//                }
            }
            if (!foundLog4jJar) {
                moduleNeededList.add("log4j-1.2.17.jar");//$NON-NLS-1$
            }

        }
        jarList.removeAll(moduleDeleteList);
        jarList.addAll(moduleNeededList);

        return moduleNeededList.size() > 0;
    }

    public static boolean addLog4jToModuleList2(Collection<ModuleNeeded> jarList, boolean isSelectLog4j2) {
        List<ModuleNeeded> moduleNeededList = new ArrayList<ModuleNeeded>();
        List<ModuleNeeded> moduleDeleteList = new ArrayList<ModuleNeeded>();
        if (isSelectLog4j2) {
            boolean foundLog4j2CoreJar = false;
            boolean foundLog4j2ApiJar = false;
//            boolean foundLog4j2AdapterJar = false;
            for (ModuleNeeded jar : jarList) {
                if (jar.getModuleName().matches("log4j-\\d+\\.\\d+\\.\\d+\\.jar")) { //$NON-NLS-1$
                    moduleDeleteList.add(jar);
                }
                if (jar.getModuleName().matches("log4j-core-\\d+\\.\\d+\\.\\d+\\.jar")) { //$NON-NLS-1$
                    foundLog4j2CoreJar = true;
                }
                if (jar.getModuleName().matches("log4j-api-\\d+\\.\\d+\\.\\d+\\.jar")) { //$NON-NLS-1$
                    foundLog4j2ApiJar = true;
                }
//                if (jar.getModuleName().matches("log4j-\\d+\\.\\d+\\-api-2.12.1.jar")) { //$NON-NLS-1$
//                    foundLog4j2AdapterJar = true;
//                }
            }

            if (!foundLog4j2CoreJar) {
                ModuleNeeded log4jCore = new ModuleNeeded("org.apache.logging.log4j", "log4j-core-2.12.1.jar", null, true); //$NON-NLS-1$ //$NON-NLS-2$
                log4jCore.setMavenUri("mvn:org.apache.logging.log4j/log4j-core/2.12.1");//$NON-NLS-1$
                moduleNeededList.add(log4jCore);

            }
            if (!foundLog4j2ApiJar) {
                ModuleNeeded log4jApi = new ModuleNeeded("org.apache.logging.log4j", "log4j-api-2.12.1.jar", null, true); //$NON-NLS-1$ //$NON-NLS-2$
                log4jApi.setMavenUri("mvn:org.apache.logging.log4j/log4j-api/2.12.1");//$NON-NLS-1$
                moduleNeededList.add(log4jApi);
            }
//            if (!foundLog4j2AdapterJar) {
//                ModuleNeeded log4jCore = new ModuleNeeded("org.apache.logging.log4j", "log4j-1.2-api-2.12.1.jar", null, true); //$NON-NLS-1$ //$NON-NLS-2$
//                log4jCore.setMavenUri("mvn:org.apache.logging.log4j/log4j-1.2-api/2.12.1");//$NON-NLS-1$
//                moduleNeededList.add(log4jCore);
//            }

        } else {
            boolean foundLog4jJar = false;
            for (ModuleNeeded jar : jarList) {
                if (jar.getModuleName().matches("log4j-\\d+\\.\\d+\\.\\d+\\.jar")) { //$NON-NLS-1$
                    foundLog4jJar = true;
                }
                if (jar.getModuleName().matches("log4j-core-\\d+\\.\\d+\\.\\d+\\.jar")) { //$NON-NLS-1$
                    moduleDeleteList.add(jar);
                }
                if (jar.getModuleName().matches("log4j-api-\\d+\\.\\d+\\.\\d+\\.jar")) { //$NON-NLS-1$
                    moduleDeleteList.add(jar);
                }
//                if (jar.getModuleName().matches("log4j-\\d+\\.\\d+\\-api-2.12.1.jar")) { //$NON-NLS-1$
//                    moduleDeleteList.add(jar);
//                }
            }
            if (!foundLog4jJar) {
                ModuleNeeded log4j = new ModuleNeeded("log4j", "log4j-1.2.17.jar", null, true); //$NON-NLS-1$ //$NON-NLS-2$
                log4j.setMavenUri("mvn:log4j/log4j/1.2.17");//$NON-NLS-1$
                moduleNeededList.add(log4j);
            }

        }

        jarList.removeAll(moduleDeleteList);
        jarList.addAll(moduleNeededList);

        return moduleNeededList.size() > 0;
    }

    public static void addLog4jToJarList(Collection<String> jarList, boolean isSelectLog4j2) {
        IProcess2 process = null;
        if (GlobalServiceRegister.getDefault().isServiceRegistered(IRunProcessService.class)) {
            IRunProcessService processService = (IRunProcessService) GlobalServiceRegister.getDefault()
                    .getService(IRunProcessService.class);
            IProcess activeProcess = processService.getActiveProcess();
            if (activeProcess instanceof IProcess2) {
                process = (IProcess2) activeProcess;
            }
        }
        List<String> modulesUsedBefore = removeLog4jFromJarListAndGetUsedJarBefore(process, jarList);
        addBackJars(jarList, isSelectLog4j2, modulesUsedBefore);
    }

    public static void addLog4jToModuleList(Collection<ModuleNeeded> jarList, boolean isSelectLog4j2) {
        IProcess2 process = null;
        if (GlobalServiceRegister.getDefault().isServiceRegistered(IRunProcessService.class)) {
            IRunProcessService processService = (IRunProcessService) GlobalServiceRegister.getDefault()
                    .getService(IRunProcessService.class);
            IProcess activeProcess = processService.getActiveProcess();
            if (activeProcess instanceof IProcess2) {
                process = (IProcess2) activeProcess;
            }
        }
        List<ModuleNeeded> modulesUsedBefore = removeLog4jFromModuleListAndGetModulesUsedBefore(process, jarList);
        addBackModules(jarList, isSelectLog4j2, modulesUsedBefore);
    }

    private static void addBackJars(Collection<String> moduleNeededList, boolean isSelectLog4j2, List<String> modulesUsedBefore) {
        if (isSelectLog4j2) {
            boolean usedlog4jJclBefore = false;
            boolean usedlog4jJulBefore = false;
            for (String module : modulesUsedBefore) {
                if (module.matches("log4j-jcl-\\d+\\.\\d+\\.\\d+\\.jar")) { //$NON-NLS-1$
                    usedlog4jJclBefore = true;
                }
                if (module.matches("log4j-jul-\\d+\\.\\d+\\.\\d+\\.jar")) { //$NON-NLS-1$
                    usedlog4jJulBefore = true;
                }
            }
//            org.apache.logging.log4j:log4j-jcl:jar (only if this jar was setup in the job before)
            if (usedlog4jJclBefore) {
                moduleNeededList.add("log4j-jcl-2.12.1.jar");
            }
//            org.apache.logging.log4j:log4j-jul:jar (only if this jar was setup in the job before)
            if (usedlog4jJulBefore) {
                moduleNeededList.add("log4j-jul-2.12.1.jar");
            }
//            org.apache.logging.log4j:log4j-slf4j-impl:jar
            moduleNeededList.add("log4j-slf4j-impl-2.12.1.jar");
//            org.apache.logging.log4j:log4j-api:jar
            moduleNeededList.add("log4j-api-2.12.1.jar");
//            org.apache.logging.log4j:log4j-core:jar
            moduleNeededList.add("log4j-core-2.12.1.jar");
            // log4j-1.2
            moduleNeededList.add("log4j-1.2-api-2.12.1.jar");
        } else {
            boolean usedjclOverSlf4jBefore = false;
            boolean usedjulToSlf4jBefore = false;
            for (String module : modulesUsedBefore) {
                if (module.matches("jcl-over-slf4j-\\d+\\.\\d+\\.\\d+\\.jar")) { //$NON-NLS-1$
                    usedjclOverSlf4jBefore = true;
                }
                if (module.matches("jul-to-slf4j-\\d+\\.\\d+\\.\\d+\\.jar")) { //$NON-NLS-1$
                    usedjulToSlf4jBefore = true;
                }
            }
//            org.slf4j:jcl-over-slf4j:jar (only if this jar was setup in the job before)
            if (usedjclOverSlf4jBefore) {
                moduleNeededList.add("jcl-over-slf4j-1.7.25.jar");
            }
//            org.slf4j:jul-to-slf4j:jar (only if this jar was setup in the job before)
            if (usedjulToSlf4jBefore) {
                moduleNeededList.add("jul-to-slf4j-1.7.25.jar");
            }
//            org.apache.logging.log4j:log4j-to-slf4j:jar
            moduleNeededList.add("log4j-to-slf4j-2.12.1.jar");
//            org.slf4j:slf4j-log4j12:jar
            moduleNeededList.add("slf4j-log4j12-1.7.25.jar");
//            log4j:log4j:jar
            moduleNeededList.add("log4j-1.2.17.jar");
        }
    }
    // set back jar
    private static void addBackModules(Collection<ModuleNeeded> moduleNeededList, boolean isSelectLog4j2,
            List<ModuleNeeded> modulesUsedBefore) {
        if (isSelectLog4j2) {
            boolean usedlog4jJclBefore = false;
            boolean usedlog4jJulBefore = false;
            for (ModuleNeeded module : modulesUsedBefore) {
                if (module.getModuleName().matches("log4j-jcl-\\d+\\.\\d+\\.\\d+\\.jar")) { //$NON-NLS-1$
                    usedlog4jJclBefore = true;
                }
                if (module.getModuleName().matches("log4j-jul-\\d+\\.\\d+\\.\\d+\\.jar")) { //$NON-NLS-1$
                    usedlog4jJulBefore = true;
                }
            }
//            org.apache.logging.log4j:log4j-jcl:jar (only if this jar was setup in the job before)
            if (usedlog4jJclBefore) {
                ModuleNeeded log4jJcl = new ModuleNeeded("org.apache.logging.log4j", "log4j-jcl-2.12.1.jar", null, true); //$NON-NLS-1$ //$NON-NLS-2$
                log4jJcl.setMavenUri("mvn:org.apache.logging.log4j/log4j-jcl/2.12.1");//$NON-NLS-1$
                moduleNeededList.add(log4jJcl);
            }
//            org.apache.logging.log4j:log4j-jul:jar (only if this jar was setup in the job before)
            if (usedlog4jJulBefore) {
                ModuleNeeded log4jJul = new ModuleNeeded("org.apache.logging.log4j", "log4j-jul-2.12.1.jar", null, true); //$NON-NLS-1$ //$NON-NLS-2$
                log4jJul.setMavenUri("mvn:org.apache.logging.log4j/log4j-jul/2.12.1");//$NON-NLS-1$
                moduleNeededList.add(log4jJul);
            }
//            org.apache.logging.log4j:log4j-slf4j-impl:jar
            ModuleNeeded log4jSlf4jImpl = new ModuleNeeded("org.apache.logging.log4j", "log4j-slf4j-impl-2.12.1.jar", null, true); //$NON-NLS-1$ //$NON-NLS-2$
            log4jSlf4jImpl.setMavenUri("mvn:org.apache.logging.log4j/log4j-slf4j-impl/2.12.1");//$NON-NLS-1$
            moduleNeededList.add(log4jSlf4jImpl);
//            org.apache.logging.log4j:log4j-api:jar
            ModuleNeeded log4jApi = new ModuleNeeded("org.apache.logging.log4j", "log4j-api-2.12.1.jar", null, true); //$NON-NLS-1$ //$NON-NLS-2$
            log4jApi.setMavenUri("mvn:org.apache.logging.log4j/log4j-api/2.12.1");//$NON-NLS-1$
            moduleNeededList.add(log4jApi);
//            org.apache.logging.log4j:log4j-core:jar
            ModuleNeeded log4jCore = new ModuleNeeded("org.apache.logging.log4j", "log4j-core-2.12.1.jar", null, true); //$NON-NLS-1$ //$NON-NLS-2$
            log4jCore.setMavenUri("mvn:org.apache.logging.log4j/log4j-core/2.12.1");//$NON-NLS-1$
            moduleNeededList.add(log4jCore);

            // log4j-1.2
            ModuleNeeded log4j12Api = new ModuleNeeded("org.apache.logging.log4j", "log4j-1.2-api-2.12.1.jar", null, true); //$NON-NLS-1$ //$NON-NLS-2$
            log4j12Api.setMavenUri("mvn:org.apache.logging.log4j/log4j-1.2-api/2.12.1");//$NON-NLS-1$
            moduleNeededList.add(log4j12Api);
        } else {
            boolean usedjclOverSlf4jBefore = false;
            boolean usedjulToSlf4jBefore = false;
            for (ModuleNeeded module : modulesUsedBefore) {
                if (module.getModuleName().matches("jcl-over-slf4j-\\d+\\.\\d+\\.\\d+\\.jar")) { //$NON-NLS-1$
                    usedjclOverSlf4jBefore = true;
                }
                if (module.getModuleName().matches("jul-to-slf4j-\\d+\\.\\d+\\.\\d+\\.jar")) { //$NON-NLS-1$
                    usedjulToSlf4jBefore = true;
                }
            }
//            org.slf4j:jcl-over-slf4j:jar (only if this jar was setup in the job before)
            if (usedjclOverSlf4jBefore) {
                ModuleNeeded jclOverSlf4j = new ModuleNeeded("org.slf4j", "jcl-over-slf4j-1.7.25.jar", null, true); //$NON-NLS-1$ //$NON-NLS-2$
                jclOverSlf4j.setMavenUri("mvn:org.slf4j/jcl-over-slf4j/1.7.25");//$NON-NLS-1$
                moduleNeededList.add(jclOverSlf4j);
            }
//            org.slf4j:jul-to-slf4j:jar (only if this jar was setup in the job before)
            if (usedjulToSlf4jBefore) {
                ModuleNeeded julToSlf4j = new ModuleNeeded("org.slf4j", "jul-to-slf4j-1.7.25.jar", null, true); //$NON-NLS-1$ //$NON-NLS-2$
                julToSlf4j.setMavenUri("mvn:org.slf4j/jul-to-slf4j/1.7.25");//$NON-NLS-1$
                moduleNeededList.add(julToSlf4j);
            }
//            org.apache.logging.log4j:log4j-to-slf4j:jar
            ModuleNeeded log4jToSlf4j = new ModuleNeeded("org.apache.logging.log4j", "log4j-to-slf4j-2.12.1.jar", null, true); //$NON-NLS-1$ //$NON-NLS-2$
            log4jToSlf4j.setMavenUri("mvn:org.apache.logging.log4j/log4j-to-slf4j/2.12.1");//$NON-NLS-1$
            moduleNeededList.add(log4jToSlf4j);
//            org.slf4j:slf4j-log4j12:jar
            ModuleNeeded slf4jLog4j12 = new ModuleNeeded("org.slf4j", "slf4j-log4j12-1.7.25.jar", null, true); //$NON-NLS-1$ //$NON-NLS-2$
            slf4jLog4j12.setMavenUri("mvn:org.slf4j/slf4j-log4j12/1.7.25");//$NON-NLS-1$
            moduleNeededList.add(slf4jLog4j12);
//            log4j:log4j:jar
            ModuleNeeded log4j = new ModuleNeeded("log4j", "log4j-1.2.17.jar", null, true); //$NON-NLS-1$ //$NON-NLS-2$
            log4j.setMavenUri("mvn:log4j/log4j/1.2.17");//$NON-NLS-1$
            moduleNeededList.add(log4j);
        }
    }

    private static List<ModuleNeeded> removeLog4jFromModuleListAndGetModulesUsedBefore(IProcess2 process,
            Collection<ModuleNeeded> jarList) {
        Set<ModuleNeeded> highPriorityModuleNeeded = getHighPriorityModuleNeeded(process);
        List<ModuleNeeded> modulesUsedBefore = new ArrayList<ModuleNeeded>();
        Iterator<ModuleNeeded> iterator = jarList.iterator();
        while (iterator.hasNext()) {
            ModuleNeeded module = iterator.next();
            getSpecialModulesUsedBefore(modulesUsedBefore, module);
            if (highPriorityModuleNeeded != null && !highPriorityModuleNeeded.contains(module)
                    && isNeedRemoveModule(module, module.getModuleName())) {
                iterator.remove();
            }
        }
        return modulesUsedBefore;
    }

    private static List<ModuleNeeded> getSpecialModulesUsedBefore(List<ModuleNeeded> modulesUsedBefore, ModuleNeeded module) {
        if (module.getModuleName().matches("log4j-jcl-\\d+\\.\\d+\\.\\d+\\.jar")) { //$NON-NLS-1$
            modulesUsedBefore.add(module);
        }
        if (module.getModuleName().matches("log4j-jul-\\d+\\.\\d+\\.\\d+\\.jar")) { //$NON-NLS-1$
            modulesUsedBefore.add(module);
        }
        if (module.getModuleName().matches("jcl-over-slf4j-\\d+\\.\\d+\\.\\d+\\.jar")) { //$NON-NLS-1$
            modulesUsedBefore.add(module);
        }
        if (module.getModuleName().matches("jul-to-slf4j-\\d+\\.\\d+\\.\\d+\\.jar")) { //$NON-NLS-1$
            modulesUsedBefore.add(module);
        }
        return modulesUsedBefore;
    }

    private static List<String> getSpecialJarsUsedBefore(List<String> jarsUsedBefore, String jar) {
        if (jar.matches("log4j-jcl-\\d+\\.\\d+\\.\\d+\\.jar")) { //$NON-NLS-1$
            jarsUsedBefore.add(jar);
        }
        if (jar.matches("log4j-jul-\\d+\\.\\d+\\.\\d+\\.jar")) { //$NON-NLS-1$
            jarsUsedBefore.add(jar);
        }
        if (jar.matches("jcl-over-slf4j-\\d+\\.\\d+\\.\\d+\\.jar")) { //$NON-NLS-1$
            jarsUsedBefore.add(jar);
        }
        if (jar.matches("jul-to-slf4j-\\d+\\.\\d+\\.\\d+\\.jar")) { //$NON-NLS-1$
            jarsUsedBefore.add(jar);
        }
        return jarsUsedBefore;

    }

    private static Set<ModuleNeeded> getHighPriorityModuleNeeded(IProcess2 process) {
        Set<ModuleNeeded> highPriorityModuleNeeded = null;
        if (process != null) {
            Property property = process.getProperty();
            highPriorityModuleNeeded = LastGenerationInfo.getInstance().getHighPriorityModuleNeeded(property.getId(),
                    property.getVersion());
        }
        return highPriorityModuleNeeded == null ? new LinkedHashSet<>() : highPriorityModuleNeeded;
    }

    private static List<String> removeLog4jFromJarListAndGetUsedJarBefore(IProcess2 process, Collection<String> jarList) {
        Set<ModuleNeeded> highPriorityModuleNeeded = getHighPriorityModuleNeeded(process);
        List<String> jarsUsedBefore = new ArrayList<String>();
        Iterator<String> iterator = jarList.iterator();
        while (iterator.hasNext()) {
            String jar = iterator.next();
            getSpecialJarsUsedBefore(jarsUsedBefore, jar);
            // TODO not be able to remove the jar whose context is "ch.qos.logback"
            if (!isHighPriorityModuleNeeded(highPriorityModuleNeeded, jar) && isNeedRemoveModule(null, jar)) {
                iterator.remove();
            }
        }
        return jarsUsedBefore;
    }

    private static  boolean isHighPriorityModuleNeeded(Set<ModuleNeeded> highPriorityModuleNeeded, String jar) {
        if (highPriorityModuleNeeded != null) {
            for (ModuleNeeded moduel : highPriorityModuleNeeded) {
                if (StringUtils.equals(moduel.getModuleName(), jar)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean isNeedRemoveModule(ModuleNeeded module, String moduleName) {
//        org.slf4j:jcl-over-slf4j:jar
        if (moduleName.matches("jcl-over-slf4j-\\d+\\.\\d+\\.\\d+\\.jar")) { //$NON-NLS-1$
            return true;
        }
//        org.slf4j:jul-to-slf4j:jar
        if (moduleName.matches("jul-to-slf4j-\\d+\\.\\d+\\.\\d+\\.jar")) { //$NON-NLS-1$
            return true;
        }
//        org.apache.logging.log4j:log4j-to-slf4j:jar
        if (moduleName.matches("log4j-to-slf4j-\\d+\\.\\d+\\.\\d+\\.jar")) { //$NON-NLS-1$
            return true;
        }
//        org.slf4j:slf4j-log4j12:jar
        if (moduleName.matches("slf4j-log4j12-\\d+\\.\\d+\\.\\d+\\.jar")) { //$NON-NLS-1$
            return true;
        }
//        log4j:log4j:jar
        if (moduleName.matches("log4j-\\d+\\.\\d+\\.\\d+\\.jar")) { //$NON-NLS-1$
            return true;
        }
//        org.apache.logging.log4j:log4j-jcl:jar
        if (moduleName.matches("log4j-jcl-\\d+\\.\\d+\\.\\d+\\.jar")) { //$NON-NLS-1$
            return true;
        }
//        org.apache.logging.log4j:log4j-jul:jar
        if (moduleName.matches("log4j-jul-\\d+\\.\\d+\\.\\d+\\.jar")) { //$NON-NLS-1$
            return true;
        }
//        org.apache.logging.log4j:log4j-slf4j-impl:jar
        if (moduleName.matches("log4j-slf4j-impl-\\d+\\.\\d+\\.\\d+\\.jar")) { //$NON-NLS-1$
            return true;
        }
//        org.apache.logging.log4j:log4j-1.2-api:jar
        if (moduleName.matches("log4j-1.2-api-\\d+\\.\\d+\\.\\d+\\.jar")) { //$NON-NLS-1$
            return true;
        }
//
//        org.apache.logging.log4j:log4j-core-2.12.1:jar
        if (moduleName.matches("log4j-core-\\d+\\.\\d+\\.\\d+\\.jar")) { //$NON-NLS-1$
            return true;
        }
// lost on jira       org.apache.logging.log4j:log4j-api-2.12.1:jar
        if (moduleName.matches("log4j-api-\\d+\\.\\d+\\.\\d+\\.jar")) { //$NON-NLS-1$
            return true;
        }

//        apache-logging:commons-logging:jar
        if (moduleName.matches("commons-logging-\\d+\\.\\d+\\.\\d+\\.jar")) { //$NON-NLS-1$
            return true;
        }
//        commons-logging:commons-logging:jar
        if (moduleName.matches("commons-logging-\\d+\\.\\d+\\.\\d+\\.jar")) { //$NON-NLS-1$
            return true;
        }
//        ch.qos.logback :logback-classic-1.3.0-alpha4.jar
        String[] mvnSplit = module.getMavenUri().split(MavenUrlHelper.SEPERATOR);
        if (mvnSplit != null && mvnSplit.length > 0) {
            if (StringUtils.equals(mvnSplit[0], MavenUrlHelper.MVN_PROTOCOL + "ch.qos.logback")) {
                return true;
            }
        }
//        org.talend.sdk.component:slf4j-standard:jar
        if (moduleName.matches("slf4j-standard-\\d+\\.\\d+\\.\\d+\\.jar")) { //$NON-NLS-1$
            return true;
        }
        return false;
    }

}

