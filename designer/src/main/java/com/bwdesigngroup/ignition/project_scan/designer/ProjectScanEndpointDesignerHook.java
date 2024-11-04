package com.bwdesigngroup.ignition.project_scan.designer;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.inductiveautomation.ignition.client.gateway_interface.GatewayConnection;
import com.inductiveautomation.ignition.client.gateway_interface.GatewayConnectionManager;
import com.inductiveautomation.ignition.client.icons.VectorIcons;
import com.inductiveautomation.ignition.common.licensing.LicenseState;
import com.inductiveautomation.ignition.designer.model.AbstractDesignerModuleHook;
import com.inductiveautomation.ignition.designer.model.DesignerContext;
import com.jidesoft.action.CommandBar;
import com.inductiveautomation.ignition.designer.IgnitionDesigner;
import com.inductiveautomation.ignition.designer.gui.DesignerToolbar;
import com.inductiveautomation.ignition.common.BundleUtil;
import com.inductiveautomation.ignition.common.gateway.messages.PushNotification;
import com.bwdesigngroup.ignition.project_scan.common.ProjectScanConstants;
import com.bwdesigngroup.ignition.project_scan.designer.actions.ProjectScanAction;
import com.bwdesigngroup.ignition.project_scan.designer.browser.ProjectBrowserStateManager;

public class ProjectScanEndpointDesignerHook extends AbstractDesignerModuleHook {
    private final Logger logger = LoggerFactory.getLogger("project-scan.Designer");
    public static DesignerContext context;
    public static GatewayConnection gatewayConnection;
    public static DesignerPushNotificationListener pushNotificationListener;
    public static IgnitionDesigner designer;
    public static ProjectBrowserStateManager browserStateManager;


    @Override
    public void startup(DesignerContext context, LicenseState activationState) throws Exception {
        logger.info("Starting up Project Scan Designer Module");
        ProjectScanEndpointDesignerHook.context = context;
        ProjectScanEndpointDesignerHook.designer = (IgnitionDesigner) context.getFrame();
        ProjectScanEndpointDesignerHook.gatewayConnection = GatewayConnectionManager.getInstance();

        BundleUtil.get().addBundle("projectscan", this.getClass(), "designer");

        browserStateManager = new ProjectBrowserStateManager(context);
        
        ProjectScanEndpointDesignerHook.pushNotificationListener = new DesignerPushNotificationListener(designer) {
            @Override
            public void receive(PushNotification notification) {
                if (notification.getMessageType().equals(ProjectScanConstants.DESIGNER_SCAN_NOTIFICATION_ID)) {
                    browserStateManager.captureState();
                    super.receive(notification);
                    // Give the UI a moment to update
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        logger.error("Sleep interrupted", e);
                    }
                    browserStateManager.restoreState();
                }
            }
        };
        gatewayConnection.addPushNotificationListener(pushNotificationListener);
    }

    @Override
    public List<CommandBar> getModuleToolbars() {
        List<CommandBar> toolbars = new ArrayList<>();
        
        // Create a toolbar
        DesignerToolbar toolbar = new DesignerToolbar(
            "ProjectScan",
            "projectscan.Toolbar.Name" // Key for the toolbar name in the bundle, not sure why it assumes this is a key
        ); 
        
        ProjectScanAction scanAction = new ProjectScanAction(
            context,
            VectorIcons.getInteractive("refresh")
        );
        
        // Add the action as a button to the toolbar
        toolbar.addButton(scanAction);
        
        toolbars.add(toolbar);
        return toolbars;
    }

    @Override
    public void shutdown() {
        logger.info("Shutting down Project Scan Designer Module");
        gatewayConnection.removePushNotificationListener(pushNotificationListener);
    }
}