package com.chartboard.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.persistence.Tuple;
import jakarta.persistence.TupleElement;

public class JPAUtil {
	
	public static List<Map<String, Object>> convertTupleToMap(List<Tuple> tuples) {
	    List<Map<String, Object>> result = new ArrayList<>();
	    for (Tuple tuple : tuples) {
	        Map<String, Object> tempMap = convertTupleToMap(tuple);
	        result.add(tempMap);
	    }
	    return result;
	}
	
	public static Map<String, Object> convertTupleToMap(Tuple tuple) {
        Map<String, Object> tempMap = new HashMap<>();
        for (TupleElement<?> key : tuple.getElements()) {
            tempMap.put(key.getAlias(), tuple.get(key));
        }
	    return tempMap;
	}
	
}
