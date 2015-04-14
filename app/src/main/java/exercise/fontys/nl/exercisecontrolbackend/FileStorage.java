package exercise.fontys.nl.exercisecontrolbackend;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Internal application object file storage handler.
 */
public class FileStorage {

    private Context context;

    public FileStorage() {
        // load application context into variable
        context = ExerciseApplication.getContext();
    }

    /**
     * Loads an object from an internal Android file.
     * @param identifier Object identifier
     * @param <T> type of object
     * @return loaded object
     * @throws IOException when loading or deserializing failed.
     */
    public <T> T load(String identifier) throws IOException {
        FileInputStream fis = null;
        ObjectInputStream is = null;

        try {
            fis = context.openFileInput(identifier);
            is = new ObjectInputStream(fis);
            T obj = (T)is.readObject();
            return obj;
        } catch (IOException | ClassNotFoundException | ClassCastException ex) {
            throw new IOException("Object deserialization failed");
        } finally {
            try {
                if (is != null)
                    is.close();
                if (fis != null)
                    fis.close();
            } catch (IOException ex) { }
        }
    }

    /**
     * Saves an object to an internal Android file.
     * @param identifier Object identifier
     * @param obj Object to save
     * @param <T>type of object
     * @throws IOException when serializing or saving failed.
     */
    public <T> void save(String identifier, T obj) throws IOException {
        FileOutputStream fos = null;
        ObjectOutputStream os = null;

        try {
            fos = context.openFileOutput(identifier, Context.MODE_PRIVATE);
            os = new ObjectOutputStream(fos);
            os.writeObject(obj);
        } catch (IOException ex) {
            throw new IOException("Object serialization failed.");
        } finally {
            try {
                if (os != null)
                    os.close();
                if (fos != null)
                    fos.close();
            } catch (IOException ex) { }
        }
    }
}
