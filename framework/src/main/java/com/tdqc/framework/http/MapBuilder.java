package com.tdqc.framework.http;

import java.util.HashMap;

/**
 * 可以连着写来构建map的工具。
 * 使用示例：HashMap params = new MapBuilder().put("arg1","hello").put("arg2","world").build();
 * Created by chenyen on 2016/3/4.
 */
public class MapBuilder {

	HashMap<String,String> params ;

	public MapBuilder() {
		params = new HashMap<>();
	}

	public MapBuilder put(String key, String value){
		params.put(key,value == null ? "":value);
		return this;
	}

	public HashMap<String,String> build(){
		return params;
	}

}
