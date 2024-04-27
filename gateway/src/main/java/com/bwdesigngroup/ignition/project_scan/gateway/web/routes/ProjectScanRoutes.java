package com.bwdesigngroup.ignition.project_scan.gateway.web.routes;

import static com.inductiveautomation.ignition.gateway.dataroutes.HttpMethod.POST;
import static com.inductiveautomation.ignition.gateway.dataroutes.RouteGroup.TYPE_JSON;

import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.inductiveautomation.ignition.common.gson.JsonObject;
import com.inductiveautomation.ignition.gateway.dataroutes.RequestContext;
import com.inductiveautomation.ignition.gateway.dataroutes.RouteGroup;
import com.inductiveautomation.ignition.gateway.model.GatewayContext;
import com.inductiveautomation.ignition.gateway.project.ProjectManager;

public class ProjectScanRoutes {
	private static final Logger logger = LoggerFactory.getLogger(ProjectScanRoutes.class.getName());
	private final RouteGroup routes;
	private final GatewayContext gatewayContext;
	private final ProjectManager projectManager;

	public ProjectScanRoutes(GatewayContext context, RouteGroup group) {
		this.routes = group;
		this.gatewayContext = context;
		this.projectManager = context.getProjectManager();
	}

	public void mountRoutes() {
		/*
		 * Trigger a project scan
		 * This will be a POST request, with nothing in the body
		 * 
		 * Example Usage: curl -X POST -H "Content-Type: application/json"
		 * http://localhost:8088/data/project-scan/scan?
		 */
		this.routes.newRoute("/scan")
				.handler(this::triggerProjectScan)
				.type(TYPE_JSON)
				.method(POST)
				.mount();
	}

	public JsonObject triggerProjectScan(RequestContext requestContext,
			HttpServletResponse httpServletResponse) throws JSONException {
		logger.info("Triggering project scan");
		projectManager.requestScan();
		return new JsonObject();
	}
}