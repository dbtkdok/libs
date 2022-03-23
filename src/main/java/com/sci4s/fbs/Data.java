// automatically generated by the FlatBuffers compiler, do not modify

package com.sci4s.fbs;

import java.nio.*;
import java.lang.*;
import java.util.*;
import com.google.flatbuffers.*;

@SuppressWarnings("unused")
public final class Data extends Table {
  public static void ValidateVersion() { Constants.FLATBUFFERS_1_11_1(); }
  public static Data getRootAsData(ByteBuffer _bb) { return getRootAsData(_bb, new Data()); }
  public static Data getRootAsData(ByteBuffer _bb, Data obj) { _bb.order(ByteOrder.LITTLE_ENDIAN); return (obj.__assign(_bb.getInt(_bb.position()) + _bb.position(), _bb)); }
  public void __init(int _i, ByteBuffer _bb) { __reset(_i, _bb); }
  public Data __assign(int _i, ByteBuffer _bb) { __init(_i, _bb); return this; }

  public String pID() { int o = __offset(4); return o != 0 ? __string(o + bb_pos) : null; }
  public ByteBuffer pIDAsByteBuffer() { return __vector_as_bytebuffer(4, 1); }
  public ByteBuffer pIDInByteBuffer(ByteBuffer _bb) { return __vector_in_bytebuffer(_bb, 4, 1); }
  public String agentID() { int o = __offset(6); return o != 0 ? __string(o + bb_pos) : null; }
  public ByteBuffer agentIDAsByteBuffer() { return __vector_as_bytebuffer(6, 1); }
  public ByteBuffer agentIDInByteBuffer(ByteBuffer _bb) { return __vector_in_bytebuffer(_bb, 6, 1); }
  public String csKey() { int o = __offset(8); return o != 0 ? __string(o + bb_pos) : null; }
  public ByteBuffer csKeyAsByteBuffer() { return __vector_as_bytebuffer(8, 1); }
  public ByteBuffer csKeyInByteBuffer(ByteBuffer _bb) { return __vector_in_bytebuffer(_bb, 8, 1); }
  public String userIP() { int o = __offset(10); return o != 0 ? __string(o + bb_pos) : null; }
  public ByteBuffer userIPAsByteBuffer() { return __vector_as_bytebuffer(10, 1); }
  public ByteBuffer userIPInByteBuffer(ByteBuffer _bb) { return __vector_in_bytebuffer(_bb, 10, 1); }
  public String serverIP() { int o = __offset(12); return o != 0 ? __string(o + bb_pos) : null; }
  public ByteBuffer serverIPAsByteBuffer() { return __vector_as_bytebuffer(12, 1); }
  public ByteBuffer serverIPInByteBuffer(ByteBuffer _bb) { return __vector_in_bytebuffer(_bb, 12, 1); }
  public String userUID() { int o = __offset(14); return o != 0 ? __string(o + bb_pos) : null; }
  public ByteBuffer userUIDAsByteBuffer() { return __vector_as_bytebuffer(14, 1); }
  public ByteBuffer userUIDInByteBuffer(ByteBuffer _bb) { return __vector_in_bytebuffer(_bb, 14, 1); }
  public String borgUID() { int o = __offset(16); return o != 0 ? __string(o + bb_pos) : null; }
  public ByteBuffer borgUIDAsByteBuffer() { return __vector_as_bytebuffer(16, 1); }
  public ByteBuffer borgUIDInByteBuffer(ByteBuffer _bb) { return __vector_in_bytebuffer(_bb, 16, 1); }
  public String clang() { int o = __offset(18); return o != 0 ? __string(o + bb_pos) : null; }
  public ByteBuffer clangAsByteBuffer() { return __vector_as_bytebuffer(18, 1); }
  public ByteBuffer clangInByteBuffer(ByteBuffer _bb) { return __vector_in_bytebuffer(_bb, 18, 1); }
  public String data() { int o = __offset(20); return o != 0 ? __string(o + bb_pos) : null; }
  public ByteBuffer dataAsByteBuffer() { return __vector_as_bytebuffer(20, 1); }
  public ByteBuffer dataInByteBuffer(ByteBuffer _bb) { return __vector_in_bytebuffer(_bb, 20, 1); }
  public String errCode() { int o = __offset(22); return o != 0 ? __string(o + bb_pos) : null; }
  public ByteBuffer errCodeAsByteBuffer() { return __vector_as_bytebuffer(22, 1); }
  public ByteBuffer errCodeInByteBuffer(ByteBuffer _bb) { return __vector_in_bytebuffer(_bb, 22, 1); }
  public String errMsg() { int o = __offset(24); return o != 0 ? __string(o + bb_pos) : null; }
  public ByteBuffer errMsgAsByteBuffer() { return __vector_as_bytebuffer(24, 1); }
  public ByteBuffer errMsgInByteBuffer(ByteBuffer _bb) { return __vector_in_bytebuffer(_bb, 24, 1); }

  public static int createData(FlatBufferBuilder builder,
      int pIDOffset,
      int agentIDOffset,
      int csKeyOffset,
      int userIPOffset,
      int serverIPOffset,
      int userUIDOffset,
      int borgUIDOffset,
      int clangOffset,
      int dataOffset,
      int errCodeOffset,
      int errMsgOffset) {
    builder.startTable(11);
    Data.addErrMsg(builder, errMsgOffset);
    Data.addErrCode(builder, errCodeOffset);
    Data.addData(builder, dataOffset);
    Data.addClang(builder, clangOffset);
    Data.addBorgUID(builder, borgUIDOffset);
    Data.addUserUID(builder, userUIDOffset);
    Data.addServerIP(builder, serverIPOffset);
    Data.addUserIP(builder, userIPOffset);
    Data.addCsKey(builder, csKeyOffset);
    Data.addAgentID(builder, agentIDOffset);
    Data.addPID(builder, pIDOffset);
    return Data.endData(builder);
  }

  public static void startData(FlatBufferBuilder builder) { builder.startTable(11); }
  public static void addPID(FlatBufferBuilder builder, int pIDOffset) { builder.addOffset(0, pIDOffset, 0); }
  public static void addAgentID(FlatBufferBuilder builder, int agentIDOffset) { builder.addOffset(1, agentIDOffset, 0); }
  public static void addCsKey(FlatBufferBuilder builder, int csKeyOffset) { builder.addOffset(2, csKeyOffset, 0); }
  public static void addUserIP(FlatBufferBuilder builder, int userIPOffset) { builder.addOffset(3, userIPOffset, 0); }
  public static void addServerIP(FlatBufferBuilder builder, int serverIPOffset) { builder.addOffset(4, serverIPOffset, 0); }
  public static void addUserUID(FlatBufferBuilder builder, int userUIDOffset) { builder.addOffset(5, userUIDOffset, 0); }
  public static void addBorgUID(FlatBufferBuilder builder, int borgUIDOffset) { builder.addOffset(6, borgUIDOffset, 0); }
  public static void addClang(FlatBufferBuilder builder, int clangOffset) { builder.addOffset(7, clangOffset, 0); }
  public static void addData(FlatBufferBuilder builder, int dataOffset) { builder.addOffset(8, dataOffset, 0); }
  public static void addErrCode(FlatBufferBuilder builder, int errCodeOffset) { builder.addOffset(9, errCodeOffset, 0); }
  public static void addErrMsg(FlatBufferBuilder builder, int errMsgOffset) { builder.addOffset(10, errMsgOffset, 0); }
  public static int endData(FlatBufferBuilder builder) {
    int o = builder.endTable();
    return o;
  }

  public static final class Vector extends BaseVector {
    public Vector __assign(int _vector, int _element_size, ByteBuffer _bb) { __reset(_vector, _element_size, _bb); return this; }

    public Data get(int j) { return get(new Data(), j); }
    public Data get(Data obj, int j) {  return obj.__assign(__indirect(__element(j), bb), bb); }
  }
}

