package nl.vu.cs.softwaredesign.lib.observers;

import nl.vu.cs.softwaredesign.lib.enumerations.Status;
import nl.vu.cs.softwaredesign.lib.interfaces.IProgressListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ProgressManager {
    private static ProgressManager instance;
    private List<IProgressListener> listeners;

    private ProgressManager() {
        this.listeners = new ArrayList<>();
    }

    public static ProgressManager getInstance() {
        if (instance == null) {
            instance = new ProgressManager();
        }

        return instance;
    }

    public void addListener(IProgressListener listener) {
        listeners.add(listener);
    }

    public void notifyListeners(Status status, File archive) {
        listeners.forEach(listener -> listener.update(status, archive));
    }
}
