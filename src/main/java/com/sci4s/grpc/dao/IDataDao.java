package com.sci4s.grpc.dao;

import java.util.List;
import java.util.Map;

public interface IDataDao {

	/**
	 * 조회 처리
	 * @param  String sqlID
	 * @param  Map<String, Object> params
	 * @return Object
	 */
	public List<Map<String, Object>> query4Object0(String sqlID, Object params) ;
	public Object query4Object1(String sqlID, Map<String, Object> params) ;
	public Object query4Object2(String sqlID, Map<String, String> params);
	public List<Map<String, Object>> query4List1(String sqlID, Map<String, Object> params) ;
	public List<Object> query4List2(String sqlID, Map<String, Object> params) ;
	public List<Object> query4List3(String sqlID, Map<String, String> params) ;
	public Object query4List4(String sqlID, Map<String, Object> params);
	public Object query4List5(String sqlID, Map<String, String> params) ;

	public Object query4Insert1 (String sqlID, Map<String, Object> params) ;
	public Object query4Insert2 (String sqlID, Map<String, String> params) ;
	public Object query4Insert3 (String sqlID, List<Map<String, Object>> params);
	public int query4Update1 (String sqlID, Map<String, Object> params);
	public int query4Update2 (String sqlID, Map<String, String> params);
	public int query4Update3 (String sqlID, List<Map<String, Object>> params) ;
}
