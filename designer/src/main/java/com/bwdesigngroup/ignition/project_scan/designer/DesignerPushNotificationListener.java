package com.bwdesigngroup.ignition.project_scan.designer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.inductiveautomation.ignition.common.gateway.messages.PushNotification;
import com.inductiveautomation.ignition.client.gateway_interface.FilteredPushNotificationListener;
import com.inductiveautomation.ignition.designer.IgnitionDesigner;

import com.bwdesigngroup.ignition.project_scan.common.ProjectScanConstants;

public class DesignerPushNotificationListener extends FilteredPushNotificationListener {
	private final Logger logger = LoggerFactory.getLogger(ProjectScanConstants.MODULE_ID + ".designerPushNotificationListener");
	private final IgnitionDesigner designer;

	public DesignerPushNotificationListener(IgnitionDesigner designer) {
		super(ProjectScanConstants.MODULE_ID, new String[] { ProjectScanConstants.DESIGNER_SCAN_NOTIFICATION_ID }); 
		this.designer = designer;
	}

	@Override
	public void receive(PushNotification notification) {
		logger.info("Received push notification: " + notification.getMessageType());
		this.designer.updateProject();
	}
}
