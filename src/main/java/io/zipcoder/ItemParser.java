package io.zipcoder;

import io.zipcoder.utils.Item;
import io.zipcoder.utils.ItemParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ItemParser {
    private Integer errorCount;

    public ItemParser() {
        errorCount = 0;
    }

    public List<Item> parseItemList(String valueToParse) {
        List<Item> items = new ArrayList<>();
        Pattern p = Pattern.compile("##");
        Arrays.stream(p.split(valueToParse)).forEach(str -> {
            try {
                items.add(parseSingleItem(str));
            } catch (ItemParseException ex) {
                errorCount++;
            }
        });
        return items;
    }

    public Item parseSingleItem(String singleItem) throws ItemParseException {
        Pattern p1 = Pattern.compile(";");
        Pattern p2 = Pattern.compile("[:@^*%]");
        Pattern p3 = Pattern.compile("##");
        String[] rawPairs = p1.split(singleItem);
        List<String[]> pairs = Arrays.stream(rawPairs).map(p2::split)
                                                      .collect(Collectors.toList());
        try {
            String name = pairs.get(0)[1].toLowerCase();
            Double price = Double.valueOf(pairs.get(1)[1]);
            String type = pairs.get(2)[1].toLowerCase();
            String temp = pairs.get(3)[1];
            String expiration = (p3.matcher(temp).find()) ? temp.substring(0, temp.length()-2) : temp;
            return new Item(name, price, type, expiration);
        } catch (Exception ex) {
            throw new ItemParseException();
        }
    }

    public Integer getErrorCount() {
        return errorCount;
    }
}
