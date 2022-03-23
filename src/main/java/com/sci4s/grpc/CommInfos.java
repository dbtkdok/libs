package com.sci4s.grpc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommInfos {
	public static final Map<String, String> appMap = new HashMap<String, String>();
	public static final List<String> commInfos = new ArrayList<String>();
	
	public static final String defMsg = "조회된 데이터가 없습니다.";
	public static final String target = "ADDRESS";//TARGET
	
	static {
		//appMap = new HashMap<String, String>();
		appMap.put("tbl_custitemsif",   "getTblCustItemsIF");
		appMap.put("tbl_itemsif",       "getTblItemsIF");
		appMap.put("tbl_gpnitemmapsif", "getTblGpnItemsIF");
		appMap.put("tbl_rolescopesif",  "getTblRoleScopesIF");
		appMap.put("tbl_rolemenuif",    "getTblRoleMenuIF");
		appMap.put("tbl_userauthif",    "getTblUserAuthIF");
		appMap.put("tbl_dictionaryif",  "getTblDictionaryIF");
		
		//commInfos = new ArrayList<String>();
		commInfos.add("agentID");
		commInfos.add("userIP");
		commInfos.add("serverIP");
		commInfos.add("userUID");
		commInfos.add("borgUID");
		commInfos.add("PID");
		commInfos.add("clang");
	}
}
