package com.bwdesigngroup.ignition.project_scan.gateway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bwdesigngroup.ignition.project_scan.gateway.web.routes.ProjectScanRoutes;
import com.bwdesigngroup.ignition.project_scan.common.ProjectScanConstants;
import com.inductiveautomation.ignition.common.licensing.LicenseState;
import com.inductiveautomation.ignition.gateway.clientcomm.ClientReqSession;
import com.inductiveautomation.ignition.gateway.dataroutes.RouteGroup;
import com.inductiveautomation.ignition.gateway.model.AbstractGatewayModuleHook;
import com.inductiveautomation.ignition.gateway.model.GatewayContext;


/**
 * Class which is instantiated by the Ignition platform when the module is loaded in the gateway scope.
 */
public class ProjectScanEndpointGatewayHook extends AbstractGatewayModuleHook {

	private final Logger logger = LoggerFactory.getLogger(ProjectScanConstants.MODULE_ID + ".gateway");
	public static GatewayContext context;

    /**
     * Called to before startup. This is the chance for the module to add its extension points and update persistent
     * records and schemas. None of the managers will be started up at this point, but the extension point managers will
     * accept extension point types.
     */
    @Override
    public void setup(GatewayContext context) {
		logger.info("Setting up Project Scan Gateway Module");
		ProjectScanEndpointGatewayHook.context = context;
    }

    /**
     * Called to initialize the module. Will only be called once. Persistence interface is available, but only in
     * read-only mode.
     */
    @Override
    public void startup(LicenseState activationState) {
		logger.info("Starting up Project Scan Gateway Module");
    }

    /**
     * Called to shutdown this module. Note that this instance will never be started back up - a new one will be created
     * if a restart is desired
     */
    @Override
    public void shutdown() {
		logger.info("Shutting down Project Scan Gateway Module");
    }
    
    /**
     * Provides a chance for the module to mount any route handlers it wants. These will be active at
     * <tt>/main/data/module-id/*</tt> See {@link RouteGroup} for details. Will be called after startup().
     */
    @Override
	public void mountRouteHandlers(RouteGroup routes) {
		new ProjectScanRoutes(context, routes).mountRoutes();
	}

    /**
     * @return {@code true} if this is a "free" module, i.e. it does not participate in the licensing system. This is
     * equivalent to the now defunct FreeModule attribute that could be specified in module.xml.
     */
    @Override
    public boolean isFreeModule() {
        return true;
    }

    @Override
    public Object getRPCHandler(ClientReqSession session, String projectName) {
        logger.debug("Creating RPC Handler for session: " + session.getId() + ", project: " + projectName);
        return new ProjectScanRPCHandler(context);
    }
}