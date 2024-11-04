package com.bwdesigngroup.ignition.project_scan.common;

/**
 * RPC interface for project scanning functionality
 */
public interface ProjectScanRPC {
    /**
     * Triggers a project scan on the gateway
     * @param updateDesigners Whether to notify designers to update
     * @param forceUpdate Whether to force the update in designers
     * @return JSON string containing result of scan
     */
    String scanProject(Boolean updateDesigners, Boolean forceUpdate);
}