package data;

import model.Item;

import java.util.ArrayList;
import java.util.List;

public interface CSV {

    List<List<String>> read();

    List<List<String>> read(String file);

    void write(ArrayList<Item> items);

    public void write(String file, List<Item> items);

    String getSelectedFile();

    void saveCurrentFile(List<Item> items);
}
