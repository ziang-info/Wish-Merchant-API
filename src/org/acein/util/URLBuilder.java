/**
 * Copyright 2015 Ziang.info, ContextLogic or its affiliates. All Rights
 * Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"). You may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.acein.util;

/**
 *
 * @author lattimore
 */
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

public class URLBuilder {

    /**
     * Build URL string from Map of params. Nested Map and Collection is also
     * supported
     *
     * @param params Map of params for constructing the URL Query String
     * @param encoding encoding type. If not set the "UTF-8" is selected by
     * default
     * @return String of type key=value&...key=value
     * @throws java.io.UnsupportedEncodingException if encoding isnot supported
     */
    public static String httpBuildQuery(Map<String, Object> params, String encoding) {
        if (isEmpty(encoding)) {
            encoding = "UTF-8";
        }
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Object> entry : params.entrySet()) {

            String name = entry.getKey();
            Object value = entry.getValue();

            if (name.equalsIgnoreCase("") 
                    || 
                    ((value instanceof String) && ((String) value).equalsIgnoreCase("")
                    )) {
                continue;
            }

            if (sb.length() > 0) {
                sb.append('&');
            }

            if (value instanceof Map) {
                List<String> baseParam = new ArrayList<String>();
                baseParam.add(name);
                String str = buildUrlFromMap(baseParam, (Map) value, encoding);
                sb.append(str);

            } else if (value instanceof Collection) {
                List<String> baseParam = new ArrayList<String>();
                baseParam.add(name);
                String str = buildUrlFromCollection(baseParam, (Collection) value, encoding);
                sb.append(str);

            } else {
                sb.append(encodeParam(name));
                sb.append("=");
                sb.append(encodeParam(value));
            }

        }
        return sb.toString();
    }

    private static String buildUrlFromMap(List<String> baseParam, Map<Object, Object> map, String encoding) {
        StringBuilder sb = new StringBuilder();
        String token;

        //Build string of first level - related with params of provided Map
        for (Map.Entry<Object, Object> entry : map.entrySet()) {

            if (sb.length() > 0) {
                sb.append('&');
            }

            String name = String.valueOf(entry.getKey());
            Object value = entry.getValue();
            if (value instanceof Map) {
                List<String> baseParam2 = new ArrayList<String>(baseParam);
                baseParam2.add(name);
                String str = buildUrlFromMap(baseParam2, (Map) value, encoding);
                sb.append(str);

            } else if (value instanceof List) {
                List<String> baseParam2 = new ArrayList<String>(baseParam);
                baseParam2.add(name);
                String str = buildUrlFromCollection(baseParam2, (List) value, encoding);
                sb.append(str);
            } else {
                token = getBaseParamString(baseParam) + "[" + name + "]=" + encodeParam(value);
                sb.append(token);
            }
        }

        return sb.toString();
    }

    private static String buildUrlFromCollection(List<String> baseParam, Collection coll, String encoding) {
        StringBuilder sb = new StringBuilder();
        String token;
        if (!(coll instanceof List)) {
            coll = new ArrayList(coll);
        }
        List arrColl = (List) coll;

        //Build string of first level - related with params of provided Map
        for (int i = 0; i < arrColl.size(); i++) {

            if (sb.length() > 0) {
                sb.append('&');
            }

            Object value = (Object) arrColl.get(i);
            if (value instanceof Map) {
                List<String> baseParam2 = new ArrayList<String>(baseParam);
                baseParam2.add(String.valueOf(i));
                String str = buildUrlFromMap(baseParam2, (Map) value, encoding);
                sb.append(str);

            } else if (value instanceof List) {
                List<String> baseParam2 = new ArrayList<String>(baseParam);
                baseParam2.add(String.valueOf(i));
                String str = buildUrlFromCollection(baseParam2, (List) value, encoding);
                sb.append(str);
            } else {
                token = getBaseParamString(baseParam) + "[" + i + "]=" + encodeParam(value);
                sb.append(token);
            }
        }

        return sb.toString();
    }

    private static String getBaseParamString(List<String> baseParam) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < baseParam.size(); i++) {
            String s = baseParam.get(i);
            if (i == 0) {
                sb.append(s);
            } else {
                sb.append("[" + s + "]");
            }
        }
        return sb.toString();
    }

    /**
     * Check if String is either empty or null
     *
     * @param str string to check
     * @return true if string is empty. Else return false
     */
    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    private static String encodeParam(Object param) {
        try {
            return URLEncoder.encode(String.valueOf(param), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return URLEncoder.encode(String.valueOf(param));
        }
    }

    /* ========================================================================= */
    /* Test functions                                                            */
    /* ========================================================================= */
    public static void main(String[] args) {
    //basicTest();
        //testWithMap();
        //testWithList();
        //testWithNestedMap();
        //testWithNestedList();
        testCompound();
    }

    private static void basicTest() {
        Map<String, Object> params = new LinkedHashMap<String, Object>();
        params.put("a", "1");
        params.put("b", "2");
        params.put("c", "3");

        System.out.println(httpBuildQuery(params, "UTF-8"));
    }

    private static void testWithMap() {
        Map<String, Object> params = new LinkedHashMap<String, Object>();
        params.put("a", "1");
        params.put("b", "2");

        Map<String, Object> cParams = new LinkedHashMap<String, Object>();
        cParams.put("c1", "c1val");
        cParams.put("c2", "c2val");
        params.put("c", cParams);

        System.out.println(httpBuildQuery(params, "UTF-8"));
    }

    private static void testWithNestedMap() {
        Map<String, Object> params = new LinkedHashMap<String, Object>();
        params.put("a", "1");
        params.put("b", "2");

        Map<String, Object> cParamsLevel1 = new LinkedHashMap<String, Object>();
        cParamsLevel1.put("cL1-1", "cLevel1-1val");
        cParamsLevel1.put("cL1-2", "cLevel1-2val");

        Map<String, Object> cParamsLevel2 = new LinkedHashMap<String, Object>();
        cParamsLevel2.put("cL2-1", "cLevel2-1val");
        cParamsLevel2.put("cL2-2", "cLevel2-2val");
        cParamsLevel1.put("cL1-3", cParamsLevel2);

        params.put("c", cParamsLevel1);

        System.out.println(httpBuildQuery(params, "UTF-8"));
    }

    private static void testWithList() {
        Map<String, Object> params = new LinkedHashMap<String, Object>();
        params.put("a", "1");
        params.put("b", "2");

        List<Object> cParams = new ArrayList<Object>();
        cParams.add("c1val");
        cParams.add("c2val");
        params.put("c", cParams);

        System.out.println(httpBuildQuery(params, "UTF-8"));
    }

    private static void testWithNestedList() {
        Map<String, Object> params = new LinkedHashMap<String, Object>();
        params.put("a", "1");
        params.put("b", "2");

        List<Object> cParamsLevel1 = new ArrayList<Object>();
        cParamsLevel1.add("cL1-val1");
        cParamsLevel1.add("cL12-val2");

        List<Object> cParamsLevel2 = new ArrayList<Object>();
        cParamsLevel2.add("cL2-val1");
        cParamsLevel2.add("cL2-val2");
        cParamsLevel1.add(cParamsLevel2);

        params.put("c", cParamsLevel1);

        System.out.println(httpBuildQuery(params, "UTF-8"));
    }

    private static void testCompound() {

        Map<String, Object> params = new LinkedHashMap<String, Object>();

        //flat
        params.put("a", "1");
        params.put("b", "2");

        //Map level 1
        Map<String, Object> cParamsLevel1 = new LinkedHashMap<String, Object>();
        cParamsLevel1.put("cL1-1", "cLevel1-1val");
        cParamsLevel1.put("cL1-2", "cLevel1-2val");

        //Map level 2
        Map<String, Object> cParamsLevel2 = new LinkedHashMap<String, Object>();
        cParamsLevel2.put("cL2-1", "cLevel2-1val");
        cParamsLevel2.put("cL2-2", "cLevel2-2val");
        cParamsLevel1.put("cL1-3", cParamsLevel2);

        params.put("c", cParamsLevel1);

        //List level 1
        List<Object> dParamsLevel1 = new ArrayList<Object>();
        dParamsLevel1.add("dL1-val1");
        dParamsLevel1.add("dL12-val2");

        //List level 2
        List<Object> dParamsLevel2 = new ArrayList<Object>();
        dParamsLevel2.add("dL2-val1");
        dParamsLevel2.add("dL2-val2");
        dParamsLevel1.add(dParamsLevel2);

        params.put("d", dParamsLevel1);

        System.out.println(httpBuildQuery(params, "UTF-8"));

    }
}
