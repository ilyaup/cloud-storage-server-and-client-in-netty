package cloud;

import java.util.HashMap;
import java.util.Map;

public enum PipelineState {
    LOGIN, UPLOAD, DOWNLOAD, DOWNLOAD1, UPLOAD1, MOVE, COPY;

    public String toCmdName() {
        return this.name().toLowerCase();
    }

    private static final Map<String, PipelineState> stateByCmd = new HashMap<>();

    static { // builds automatically when new pipeline state enum is added
        for (PipelineState u : PipelineState.values()) {
            String stringState = u.toString().toLowerCase(); // otherwise idea says put may produce NullPointerException
            stateByCmd.put(stringState, u);
        }
    }

    public static PipelineState stateFromCmdName(String cmdName) {
        return stateByCmd.get(cmdName);
    }
}
