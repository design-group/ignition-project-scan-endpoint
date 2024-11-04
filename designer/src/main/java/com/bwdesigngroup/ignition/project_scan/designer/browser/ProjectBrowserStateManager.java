package com.bwdesigngroup.ignition.project_scan.designer.browser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.inductiveautomation.ignition.designer.model.DesignerContext;
import com.inductiveautomation.ignition.designer.navtree.model.AbstractNavTreeNode;
import com.inductiveautomation.ignition.designer.navtree.model.ProjectBrowserRoot;
import com.bwdesigngroup.ignition.project_scan.common.ProjectScanConstants;

import java.util.*;

public class ProjectBrowserStateManager {
    private final Logger logger = LoggerFactory.getLogger(ProjectScanConstants.MODULE_ID + ".browserState");
    private final DesignerContext context;
    private Map<String, TreeNodeState> lastTreeState;

    public ProjectBrowserStateManager(DesignerContext context) {
        this.context = context;
    }

    public void captureState() {
        ProjectBrowserRoot root = context.getProjectBrowserRoot();
        if (root != null) {
            lastTreeState = new HashMap<>();
            for (int i = 0; i < root.getChildCount(); i++) {
                captureNodeState(root.getChildAt(i), lastTreeState);
            }
            logger.debug("Captured project browser state");
        }
    }

    public void restoreState() {
        if (lastTreeState != null) {
            ProjectBrowserRoot root = context.getProjectBrowserRoot();
            if (root != null) {
                for (int i = 0; i < root.getChildCount(); i++) {
                    restoreNodeState(root.getChildAt(i));
                }
                logger.debug("Restored project browser state");
            }
        }
    }

    private void captureNodeState(AbstractNavTreeNode node, Map<String, TreeNodeState> stateMap) {
        String pathString = getNodePath(node);
        boolean expanded = node.getModel().isExpanded(node);
        boolean selected = node.isSelected();
        
        TreeNodeState state = new TreeNodeState(
            pathString,
            node.getName(),
            expanded,
            selected
        );
        stateMap.put(pathString, state);

        if (!node.isLeaf()) {
            for (int i = 0; i < node.getChildCount(); i++) {
                captureNodeState(node.getChildAt(i), stateMap);
            }
        }
    }

    private String getNodePath(AbstractNavTreeNode node) {
        List<String> pathParts = new ArrayList<>();
        AbstractNavTreeNode current = node;
        while (current != null && current.getParent() != null) {
            pathParts.add(0, current.getName());
            current = current.getParent();
        }
        return String.join("/", pathParts);
    }

    private void restoreNodeState(AbstractNavTreeNode node) {
        String pathString = getNodePath(node);
        TreeNodeState savedState = lastTreeState.get(pathString);
        
        if (savedState != null) {
            if (savedState.expanded) {
                node.getModel().expandNode(node);
            }
            if (savedState.selected) {
                node.select(true);
            }
        }

        if (!node.isLeaf()) {
            for (int i = 0; i < node.getChildCount(); i++) {
                restoreNodeState(node.getChildAt(i));
            }
        }
    }

    private static class TreeNodeState {
        final String path;
        final String name; 
        final boolean expanded;
        final boolean selected;

        TreeNodeState(String path, String name, boolean expanded, boolean selected) {
            this.path = path;
            this.name = name;
            this.expanded = expanded;
            this.selected = selected;
        }
    }
}