package data;

import model.Item;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileManager implements CSV {

    public static final String DEFAULT_NAME = "data.csv";

    private String selectedFile;

    @Override
    public String getSelectedFile() {
        return selectedFile;
    }

    @Override
    public void saveCurrentFile(List<Item> items) {
        write(selectedFile, items);
    }

    @Override
    public List<List<String>> read() {
        String line = "";
        String cvsSplitBy = ",";

        ArrayList<List<String>> items = new ArrayList<>();
        selectedFile = DEFAULT_NAME;

        try (BufferedReader br = new BufferedReader(new FileReader(DEFAULT_NAME))) {
            line = br.readLine();
            String[] headers = line.split(cvsSplitBy);

            while ((line = br.readLine()) != null) {
                String[] data = line.split(cvsSplitBy);
                List<String> obj = Arrays.stream(data).toList();
                items.add(obj);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return items;
    }

    @Override
    public List<List<String>> read(String file) {
        String line = "";
        String cvsSplitBy = ",";
        selectedFile = file;

        ArrayList<List<String>> items = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            line = br.readLine();
            String[] headers = line.split(cvsSplitBy);

            while ((line = br.readLine()) != null) {
                String[] data = line.split(cvsSplitBy);
                List<String> obj = Arrays.stream(data).toList();
                items.add(obj);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return items;
    }

    @Override
    public void write(ArrayList<Item> items) {
        String[] headers = {"Name", "Price", "Amount"};

        try {
            FileWriter writer = new FileWriter(DEFAULT_NAME);

            // write headers
            for (int i = 0; i < headers.length; i++) {
                writer.append(headers[i]);
                if (i != headers.length - 1) {
                    writer.append(",");
                }
            }
            writer.append("\n");

            // write data
            for (int i = 0; i < items.size(); i++) {
                writer.append(items.get(i).getName() + "," + items.get(i).getPrice() + "," + items.get(i).getCount());
                writer.append("\n");
            }

            writer.flush();
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void write(String file, List<Item> items) {
        String[] headers = {"Name", "Price", "Amount"};

        try {
            FileWriter writer = new FileWriter(file);

            // write headers
            for (int i = 0; i < headers.length; i++) {
                writer.append(headers[i]);
                if (i != headers.length - 1) {
                    writer.append(",");
                }
            }
            writer.append("\n");

            for (int i = 0; i < items.size(); i++) {
                Item item = items.get(i);
                writer.append(item.getName() + "," + item.getPrice() + "," + item.getCount());
                writer.append("\n");
            }

            writer.flush();
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
