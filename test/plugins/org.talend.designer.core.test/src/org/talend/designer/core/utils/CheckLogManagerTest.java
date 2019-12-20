package org.talend.designer.core.utils;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.talend.commons.CommonsPlugin;
import org.talend.core.CorePlugin;
import org.talend.core.model.components.ComponentCategory;
import org.talend.core.model.components.IComponent;
import org.talend.core.model.general.ModuleNeeded;
import org.talend.core.model.process.BigDataNode;
import org.talend.core.model.process.EComponentCategory;
import org.talend.core.model.process.EParameterFieldType;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.process.INode;
import org.talend.core.model.process.IProcess;
import org.talend.core.ui.component.ComponentsFactoryProvider;
import org.talend.designer.core.model.components.DummyComponent;
import org.talend.designer.core.model.components.ElementParameter;
import org.talend.designer.core.ui.editor.process.Process;
import org.talend.designer.core.ui.editor.properties.controllers.AbstractGuessSchemaProcess;
import org.talend.repository.ui.utils.Log4jPrefsSettingManager;

public class CheckLogManagerTest {

    private static final String SPARK_CONFIGURATION = "tSparkConfiguration";
    @Test
    public void testAddLog4jToModuleList() {
        boolean headless = CommonsPlugin.isHeadless();
        try {
            CommonsPlugin.setHeadless(true);
            BigDataNode node = new BigDataNode(new DummyComponent("dummyComponent"), "dummyComponent_1");
            node.setSubProcessStart(true);
            node.setActivate(true);
            IProcess processbig = new Process(AbstractGuessSchemaProcess.getNewmockProperty());
            node.setProcess(processbig);
            List<INode> nodes = (List<INode>) processbig.getGraphicalNodes();
            nodes.add(node);

            IComponent sparkConfigurationComponent = ComponentsFactoryProvider.getInstance().get(SPARK_CONFIGURATION,
                    ComponentCategory.CATEGORY_4_SPARK.getName());
            BigDataNode sparkConfigurationNode = new BigDataNode(sparkConfigurationComponent, SPARK_CONFIGURATION + "_1");
            sparkConfigurationNode.setSubProcessStart(true);
            sparkConfigurationNode.setActivate(true);
            nodes.add(sparkConfigurationNode);

            ElementParameter storageConfigurationParam = createElementParameter(node, "STORAGE_CONFIGURATION",
                    sparkConfigurationNode.getUniqueName());
            List<IElementParameter> nodeParams = (List<IElementParameter>) node.getElementParameters();
            nodeParams.add(storageConfigurationParam);

            Set<ModuleNeeded> neededLibraries1 = CorePlugin.getDefault().getDesignerCoreService()
                    .getNeededLibrariesForProcess(processbig, false);
            boolean selectLog4j2 = Log4jPrefsSettingManager.getInstance().isSelectLog4j2();

            List<String> modules4log4j = new ArrayList<>();
            for (ModuleNeeded moule : neededLibraries1) {
                modules4log4j.add(moule.getModuleName());
            }
            if (!selectLog4j2) {
                // case1: if original job need commons-logging:commons-logging:ja for log4j1
                assertTrue(modules4log4j.contains("jcl-over-slf4j-1.7.25.jar"));
            } else {
                // case2: if original job need commons-logging:commons-logging:ja for log4j2
                assertTrue(modules4log4j.contains("log4j-jcl-2.12.1.jar"));
                // case3:if original job need log4j1 jar, add back log4j-1.2-api for log4j2
                assertTrue(modules4log4j.contains("log4j-1.2-api-2.12.1.jar"));
            }
        } finally {
            CommonsPlugin.setHeadless(headless);
        }
    }

    private ElementParameter createElementParameter(INode node, String name, Object value) {
        ElementParameter elemParam = new ElementParameter(node);
        elemParam.setName(name);
        elemParam.setValue(value);
        elemParam.setDisplayName(name);
        elemParam.setFieldType(EParameterFieldType.TEXT);
        elemParam.setCategory(EComponentCategory.MAIN);
        elemParam.setNumRow(1);
        elemParam.setReadOnly(false);
        elemParam.setShow(true);
        return elemParam;
    }
}
