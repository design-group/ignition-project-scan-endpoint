package com.bwdesigngroup.ignition.project_scan.gateway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.inductiveautomation.ignition.common.gson.JsonObject;
import com.inductiveautomation.ignition.gateway.model.GatewayContext;
import com.bwdesigngroup.ignition.project_scan.common.ProjectScanRPC;
import com.bwdesigngroup.ignition.project_scan.common.ProjectScanConstants;

public class ProjectScanRPCHandler implements ProjectScanRPC {
    private final Logger logger = LoggerFactory.getLogger(ProjectScanConstants.MODULE_ID + ".RPCHandler");
    private final GatewayContext context;
    
    public ProjectScanRPCHandler(GatewayContext context) {
        this.context = context;
        logger.debug("RPC Handler created");
    }
    
    @Override
    public String scanProject(Boolean updateDesigners, Boolean forceUpdate) {
        logger.debug("Received RPC request to scan project - updateDesigners: " + updateDesigners + ", forceUpdate: " + forceUpdate);
        
        JsonObject result = new JsonObject();
        try {
            logger.info("Initiating project scan via RPC request");
            context.getProjectManager().requestScan();
            result.addProperty("success", true);
            logger.info("Project scan request successful");
        } catch (Exception e) {
            logger.error("Failed to execute project scan", e);
            result.addProperty("success", false);
            result.addProperty("error", e.getMessage());
        }
        return result.toString();
    }
}