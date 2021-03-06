// automatically generated by the FlatBuffers compiler, do not modify

package com.sci4s.fbs;

import java.nio.*;
import java.lang.*;
import java.util.*;
import com.google.flatbuffers.*;

@SuppressWarnings("unused")
public final class Login extends Table {
  public static void ValidateVersion() { Constants.FLATBUFFERS_1_11_1(); }
  public static Login getRootAsLogin(ByteBuffer _bb) { return getRootAsLogin(_bb, new Login()); }
  public static Login getRootAsLogin(ByteBuffer _bb, Login obj) { _bb.order(ByteOrder.LITTLE_ENDIAN); return (obj.__assign(_bb.getInt(_bb.position()) + _bb.position(), _bb)); }
  public void __init(int _i, ByteBuffer _bb) { __reset(_i, _bb); }
  public Login __assign(int _i, ByteBuffer _bb) { __init(_i, _bb); return this; }

  public String loginKey() { int o = __offset(4); return o != 0 ? __string(o + bb_pos) : null; }
  public ByteBuffer loginKeyAsByteBuffer() { return __vector_as_bytebuffer(4, 1); }
  public ByteBuffer loginKeyInByteBuffer(ByteBuffer _bb) { return __vector_in_bytebuffer(_bb, 4, 1); }

  public static int createLogin(FlatBufferBuilder builder,
      int loginKeyOffset) {
    builder.startTable(1);
    Login.addLoginKey(builder, loginKeyOffset);
    return Login.endLogin(builder);
  }

  public static void startLogin(FlatBufferBuilder builder) { builder.startTable(1); }
  public static void addLoginKey(FlatBufferBuilder builder, int loginKeyOffset) { builder.addOffset(0, loginKeyOffset, 0); }
  public static int endLogin(FlatBufferBuilder builder) {
    int o = builder.endTable();
    return o;
  }

  public static final class Vector extends BaseVector {
    public Vector __assign(int _vector, int _element_size, ByteBuffer _bb) { __reset(_vector, _element_size, _bb); return this; }

    public Login get(int j) { return get(new Login(), j); }
    public Login get(Login obj, int j) {  return obj.__assign(__indirect(__element(j), bb), bb); }
  }
}

