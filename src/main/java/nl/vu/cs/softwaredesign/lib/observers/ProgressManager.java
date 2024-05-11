package nl.vu.cs.softwaredesign.lib.observers;

import nl.vu.cs.softwaredesign.lib.enumerations.Status;
import nl.vu.cs.softwaredesign.lib.interfaces.IProgressListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ProgressManager {
    private static ProgressManager instance = new ProgressManager();
    private final List<IProgressListener> listeners;

    private ProgressManager() {
        this.listeners = new ArrayList<>();
    }

    public static ProgressManager getInstance() {
        if (instance == null) {
            instance = new ProgressManager();
        }

        return instance;
    }

    /**
     * Adds a progress listener to be notified of status updates.
     *
     * @param listener The progress listener to add.
     */
    public void addListener(IProgressListener listener) {
        listeners.add(listener);
    }

    /**
     * Notifies all registered listeners of a status update.
     *
     * @param status    The status update.
     * @param archive   The file archive associated with the status update.
     */
    public void notifyListeners(Status status, File archive) {
        listeners.forEach(listener -> listener.update(status, archive));
    }
}
