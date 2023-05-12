package com.maorzehavi.purchaseservice.util;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

public class CouponPurchaseResponseParser {
    public static ResponseData parseResponse(String response) {
        ResponseData responseData = new ResponseData();

        // Split the response into two parts: list and double value
        String[] parts = response.split("\\$");
        String listPart = parts[0];
        String doublePart = parts[1];

        // Parse the list of Long values
        List<Long> longList = new ArrayList<>();
        String[] longStrings = listPart.replaceAll("[\\[\\]]", "").split(",");
        for (String longString : longStrings) {
            longList.add(Long.parseLong(longString.trim()));
        }
        responseData.setLongList(longList);

        // Parse the double value
        double doubleValue = Double.parseDouble(doublePart);
        responseData.setDoubleValue(doubleValue);

        return responseData;
    }

    @Getter
    @Setter
    public static class ResponseData {
        private List<Long> longList;
        private double doubleValue;

    }
}
