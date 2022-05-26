package com.sci4s.grpc;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ErrConstance {
	
	public static final String NO_ERROR       = "SUCCESS";// 성공
	public static final String NOT_EXIST_USER = "E0001";  // 미존재(계정, 사용자 등)
	public static final String NOT_EQUAL_PWD  = "E0002";  // 패스워드 불일치
	public static final String NOT_AUTH       = "E0003";  // 인증 및 권한 부재
	public static final String NOT_EXIST      = "E0009";  // 미존재(계정, 사용자 등)
	public static final String ERR_9999       = "E9999";  // 시스템에러
	public static final String NO_UNIQUE      = "E8000";  // 데이터 중복(프로그램 오류)
	public static final String REF_DATA       = "E8001";  // 참조된 데이터 또는 하위 데이터 존재(프로그램 오류)
	public static final String NO_DATA        = "NO_DATA";// 조회된 데이터가 없습니다.
	
	public static final int    GRPC_MAX_SIZE = 2147483647;
	public static final String CLANG  = "KR";
	
	public static String getPrintStackTrace(Exception e) {     
		StringWriter errors = new StringWriter();
		try {
			e.printStackTrace(new PrintWriter(errors));         
	        return errors.toString();    
		} catch(Exception ex) {
			ex.printStackTrace();
			return e.getMessage();
		} finally {
			if(errors != null) {errors = null;}
		}
        
             
    }
}
