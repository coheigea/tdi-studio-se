// ============================================================================
//
// Copyright (C) 2006-2007 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.core.debug;

import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.internal.ui.DebugUIPlugin;
import org.eclipse.debug.internal.ui.launchConfigurations.LaunchConfigurationManager;
import org.eclipse.debug.internal.ui.launchConfigurations.LaunchHistory;
import org.eclipse.debug.ui.DebugUITools;
import org.eclipse.debug.ui.IDebugUIConstants;
import org.eclipse.debug.ui.actions.AbstractLaunchToolbarAction;
import org.eclipse.debug.ui.actions.LaunchAction;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.designer.core.i18n.Messages;
import org.talend.repository.ProjectManager;
import org.talend.repository.editor.RepositoryEditorInput;
import org.talend.repository.model.RepositoryNode;

/**
 * DOC bqian class global comment. Detailled comment
 */
public class TalendLaunchToolbarAction extends AbstractLaunchToolbarAction {

    private IWorkbenchWindow window;

    private ISelection selection;

    private final static String RUN_LABEL = Messages.getString("MultiPageEditorContributor.runMenu");

    /**
     * DOC bqian TalendLaunchToolbarAction constructor comment.
     * 
     * @param launchGroupIdentifier
     */
    public TalendLaunchToolbarAction() {
        super(IDebugUIConstants.ID_RUN_LAUNCH_GROUP);
    }

    /**
     * Fills the drop-down menu with favorites and launch history
     * 
     * @param menu the menu to fill
     */
    protected void fillMenu(Menu menu) {
        ILaunchConfiguration[] historyList = getHistory();
        ILaunchConfiguration[] favoriteList = getFavorites();

        // Add favorites
        int accelerator = 1;
        accelerator = addToMenu(menu, favoriteList, accelerator);

        // Separator between favorites and history
        if (favoriteList.length > 0 && historyList.length > 0) {
            addSeparator(menu);
        }

        // Add history launches next
        addToMenu(menu, historyList, accelerator);
    }

    private int addToMenu(Menu menu, ILaunchConfiguration[] launchList, int accelerator) {
        for (int i = 0; i < launchList.length; i++) {
            ILaunchConfiguration launch = launchList[i];
            try {
                if (launch.getType().getIdentifier().equals(TalendDebugUIConstants.JOB_DEBUG_LAUNCH_CONFIGURATION_TYPE)
                        && isCurrentProject(launch)) {
                    LaunchAction action = new LaunchAction(launch, getMode());
                    addToMenu(menu, action, accelerator);
                    accelerator++;
                }
            } catch (Exception e) {
                ExceptionHandler.process(e);
                continue;
            }
        }
        return accelerator;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.debug.ui.actions.AbstractLaunchHistoryAction#init(org.eclipse.ui.IWorkbenchWindow)
     */
    @Override
    public void init(IWorkbenchWindow window) {
        super.init(window);
        this.window = window;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.debug.ui.actions.AbstractLaunchHistoryAction#selectionChanged(org.eclipse.jface.action.IAction,
     * org.eclipse.jface.viewers.ISelection)
     */
    @Override
    public void selectionChanged(IAction action, ISelection selection) {
        super.selectionChanged(action, selection);
        this.selection = selection;

        if (selection instanceof IStructuredSelection) {
            IStructuredSelection sel = (IStructuredSelection) selection;
            Object o = sel.getFirstElement();
            if ((o instanceof RepositoryNode)) {
                RepositoryNode node = (RepositoryNode) o;
                if (node.getObject() != null && node.getObject().getType().equals(ERepositoryObjectType.PROCESS)) {
                    action.setToolTipText(RUN_LABEL + " current job " + node.getObject().getProperty().getLabel());
                    return;
                }
            }
        }
        // launch the job that is open in editor
        if (window != null) {
            IWorkbenchPage page = window.getActivePage();
            if (page != null) {
                if (page.getActivePart() == page.getActiveEditor()) {
                    IEditorPart editor = page.getActiveEditor();
                    if (editor == null) {
                        return;
                    }
                    IEditorInput input = editor.getEditorInput();
                    if (input instanceof RepositoryEditorInput) {
                        RepositoryEditorInput rInput = (RepositoryEditorInput) input;
                        action.setToolTipText(RUN_LABEL + " current job " + rInput.getItem().getProperty().getLabel());
                        return;
                    }
                }
            }
        }

        ILaunchConfiguration configuration = getLastLaunch();
        if (!isCurrentProject(configuration)) {
            action.setToolTipText(RUN_LABEL);
        } else {
            try {
                action.setToolTipText(RUN_LABEL + " " + configuration.getAttribute(TalendDebugUIConstants.JOB_NAME, ""));
            } catch (Exception e) {
            }
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.debug.ui.actions.AbstractLaunchHistoryAction#getLastLaunch()
     */
    @Override
    protected ILaunchConfiguration getLastLaunch() {
        LaunchHistory history = getLaunchConfigurationManager().getLaunchHistory(getLaunchGroupIdentifier());
        if (history != null) {
            ILaunchConfiguration[] filterConfigs = history.getCompleteLaunchHistory();
            for (ILaunchConfiguration launchConfiguration : filterConfigs) {
                if (isCurrentProject(launchConfiguration)) {
                    return launchConfiguration;
                }
            }
        }
        return null;
    }

    /**
     * Launch the last launch, or open the launch config dialog if none.
     * 
     * @see org.eclipse.ui.IActionDelegate#run(org.eclipse.jface.action.IAction)
     */
    public void run(IAction action) {
        // launch the job that is selected in the repository.
        if (selection instanceof IStructuredSelection) {
            IStructuredSelection sel = (IStructuredSelection) selection;
            Object o = sel.getFirstElement();
            if ((o instanceof RepositoryNode)) {
                RepositoryNode node = (RepositoryNode) o;
                if (node.getObject() != null && node.getObject().getType().equals(ERepositoryObjectType.PROCESS)) {
                    JobLaunchShortcutManager.run(selection);
                    return;
                }
            }
        }
        // launch the job that is open in editor
        if (window != null) {
            IWorkbenchPage page = window.getActivePage();
            if (page != null) {
                if (page.getActivePart() == page.getActiveEditor()) {
                    IEditorPart editor = page.getActiveEditor();
                    IEditorInput input = editor.getEditorInput();
                    if (input instanceof RepositoryEditorInput) {
                        JobLaunchShortcutManager.run(editor);
                        return;
                    }
                }
            }
        }

        ILaunchConfiguration configuration = getLastLaunch();

        if (configuration == null) {
            MessageDialog.openInformation(DebugUIPlugin.getShell(), "Infomation", "There is no running item available.");
            // DebugUITools.openLaunchConfigurationDialogOnGroup(DebugUIPlugin.getShell(), new StructuredSelection(),
            // getLaunchGroupIdentifier());
        } else {
            DebugUITools.launch(configuration, getMode());
        }
    }

    private boolean isCurrentProject(ILaunchConfiguration configuration) {
        try {
            String projectName = configuration.getAttribute(TalendDebugUIConstants.CURRENT_PROJECT_NAME, (String) null);
            return projectName.equals(ProjectManager.getInstance().getCurrentProject().getLabel());
        } catch (Exception e) {
            // TODO: handle exception
        }
        return false;
    }

    // }
    /**
     * Returns the launch configuration manager.
     * 
     * @return launch configuration manager
     */
    private LaunchConfigurationManager getLaunchConfigurationManager() {
        return DebugUIPlugin.getDefault().getLaunchConfigurationManager();
    }
}
