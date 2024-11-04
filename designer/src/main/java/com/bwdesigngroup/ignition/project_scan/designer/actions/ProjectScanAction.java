package com.bwdesigngroup.ignition.project_scan.designer.actions;

import com.inductiveautomation.ignition.client.util.action.BaseAction;
import com.inductiveautomation.ignition.designer.model.DesignerContext;
import com.inductiveautomation.ignition.common.gson.JsonObject;
import com.inductiveautomation.ignition.common.BundleUtil;
import com.inductiveautomation.ignition.common.gson.Gson;
import com.inductiveautomation.ignition.common.util.LoggerEx;
import com.inductiveautomation.ignition.client.gateway_interface.ModuleRPCFactory;
import com.bwdesigngroup.ignition.project_scan.common.ProjectScanConstants;
import com.bwdesigngroup.ignition.project_scan.common.ProjectScanRPC;
import com.bwdesigngroup.ignition.project_scan.designer.ProjectScanEndpointDesignerHook;


import javax.swing.Icon;
import javax.swing.SwingWorker;

public class ProjectScanAction extends BaseAction {
    private final LoggerEx logger = LoggerEx.newBuilder().build(ProjectScanConstants.MODULE_ID + ".ProjectScanAction");
    private final DesignerContext context;
    private final Gson gson = new Gson();

    public ProjectScanAction(DesignerContext context, Icon icon) {
        super(BundleUtil.get().getString("projectscan.Action.ScanProject.Name"), icon);
        this.context = context;
        putValue(SHORT_DESCRIPTION, BundleUtil.get().getString("projectscan.Action.ScanProject.Description"));
        logger.info("Project Scan Action initialized");
    }

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        logger.info("Project scan button clicked, initiating scan request");
        
        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                try {
                    logger.debug("Creating RPC interface");
                    ProjectScanRPC rpc = ModuleRPCFactory.create(
                        ProjectScanConstants.MODULE_ID,
                        ProjectScanRPC.class
                    );
                    
                    logger.debug("Making RPC call to gateway");
                    String result = rpc.scanProject(true, false);
                    logger.debug("Received RPC response: " + result);

                    JsonObject response = gson.fromJson(result, JsonObject.class);
                    if (response.has("success") && response.get("success").getAsBoolean()) {
                        logger.info("Gateway project scan completed successfully, triggering designer update");
                        
                        // Need to update on EDT since we're in a SwingWorker
                        javax.swing.SwingUtilities.invokeLater(() -> {
                            try {
                                // Capture browser state before update
                                if (ProjectScanEndpointDesignerHook.browserStateManager != null) {
                                    ProjectScanEndpointDesignerHook.browserStateManager.captureState();
                                }
                                
                                // Trigger the update
                                ProjectScanEndpointDesignerHook.designer.updateProject();
                                
                                // Give the UI a moment to update then restore state
                                Thread.sleep(100);
                                
                                if (ProjectScanEndpointDesignerHook.browserStateManager != null) {
                                    ProjectScanEndpointDesignerHook.browserStateManager.restoreState();
                                }
                            } catch (Exception ex) {
                                logger.error("Error during designer update", ex);
                            }
                        });
                    } else {
                        String error = response.has("error") ? response.get("error").getAsString() : "Unknown error";
                        logger.error("Gateway project scan failed: " + error);
                    }

                } catch (Exception ex) {
                    logger.error("Failed to trigger gateway project scan", ex);
                }
                return null;
            }

            @Override
            protected void done() {
                try {
                    get();
                } catch (Exception ex) {
                    logger.error("Error during project scan execution", ex);
                }
            }
        }.execute();
    }
}