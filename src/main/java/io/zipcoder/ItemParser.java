package io.zipcoder;

import io.zipcoder.utils.Item;
import io.zipcoder.utils.ItemParseException;
import io.zipcoder.utils.match.Match;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ItemParser {
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
        return rawItems.stream().filter(Objects::nonNull).collect(Collectors.toList());
    }

    public Item parseSingleItem(String singleItem) throws ItemParseException {
        Pattern p1 = Pattern.compile(";");
        Pattern p2 = Pattern.compile("[:@^*%]");
        String[] rawpairs = p1.split(singleItem);
        List<String[]> pairs = Arrays.stream(rawpairs).filter(str -> p2.matcher(str).find())
                                                      .map(p2::split)
                                                      .collect(Collectors.toList());
        if (pairs.size() < 4) throw new ItemParseException();
        String name = pairs.get(0)[1].toLowerCase();
        Double price = Double.valueOf(pairs.get(1)[1]);
        String type = pairs.get(2)[1].toLowerCase();
        String expiration = pairs.get(3)[1];
        return new Item(name, price, type, expiration);

    }
}
