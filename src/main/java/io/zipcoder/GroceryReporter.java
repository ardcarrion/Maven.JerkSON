package io.zipcoder;

import io.zipcoder.utils.FileReader;
import io.zipcoder.utils.Item;

import javax.swing.text.html.Option;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class GroceryReporter {
    private final String originalFileText;

    public GroceryReporter(String jerksonFileName) {
        this.originalFileText = FileReader.readFile(jerksonFileName);
    }


    @Override
    public String toString() {
        String[] names = {"Milk", "Bread", "Cookies", "Apples"};
        HashMap<String, Double[]> prices = new HashMap<>();
        for (String name: names) {
            long count = addGroceryItem(name, prices);
            System.out.printf("name:%8s\tseen:%d\n", name, count);
            System.out.println("=============\t=============");
            Double max = prices.get(name)[0];
            Double min = prices.get(name)[1];
            System.out.printf("Price:%7.2f\n", max);
            System.out.println("-------------\t------------");
            if (!min.equals(max)) System.out.printf("Price:%7.2f\n", min);
        }
        return null;
    }

    public long addGroceryItem(String name, HashMap<String, Double[]> list) {
        ItemParser ip = new ItemParser();
        List<Item> items = ip.parseItemList(originalFileText);
        Item max = items.stream().filter(item -> item.getName().equals(name.toLowerCase()))
                            .max(Comparator.comparingDouble(Item::getPrice)).get();
        Item min = items.stream().filter(item -> item.getName().equals(name.toLowerCase()))
                .min(Comparator.comparingDouble(Item::getPrice)).get();
        Double[] counts = {max.getPrice(), min.getPrice()};
        list.put(name, counts);
        return items.stream().filter(item -> item.getName().equals(name.toLowerCase())).count();
    }
}
