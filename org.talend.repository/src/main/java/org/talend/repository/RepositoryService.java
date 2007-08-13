// ============================================================================
//
// Talend Community Edition
//
// Copyright (C) 2006-2007 Talend - www.talend.com
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
//
// ============================================================================
package org.talend.repository;

import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.talend.core.CorePlugin;
import org.talend.core.model.components.IComponentsFactory;
import org.talend.core.model.repository.IRepositoryObject;
import org.talend.repository.model.ComponentsFactoryProvider;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.repository.model.IRepositoryService;
import org.talend.repository.model.ProxyRepositoryFactory;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.model.RepositoryNodeUtilities;
import org.talend.repository.ui.views.RepositoryView;
import org.talend.repository.ui.wizards.metadata.connection.genericshema.GenericSchemaWizard;
import org.talend.repository.ui.utils.ColumnNameValidator;
import org.talend.repository.utils.RepositoryPathProvider;
import org.talend.core.model.properties.Property;;

/**
 * DOC qian class global comment. Detailled comment <br/>
 * 
 * $Id: talend-code-templates.xml 1 2006-09-29 17:06:40 +0000 (星期五, 29 九月 2006) nrousseau $
 * 
 */
public class RepositoryService implements IRepositoryService {

    private GenericSchemaWizard genericSchemaWizard = null;

    public IComponentsFactory getComponentsFactory() {
        return ComponentsFactoryProvider.getInstance();
    }

    public IPath getPathFileName(String folderName, String fileName) {
        return RepositoryPathProvider.getPathFileName(folderName, fileName);
    }

    public IProxyRepositoryFactory getProxyRepositoryFactory() {
        return ProxyRepositoryFactory.getInstance();
    }

    public IPath getRepositoryPath(RepositoryNode node) {
        return RepositoryNodeUtilities.getPath(node);
    }

    ChangeProcessor changeProcessor = new ChangeProcessor();

    public void registerRepositoryChangedListener(IRepositoryChangedListener listener) {
        changeProcessor.addRepositoryChangedListener(listener);
    }

    public void registerRepositoryChangedListenerAsFirst(IRepositoryChangedListener listener) {
        changeProcessor.registerRepositoryChangedListenerAsFirst(listener);
    }

    public void removeRepositoryChangedListener(IRepositoryChangedListener listener) {
        changeProcessor.removeRepositoryChangedListener(listener);
    }

    public void repositoryChanged(RepositoryElementDelta delta) {
        changeProcessor.repositoryChanged(delta);
    }

    // This method is used for the Action in RepositoryView to synchronize the sqlBuilder.
    // see DataBaseWizard, DatabaseTableWizard, AContextualAction
    public void notifySQLBuilder(List<IRepositoryObject> list) {
        IRepositoryChangedListener listener = (IRepositoryChangedListener) RepositoryView.show();
        removeRepositoryChangedListener(listener);
        for (Iterator<IRepositoryObject> iter = list.iterator(); iter.hasNext();) {
            IRepositoryObject element = iter.next();
            repositoryChanged(new RepositoryElementDelta(element));
        }
        registerRepositoryChangedListenerAsFirst(listener);
    }

    public String validateColumnName(String columnName, int index) {
        return ColumnNameValidator.validateColumnNameFormat(columnName, index);
    }

    public WizardDialog getGenericSchemaWizardDialog(Shell shell, IWorkbench workbench, boolean creation,
            ISelection selection, String[] existingNames, boolean isSinglePageOnly) {

        genericSchemaWizard = new GenericSchemaWizard(workbench, creation, selection, existingNames, isSinglePageOnly);
        return new WizardDialog(shell, genericSchemaWizard);
    }

    public Property getPropertyFromWizardDialog() {
        if (this.genericSchemaWizard != null) {
            return this.genericSchemaWizard.getConnectionProperty();
        }
        return null;
    }
    
    public IPath getPathForSaveAsGenericSchema() {
        if (this.genericSchemaWizard != null) 
        {return this.genericSchemaWizard.getPathForSaveAsGenericSchema();}
        return null;
    }
}
