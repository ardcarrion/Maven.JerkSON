package io.zipcoder;

import io.zipcoder.utils.Item;
import io.zipcoder.utils.ItemParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ItemParser {
    public Integer getErrorCount() {
        return errorCount;
    }

    private Integer errorCount;

    public ItemParser() {
        errorCount = 0;
    }

    public List<Item> parseItemList(String valueToParse) {
        List<Item> rawItems = new ArrayList<>();
        Pattern p = Pattern.compile("##");
        Arrays.stream(p.split(valueToParse)).forEach(str -> {
            try {
                rawItems.add(parseSingleItem(str));
            } catch (ItemParseException ex) {
                rawItems.add(null);
            }
        });
        List<Item> items = rawItems.stream().filter(Objects::nonNull).collect(Collectors.toList());
        this.errorCount = rawItems.size() - items.size();
        return items;
    }

    public Item parseSingleItem(String singleItem) throws ItemParseException {
        Pattern p1 = Pattern.compile(";");
        Pattern p2 = Pattern.compile("[:@^*%]");
        String[] rawpairs = p1.split(singleItem);
        List<String[]> pairs = Arrays.stream(rawpairs).filter(str -> p2.matcher(str).find())
                                                      .map(p2::split)
                                                      .collect(Collectors.toList());
        try {
            String name = pairs.get(0)[1].toLowerCase();
            Double price = Double.valueOf(pairs.get(1)[1]);
            String type = pairs.get(2)[1].toLowerCase();
            String expirationTemp = pairs.get(3)[1];
            String expiration = (Pattern.compile("##").matcher(expirationTemp).find()) ? expirationTemp.substring(0, expirationTemp.length() - 2) : expirationTemp;
            return new Item(name, price, type, expiration);
        } catch (Exception ex) {
            throw new ItemParseException();
        }

    }
}
