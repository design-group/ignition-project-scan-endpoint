package com.bwdesigngroup.ignition.project_scan.designer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.inductiveautomation.ignition.client.gateway_interface.GatewayConnection;
import com.inductiveautomation.ignition.client.gateway_interface.GatewayConnectionManager;
import com.inductiveautomation.ignition.common.licensing.LicenseState;
import com.inductiveautomation.ignition.designer.model.AbstractDesignerModuleHook;
import com.inductiveautomation.ignition.designer.model.DesignerContext;
import com.inductiveautomation.ignition.designer.IgnitionDesigner;

public class ProjectScanEndpointDesignerHook extends AbstractDesignerModuleHook {
	private final Logger logger = LoggerFactory.getLogger("project-scan.Designer");
	public static DesignerContext context;
	public static GatewayConnection gatewayConnection;
	public static DesignerPushNotificationListener pushNotificationListener;
	public static IgnitionDesigner designer;

    @Override
    public void startup(DesignerContext context, LicenseState activationState) throws Exception {
		logger.info("Starting up Project Scan Designer Module");
		ProjectScanEndpointDesignerHook.context = context;
		ProjectScanEndpointDesignerHook.designer = (IgnitionDesigner) context.getFrame();
		ProjectScanEndpointDesignerHook.gatewayConnection = GatewayConnectionManager.getInstance();
		ProjectScanEndpointDesignerHook.pushNotificationListener = new DesignerPushNotificationListener(designer);
		gatewayConnection.addPushNotificationListener(pushNotificationListener);
    }

	@Override
	public void shutdown() {
		logger.info("Shutting down Project Scan Designer Module");
		gatewayConnection.removePushNotificationListener(pushNotificationListener);
	}
}
