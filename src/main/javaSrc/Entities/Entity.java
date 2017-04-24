package main.javaSrc.Entities;

import main.javaSrc.helpers.EVException;
import org.w3c.dom.events.EventException;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface Entity {
    int getId();

    void setId(int id);

    void setPersistent(boolean persistent);

    boolean isPersistent();

    String getRestoreString() throws EVException;

    PreparedStatement insertStoreData(PreparedStatement stmt) throws EVException, SQLException;

    String getType();
}


