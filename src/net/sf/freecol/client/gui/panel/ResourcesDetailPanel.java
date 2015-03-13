/**
 *  Copyright (C) 2002-2015   The FreeCol Team
 *
 *  This file is part of FreeCol.
 *
 *  FreeCol is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  FreeCol is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with FreeCol.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.sf.freecol.client.gui.panel;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.tree.DefaultMutableTreeNode;
import net.miginfocom.swing.MigLayout;

import net.sf.freecol.client.FreeColClient;
import net.sf.freecol.client.gui.FontLibrary;
import net.sf.freecol.client.gui.GUI;
import net.sf.freecol.client.gui.action.ColopediaAction.PanelType;
import net.sf.freecol.common.i18n.Messages;
import net.sf.freecol.common.model.FreeColGameObjectType;
import net.sf.freecol.common.model.GoodsType;
import net.sf.freecol.common.model.Modifier;
import net.sf.freecol.common.model.ResourceType;
import net.sf.freecol.common.model.Scope;
import static net.sf.freecol.common.util.StringUtils.*;


/**
 * This panel displays details of resources in the Colopedia.
 */
public class ResourcesDetailPanel extends ColopediaGameObjectTypePanel<ResourceType> {


    /**
     * Creates a new instance of this ColopediaDetailPanel.
     *
     * @param freeColClient The <code>FreeColClient</code> for the game.
     * @param colopediaPanel The parent ColopediaPanel.
     */
    public ResourcesDetailPanel(FreeColClient freeColClient,
                                ColopediaPanel colopediaPanel) {
        super(freeColClient, colopediaPanel,
              PanelType.RESOURCES.toString(), 0.75);
    }


    /**
     * Adds one or several subtrees for all the objects for which this
     * ColopediaDetailPanel could build a detail panel to the given
     * root node.
     *
     * @param root a <code>DefaultMutableTreeNode</code>
     */
    public void addSubTrees(DefaultMutableTreeNode root) {
        super.addSubTrees(root, getSpecification().getResourceTypeList());
    }

    /**
     * Builds the details panel for the ResourceType with the given
     * identifier.
     *
     * @param id The object identifier.
     * @param panel the detail panel to build
     */
    public void buildDetail(String id, JPanel panel) {
        if (getId().equals(id)) {
            return;
        }

        ResourceType type = getSpecification().getResourceType(id);
        panel.setLayout(new MigLayout("wrap 2", "[]20[]"));

        JLabel name = GUI.localizedLabel(type.getNameKey());
        name.setFont(FontLibrary.SMALL_HEADER_FONT);
        panel.add(name, "span, align center, wrap 40");

        panel.add(GUI.localizedLabel("colopedia.resource.bonusProduction"));
        JPanel goodsPanel = new JPanel();
        goodsPanel.setOpaque(false);
        for (Modifier modifier : type.getModifiers()) {
            String text = ModifierFormat.getModifierAsString(modifier);
            if (modifier.hasScope()) {
                List<String> scopeStrings = new ArrayList<>();
                for (Scope scope : modifier.getScopes()) {
                    if (scope.getType() != null) {
                        FreeColGameObjectType fcgot = getSpecification()
                            .findType(scope.getType());
                        scopeStrings.add(Messages.getName(fcgot));
                    }
                }
                if (!scopeStrings.isEmpty()) {
                    text += " (" + join(", ", scopeStrings) + ")";
                }
            }

            GoodsType goodsType = getSpecification().getGoodsType(modifier.getId());
            JButton goodsButton = getGoodsButton(goodsType, text);
            goodsPanel.add(goodsButton);
        }
        panel.add(goodsPanel);

        panel.add(GUI.localizedLabel("colopedia.resource.description"),
                  "newline 20");
        panel.add(GUI.getDefaultTextArea(Messages.getDescription(type), 30),
                  "growx");
    }

}
