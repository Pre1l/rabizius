package data;

/**
 * Used to save/manage submit results that is used by views to display changes or react to errors
 */
public class SubmitResult extends StringData {
    private boolean status;

    public void setStatus(boolean status) {
        this.status = status;
    }

    public boolean getStatus() {
        return status;
    }
}
