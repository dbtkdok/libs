package com.sci4s.grpc.utils;

import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.Descriptors;

public class GrpcReflectUtil {
	/**
	 * 자바 클래스의 속성 값을 GRPC 메시지 객체에 복제하는 메소드
	 * 
	 * @author flacom
	 * @param  Object bean
	 * @param  Builder builder
	 * @param  String cskey
	 * @param  String errcd
	 * @param  String errmsg
	 * @return com.google.protobuf.Message
	 * @since  2008-08-29
	 */
	public static com.google.protobuf.Message copyMessageBuilder(Object bean
			, com.google.protobuf.Message.Builder builder
			, String cskey, String errcd, String errmsg) throws Exception {
		Logger logger = LoggerFactory.getLogger(GrpcReflectUtil.class);
		Descriptors.Descriptor descriptor = null;
		Method[] methods = null;
		try {
			descriptor = builder.getDescriptorForType();
			methods = bean.getClass().getDeclaredMethods();

			for (int ii = 0; ii < methods.length; ii++) {
				if (methods[ii].getName().startsWith("get")) {
					String getAll = methods[ii].getName();
					String getFirst = getAll.toLowerCase().substring(3, 4);
					String propName = getFirst + getAll.substring(4);
//					logger.info(getAll +"&&&&&&&&&&propName&&&&&&&&&" + propName);
					Object val = methods[ii].invoke(bean, null);
					
					String retType = methods[ii].getReturnType().getName().toUpperCase();
					
					// 에러설정와 CsKey 설정은 기본임.
					if ("errCode".equals(propName)) {
						val = errcd;
					}
					if ("errMsg".equals(propName)) {
						val = errmsg;
					}
					if ("csKey".equals(propName)) {
						val = cskey;
					}
					if (val == null)
						continue;
					try {
						Descriptors.FieldDescriptor fd = descriptor.findFieldByName(propName);
//						logger.info("&&&&&&&&&&&&&&&&&&&" + fd);
						if (fd != null) {
							String javaType = fd.getJavaType().toString().toUpperCase();						
//							logger.info("&&&&&&&&&&&&&&&&&&&");
//							logger.info(retType +"////"+ propName +"::::"+ javaType);
//							logger.info("&&&&&&&&&&&&&&&&&&&");						
							if ("INT".equals(javaType)) {
								builder.setField(fd, Integer.parseInt(val.toString()));
							} else if ("DOUBLE".equals(javaType)) {
								builder.setField(fd, Double.parseDouble(val.toString()));
							} else if ("FLOAT".equals(javaType)) {
								builder.setField(fd, Float.parseFloat(val.toString()));
							} else if ("INT32".equals(javaType)) {
								builder.setField(fd, Integer.parseInt(val.toString()));
							} else if ("INT64".equals(javaType)) {
								builder.setField(fd, Long.parseLong(val.toString()));
							} else if ("UINT32".equals(javaType)) {
								builder.setField(fd, Integer.parseInt(val.toString()));
							} else if ("UINT64".equals(javaType)) {
								builder.setField(fd, Long.parseLong(val.toString()));
							} else if ("SINT32".equals(javaType)) {
								builder.setField(fd, Integer.parseInt(val.toString()));
							} else if ("SINT64".equals(javaType)) {
								builder.setField(fd, Long.parseLong(val.toString()));
							} else if ("FIXED32".equals(javaType)) {
								builder.setField(fd, Integer.parseInt(val.toString()));
							} else if ("FIXED64".equals(javaType)) {
								builder.setField(fd, Long.parseLong(val.toString()));
							} else if ("SFIXED32".equals(javaType)) {
								builder.setField(fd, Integer.parseInt(val.toString()));
							} else if ("SFIXED64".equals(javaType)) {
								builder.setField(fd, Long.parseLong(val.toString()));
							} else if ("BOOL".equals(javaType)) {
								builder.setField(fd, Boolean.parseBoolean(val.toString()));
							} else if ("BYTES".equals(javaType)) {
								builder.setField(fd, val);
							} else {//STRING
								builder.setField(fd, val);
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			return builder.build();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (builder != null) {
				try { builder = null; } catch (Exception ex) {}
			}
			if (descriptor != null) {
				try { descriptor = null; } catch (Exception ex) {}
			}
			if (methods != null) {
				try { methods = null; } catch (Exception ex) {}
			}
			if (logger != null) {
				try { logger = null; } catch (Exception ex) {}
			}
		}
	}
}
