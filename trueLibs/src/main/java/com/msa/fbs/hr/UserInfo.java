// automatically generated by the FlatBuffers compiler, do not modify

package com.msa.fbs.hr;

import java.nio.*;
import java.lang.*;
import java.util.*;
import com.google.flatbuffers.*;

@SuppressWarnings("unused")
public final class UserInfo extends Table {
  public static void ValidateVersion() { Constants.FLATBUFFERS_1_11_1(); }
  public static UserInfo getRootAsUserInfo(ByteBuffer _bb) { return getRootAsUserInfo(_bb, new UserInfo()); }
  public static UserInfo getRootAsUserInfo(ByteBuffer _bb, UserInfo obj) { _bb.order(ByteOrder.LITTLE_ENDIAN); return (obj.__assign(_bb.getInt(_bb.position()) + _bb.position(), _bb)); }
  public void __init(int _i, ByteBuffer _bb) { __reset(_i, _bb); }
  public UserInfo __assign(int _i, ByteBuffer _bb) { __init(_i, _bb); return this; }

  public int userID() { int o = __offset(4); return o != 0 ? bb.getInt(o + bb_pos) : 0; }
  public String loginID() { int o = __offset(6); return o != 0 ? __string(o + bb_pos) : null; }
  public ByteBuffer loginIDAsByteBuffer() { return __vector_as_bytebuffer(6, 1); }
  public ByteBuffer loginIDInByteBuffer(ByteBuffer _bb) { return __vector_in_bytebuffer(_bb, 6, 1); }
  public String pwd() { int o = __offset(8); return o != 0 ? __string(o + bb_pos) : null; }
  public ByteBuffer pwdAsByteBuffer() { return __vector_as_bytebuffer(8, 1); }
  public ByteBuffer pwdInByteBuffer(ByteBuffer _bb) { return __vector_in_bytebuffer(_bb, 8, 1); }
  public String userNM() { int o = __offset(10); return o != 0 ? __string(o + bb_pos) : null; }
  public ByteBuffer userNMAsByteBuffer() { return __vector_as_bytebuffer(10, 1); }
  public ByteBuffer userNMInByteBuffer(ByteBuffer _bb) { return __vector_in_bytebuffer(_bb, 10, 1); }
  public String userNMEng() { int o = __offset(12); return o != 0 ? __string(o + bb_pos) : null; }
  public ByteBuffer userNMEngAsByteBuffer() { return __vector_as_bytebuffer(12, 1); }
  public ByteBuffer userNMEngInByteBuffer(ByteBuffer _bb) { return __vector_in_bytebuffer(_bb, 12, 1); }
  public String empNO() { int o = __offset(14); return o != 0 ? __string(o + bb_pos) : null; }
  public ByteBuffer empNOAsByteBuffer() { return __vector_as_bytebuffer(14, 1); }
  public ByteBuffer empNOInByteBuffer(ByteBuffer _bb) { return __vector_in_bytebuffer(_bb, 14, 1); }
  public String regionCD() { int o = __offset(16); return o != 0 ? __string(o + bb_pos) : null; }
  public ByteBuffer regionCDAsByteBuffer() { return __vector_as_bytebuffer(16, 1); }
  public ByteBuffer regionCDInByteBuffer(ByteBuffer _bb) { return __vector_in_bytebuffer(_bb, 16, 1); }
  public String regionNM() { int o = __offset(18); return o != 0 ? __string(o + bb_pos) : null; }
  public ByteBuffer regionNMAsByteBuffer() { return __vector_as_bytebuffer(18, 1); }
  public ByteBuffer regionNMInByteBuffer(ByteBuffer _bb) { return __vector_in_bytebuffer(_bb, 18, 1); }
  public String zipCD() { int o = __offset(20); return o != 0 ? __string(o + bb_pos) : null; }
  public ByteBuffer zipCDAsByteBuffer() { return __vector_as_bytebuffer(20, 1); }
  public ByteBuffer zipCDInByteBuffer(ByteBuffer _bb) { return __vector_in_bytebuffer(_bb, 20, 1); }
  public String telNO() { int o = __offset(22); return o != 0 ? __string(o + bb_pos) : null; }
  public ByteBuffer telNOAsByteBuffer() { return __vector_as_bytebuffer(22, 1); }
  public ByteBuffer telNOInByteBuffer(ByteBuffer _bb) { return __vector_in_bytebuffer(_bb, 22, 1); }
  public String mobile() { int o = __offset(24); return o != 0 ? __string(o + bb_pos) : null; }
  public ByteBuffer mobileAsByteBuffer() { return __vector_as_bytebuffer(24, 1); }
  public ByteBuffer mobileInByteBuffer(ByteBuffer _bb) { return __vector_in_bytebuffer(_bb, 24, 1); }
  public String userType() { int o = __offset(26); return o != 0 ? __string(o + bb_pos) : null; }
  public ByteBuffer userTypeAsByteBuffer() { return __vector_as_bytebuffer(26, 1); }
  public ByteBuffer userTypeInByteBuffer(ByteBuffer _bb) { return __vector_in_bytebuffer(_bb, 26, 1); }
  public int isActive() { int o = __offset(28); return o != 0 ? bb.getInt(o + bb_pos) : 0; }
  public String roleID() { int o = __offset(30); return o != 0 ? __string(o + bb_pos) : null; }
  public ByteBuffer roleIDAsByteBuffer() { return __vector_as_bytebuffer(30, 1); }
  public ByteBuffer roleIDInByteBuffer(ByteBuffer _bb) { return __vector_in_bytebuffer(_bb, 30, 1); }
  public String grade() { int o = __offset(32); return o != 0 ? __string(o + bb_pos) : null; }
  public ByteBuffer gradeAsByteBuffer() { return __vector_as_bytebuffer(32, 1); }
  public ByteBuffer gradeInByteBuffer(ByteBuffer _bb) { return __vector_in_bytebuffer(_bb, 32, 1); }
  public String faxNO() { int o = __offset(34); return o != 0 ? __string(o + bb_pos) : null; }
  public ByteBuffer faxNOAsByteBuffer() { return __vector_as_bytebuffer(34, 1); }
  public ByteBuffer faxNOInByteBuffer(ByteBuffer _bb) { return __vector_in_bytebuffer(_bb, 34, 1); }
  public String totAdminYn() { int o = __offset(36); return o != 0 ? __string(o + bb_pos) : null; }
  public ByteBuffer totAdminYnAsByteBuffer() { return __vector_as_bytebuffer(36, 1); }
  public ByteBuffer totAdminYnInByteBuffer(ByteBuffer _bb) { return __vector_in_bytebuffer(_bb, 36, 1); }
  public String addr1() { int o = __offset(38); return o != 0 ? __string(o + bb_pos) : null; }
  public ByteBuffer addr1AsByteBuffer() { return __vector_as_bytebuffer(38, 1); }
  public ByteBuffer addr1InByteBuffer(ByteBuffer _bb) { return __vector_in_bytebuffer(_bb, 38, 1); }
  public String addr2() { int o = __offset(40); return o != 0 ? __string(o + bb_pos) : null; }
  public ByteBuffer addr2AsByteBuffer() { return __vector_as_bytebuffer(40, 1); }
  public ByteBuffer addr2InByteBuffer(ByteBuffer _bb) { return __vector_in_bytebuffer(_bb, 40, 1); }
  public String email() { int o = __offset(42); return o != 0 ? __string(o + bb_pos) : null; }
  public ByteBuffer emailAsByteBuffer() { return __vector_as_bytebuffer(42, 1); }
  public ByteBuffer emailInByteBuffer(ByteBuffer _bb) { return __vector_in_bytebuffer(_bb, 42, 1); }
  public String custID() { int o = __offset(44); return o != 0 ? __string(o + bb_pos) : null; }
  public ByteBuffer custIDAsByteBuffer() { return __vector_as_bytebuffer(44, 1); }
  public ByteBuffer custIDInByteBuffer(ByteBuffer _bb) { return __vector_in_bytebuffer(_bb, 44, 1); }
  public String custNM() { int o = __offset(46); return o != 0 ? __string(o + bb_pos) : null; }
  public ByteBuffer custNMAsByteBuffer() { return __vector_as_bytebuffer(46, 1); }
  public ByteBuffer custNMInByteBuffer(ByteBuffer _bb) { return __vector_in_bytebuffer(_bb, 46, 1); }
  public String obuID() { int o = __offset(48); return o != 0 ? __string(o + bb_pos) : null; }
  public ByteBuffer obuIDAsByteBuffer() { return __vector_as_bytebuffer(48, 1); }
  public ByteBuffer obuIDInByteBuffer(ByteBuffer _bb) { return __vector_in_bytebuffer(_bb, 48, 1); }
  public String obuNM() { int o = __offset(50); return o != 0 ? __string(o + bb_pos) : null; }
  public ByteBuffer obuNMAsByteBuffer() { return __vector_as_bytebuffer(50, 1); }
  public ByteBuffer obuNMInByteBuffer(ByteBuffer _bb) { return __vector_in_bytebuffer(_bb, 50, 1); }
  public int borgUID() { int o = __offset(52); return o != 0 ? bb.getInt(o + bb_pos) : 0; }
  public String borgID() { int o = __offset(54); return o != 0 ? __string(o + bb_pos) : null; }
  public ByteBuffer borgIDAsByteBuffer() { return __vector_as_bytebuffer(54, 1); }
  public ByteBuffer borgIDInByteBuffer(ByteBuffer _bb) { return __vector_in_bytebuffer(_bb, 54, 1); }
  public String borgNM() { int o = __offset(56); return o != 0 ? __string(o + bb_pos) : null; }
  public ByteBuffer borgNMAsByteBuffer() { return __vector_as_bytebuffer(56, 1); }
  public ByteBuffer borgNMInByteBuffer(ByteBuffer _bb) { return __vector_in_bytebuffer(_bb, 56, 1); }
  public String agentID() { int o = __offset(58); return o != 0 ? __string(o + bb_pos) : null; }
  public ByteBuffer agentIDAsByteBuffer() { return __vector_as_bytebuffer(58, 1); }
  public ByteBuffer agentIDInByteBuffer(ByteBuffer _bb) { return __vector_in_bytebuffer(_bb, 58, 1); }
  public String userActFile() { int o = __offset(60); return o != 0 ? __string(o + bb_pos) : null; }
  public ByteBuffer userActFileAsByteBuffer() { return __vector_as_bytebuffer(60, 1); }
  public ByteBuffer userActFileInByteBuffer(ByteBuffer _bb) { return __vector_in_bytebuffer(_bb, 60, 1); }
  public String dbSTS() { int o = __offset(62); return o != 0 ? __string(o + bb_pos) : null; }
  public ByteBuffer dbSTSAsByteBuffer() { return __vector_as_bytebuffer(62, 1); }
  public ByteBuffer dbSTSInByteBuffer(ByteBuffer _bb) { return __vector_in_bytebuffer(_bb, 62, 1); }
  public String userIP() { int o = __offset(64); return o != 0 ? __string(o + bb_pos) : null; }
  public ByteBuffer userIPAsByteBuffer() { return __vector_as_bytebuffer(64, 1); }
  public ByteBuffer userIPInByteBuffer(ByteBuffer _bb) { return __vector_in_bytebuffer(_bb, 64, 1); }
  public String csKey() { int o = __offset(66); return o != 0 ? __string(o + bb_pos) : null; }
  public ByteBuffer csKeyAsByteBuffer() { return __vector_as_bytebuffer(66, 1); }
  public ByteBuffer csKeyInByteBuffer(ByteBuffer _bb) { return __vector_in_bytebuffer(_bb, 66, 1); }
  public String pID() { int o = __offset(68); return o != 0 ? __string(o + bb_pos) : null; }
  public ByteBuffer pIDAsByteBuffer() { return __vector_as_bytebuffer(68, 1); }
  public ByteBuffer pIDInByteBuffer(ByteBuffer _bb) { return __vector_in_bytebuffer(_bb, 68, 1); }
  public String errCode() { int o = __offset(70); return o != 0 ? __string(o + bb_pos) : null; }
  public ByteBuffer errCodeAsByteBuffer() { return __vector_as_bytebuffer(70, 1); }
  public ByteBuffer errCodeInByteBuffer(ByteBuffer _bb) { return __vector_in_bytebuffer(_bb, 70, 1); }
  public String errMsg() { int o = __offset(72); return o != 0 ? __string(o + bb_pos) : null; }
  public ByteBuffer errMsgAsByteBuffer() { return __vector_as_bytebuffer(72, 1); }
  public ByteBuffer errMsgInByteBuffer(ByteBuffer _bb) { return __vector_in_bytebuffer(_bb, 72, 1); }

  public static int createUserInfo(FlatBufferBuilder builder,
      int userID,
      int loginIDOffset,
      int pwdOffset,
      int userNMOffset,
      int userNMEngOffset,
      int empNOOffset,
      int regionCDOffset,
      int regionNMOffset,
      int zipCDOffset,
      int telNOOffset,
      int mobileOffset,
      int userTypeOffset,
      int isActive,
      int roleIDOffset,
      int gradeOffset,
      int faxNOOffset,
      int totAdminYnOffset,
      int addr1Offset,
      int addr2Offset,
      int emailOffset,
      int custIDOffset,
      int custNMOffset,
      int obuIDOffset,
      int obuNMOffset,
      int borgUID,
      int borgIDOffset,
      int borgNMOffset,
      int agentIDOffset,
      int userActFileOffset,
      int dbSTSOffset,
      int userIPOffset,
      int csKeyOffset,
      int pIDOffset,
      int errCodeOffset,
      int errMsgOffset) {
    builder.startTable(35);
    UserInfo.addErrMsg(builder, errMsgOffset);
    UserInfo.addErrCode(builder, errCodeOffset);
    UserInfo.addPID(builder, pIDOffset);
    UserInfo.addCsKey(builder, csKeyOffset);
    UserInfo.addUserIP(builder, userIPOffset);
    UserInfo.addDbSTS(builder, dbSTSOffset);
    UserInfo.addUserActFile(builder, userActFileOffset);
    UserInfo.addAgentID(builder, agentIDOffset);
    UserInfo.addBorgNM(builder, borgNMOffset);
    UserInfo.addBorgID(builder, borgIDOffset);
    UserInfo.addBorgUID(builder, borgUID);
    UserInfo.addObuNM(builder, obuNMOffset);
    UserInfo.addObuID(builder, obuIDOffset);
    UserInfo.addCustNM(builder, custNMOffset);
    UserInfo.addCustID(builder, custIDOffset);
    UserInfo.addEmail(builder, emailOffset);
    UserInfo.addAddr2(builder, addr2Offset);
    UserInfo.addAddr1(builder, addr1Offset);
    UserInfo.addTotAdminYn(builder, totAdminYnOffset);
    UserInfo.addFaxNO(builder, faxNOOffset);
    UserInfo.addGrade(builder, gradeOffset);
    UserInfo.addRoleID(builder, roleIDOffset);
    UserInfo.addIsActive(builder, isActive);
    UserInfo.addUserType(builder, userTypeOffset);
    UserInfo.addMobile(builder, mobileOffset);
    UserInfo.addTelNO(builder, telNOOffset);
    UserInfo.addZipCD(builder, zipCDOffset);
    UserInfo.addRegionNM(builder, regionNMOffset);
    UserInfo.addRegionCD(builder, regionCDOffset);
    UserInfo.addEmpNO(builder, empNOOffset);
    UserInfo.addUserNMEng(builder, userNMEngOffset);
    UserInfo.addUserNM(builder, userNMOffset);
    UserInfo.addPwd(builder, pwdOffset);
    UserInfo.addLoginID(builder, loginIDOffset);
    UserInfo.addUserID(builder, userID);
    return UserInfo.endUserInfo(builder);
  }

  public static void startUserInfo(FlatBufferBuilder builder) { builder.startTable(35); }
  public static void addUserID(FlatBufferBuilder builder, int userID) { builder.addInt(0, userID, 0); }
  public static void addLoginID(FlatBufferBuilder builder, int loginIDOffset) { builder.addOffset(1, loginIDOffset, 0); }
  public static void addPwd(FlatBufferBuilder builder, int pwdOffset) { builder.addOffset(2, pwdOffset, 0); }
  public static void addUserNM(FlatBufferBuilder builder, int userNMOffset) { builder.addOffset(3, userNMOffset, 0); }
  public static void addUserNMEng(FlatBufferBuilder builder, int userNMEngOffset) { builder.addOffset(4, userNMEngOffset, 0); }
  public static void addEmpNO(FlatBufferBuilder builder, int empNOOffset) { builder.addOffset(5, empNOOffset, 0); }
  public static void addRegionCD(FlatBufferBuilder builder, int regionCDOffset) { builder.addOffset(6, regionCDOffset, 0); }
  public static void addRegionNM(FlatBufferBuilder builder, int regionNMOffset) { builder.addOffset(7, regionNMOffset, 0); }
  public static void addZipCD(FlatBufferBuilder builder, int zipCDOffset) { builder.addOffset(8, zipCDOffset, 0); }
  public static void addTelNO(FlatBufferBuilder builder, int telNOOffset) { builder.addOffset(9, telNOOffset, 0); }
  public static void addMobile(FlatBufferBuilder builder, int mobileOffset) { builder.addOffset(10, mobileOffset, 0); }
  public static void addUserType(FlatBufferBuilder builder, int userTypeOffset) { builder.addOffset(11, userTypeOffset, 0); }
  public static void addIsActive(FlatBufferBuilder builder, int isActive) { builder.addInt(12, isActive, 0); }
  public static void addRoleID(FlatBufferBuilder builder, int roleIDOffset) { builder.addOffset(13, roleIDOffset, 0); }
  public static void addGrade(FlatBufferBuilder builder, int gradeOffset) { builder.addOffset(14, gradeOffset, 0); }
  public static void addFaxNO(FlatBufferBuilder builder, int faxNOOffset) { builder.addOffset(15, faxNOOffset, 0); }
  public static void addTotAdminYn(FlatBufferBuilder builder, int totAdminYnOffset) { builder.addOffset(16, totAdminYnOffset, 0); }
  public static void addAddr1(FlatBufferBuilder builder, int addr1Offset) { builder.addOffset(17, addr1Offset, 0); }
  public static void addAddr2(FlatBufferBuilder builder, int addr2Offset) { builder.addOffset(18, addr2Offset, 0); }
  public static void addEmail(FlatBufferBuilder builder, int emailOffset) { builder.addOffset(19, emailOffset, 0); }
  public static void addCustID(FlatBufferBuilder builder, int custIDOffset) { builder.addOffset(20, custIDOffset, 0); }
  public static void addCustNM(FlatBufferBuilder builder, int custNMOffset) { builder.addOffset(21, custNMOffset, 0); }
  public static void addObuID(FlatBufferBuilder builder, int obuIDOffset) { builder.addOffset(22, obuIDOffset, 0); }
  public static void addObuNM(FlatBufferBuilder builder, int obuNMOffset) { builder.addOffset(23, obuNMOffset, 0); }
  public static void addBorgUID(FlatBufferBuilder builder, int borgUID) { builder.addInt(24, borgUID, 0); }
  public static void addBorgID(FlatBufferBuilder builder, int borgIDOffset) { builder.addOffset(25, borgIDOffset, 0); }
  public static void addBorgNM(FlatBufferBuilder builder, int borgNMOffset) { builder.addOffset(26, borgNMOffset, 0); }
  public static void addAgentID(FlatBufferBuilder builder, int agentIDOffset) { builder.addOffset(27, agentIDOffset, 0); }
  public static void addUserActFile(FlatBufferBuilder builder, int userActFileOffset) { builder.addOffset(28, userActFileOffset, 0); }
  public static void addDbSTS(FlatBufferBuilder builder, int dbSTSOffset) { builder.addOffset(29, dbSTSOffset, 0); }
  public static void addUserIP(FlatBufferBuilder builder, int userIPOffset) { builder.addOffset(30, userIPOffset, 0); }
  public static void addCsKey(FlatBufferBuilder builder, int csKeyOffset) { builder.addOffset(31, csKeyOffset, 0); }
  public static void addPID(FlatBufferBuilder builder, int pIDOffset) { builder.addOffset(32, pIDOffset, 0); }
  public static void addErrCode(FlatBufferBuilder builder, int errCodeOffset) { builder.addOffset(33, errCodeOffset, 0); }
  public static void addErrMsg(FlatBufferBuilder builder, int errMsgOffset) { builder.addOffset(34, errMsgOffset, 0); }
  public static int endUserInfo(FlatBufferBuilder builder) {
    int o = builder.endTable();
    return o;
  }

  public static final class Vector extends BaseVector {
    public Vector __assign(int _vector, int _element_size, ByteBuffer _bb) { __reset(_vector, _element_size, _bb); return this; }

    public UserInfo get(int j) { return get(new UserInfo(), j); }
    public UserInfo get(UserInfo obj, int j) {  return obj.__assign(__indirect(__element(j), bb), bb); }
  }
}

