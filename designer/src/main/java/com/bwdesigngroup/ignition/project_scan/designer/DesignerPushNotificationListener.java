package com.bwdesigngroup.ignition.project_scan.designer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import com.inductiveautomation.ignition.common.gateway.messages.PushNotification;
import com.inductiveautomation.ignition.common.gson.JsonObject;
import com.inductiveautomation.ignition.common.gson.Gson;
import com.inductiveautomation.ignition.client.gateway_interface.FilteredPushNotificationListener;
import com.inductiveautomation.ignition.designer.IgnitionDesigner;
import com.bwdesigngroup.ignition.project_scan.designer.dialog.ConfirmationDialog;
import com.bwdesigngroup.ignition.project_scan.common.ProjectScanConstants;

public class DesignerPushNotificationListener extends FilteredPushNotificationListener {
    private final Logger logger = LoggerFactory
            .getLogger(ProjectScanConstants.MODULE_ID + ".designerPushNotificationListener");
    private final IgnitionDesigner designer;
    private final Gson gson = new Gson();

    public DesignerPushNotificationListener(IgnitionDesigner designer) {
        super(ProjectScanConstants.MODULE_ID, new String[] { ProjectScanConstants.DESIGNER_SCAN_NOTIFICATION_ID });
        this.designer = designer;
    }

    @Override
    public void receive(PushNotification notification) {
        logger.info("Received push notification: " + notification.getMessageType());

        if (notification.getMessageType().equals(ProjectScanConstants.DESIGNER_SCAN_NOTIFICATION_ID)) {
            // Parse the notification message as a JSON string
            Object notificationMessage = notification.getMessage();
			JsonObject notificationData = gson.fromJson(notificationMessage.toString(), JsonObject.class);

            boolean forceUpdate = notificationData != null && notificationData.has("forceUpdate")
                    && notificationData.get("forceUpdate").getAsBoolean();
            if (forceUpdate) {
                this.designer.updateProject();
            } else {
                if (showDialog()) {
                    this.designer.updateProject();
                }
            }
        }
    }

    public Boolean showDialog() {
        ConfirmationDialog dialog = new ConfirmationDialog(IgnitionDesigner.getFrame());
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
        return dialog.isConfirmed();
    }
}