package com.easemob.weichat.integration.data;

import java.util.ArrayList;

import lombok.Data;

/**
 * @author shengyp
 * @since 09/08/16
 */

public class NewUserTaskData {
    private NewUserTaskData(){
        // not to be called
    }

    @Data
    public static class NewUserTasksResponseData {
        private boolean newUserflag;
        private ArrayList<String> tasklist;
    }

    @Data
    public static class AgentUserTaskDone {
        private String taskDone;
    }
}