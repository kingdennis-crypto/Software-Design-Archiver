package nl.vu.cs.softwaredesign.lib.interfaces;

import nl.vu.cs.softwaredesign.lib.enumerations.Status;
import java.io.File;

/**
 * Interface for the progress listener
 * The progress listener is used to update the status of the progress process
 */
public interface IProgressListener {
    void update(Status status, File file);


}
