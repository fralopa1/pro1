package data;

import model.Item;

import java.util.ArrayList;
import java.util.List;

public class Mapper {
    public static List<Item> toItemModel(List<List<String>> data) {
        ArrayList<Item> list = new ArrayList();
        if (data.get(0).size() != 3) {
            throw new IllegalArgumentException("Error");
        } else {
            data.forEach(
                    objData -> {
                        Item item = new Item(objData.get(0), Float.parseFloat(objData.get(1)), Integer.parseInt(objData.get(2)));
                        list.add(item);
                    }
            );
        }
        return list;
    }
}
