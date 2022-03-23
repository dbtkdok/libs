//Generated by flatc compiler (version 1.11.0)
//If you make any local changes, they will be lost
//source: flatHRJson.fbs

package com.sci4s.fbs.hr;

import com.google.flatbuffers.grpc.FlatbuffersUtils;

import java.nio.ByteBuffer;
import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ClientCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ClientCalls.asyncClientStreamingCall;
import static io.grpc.stub.ClientCalls.asyncServerStreamingCall;
import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.blockingServerStreamingCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.stub.ServerCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ServerCalls.asyncClientStreamingCall;
import static io.grpc.stub.ServerCalls.asyncServerStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler",
    comments = "Source: flatHRJson.fbs")
public final class FlatJsonGrpc {

  private FlatJsonGrpc() {}
  
  public static final String SERVICE_NAME = "com.sci4s.fbs.hr.FlatJson";
  
  // Static method descriptors that strictly reflect the proto.
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  @java.lang.Deprecated // Use {@link #getGetLoginUserInfoMethod()} instead. 
  public static final io.grpc.MethodDescriptor<com.sci4s.fbs.Data,
      com.sci4s.fbs.hr.UserInfo> METHOD_GET_LOGIN_USER_INFO = getGetLoginUserInfoMethod();
  
  private static volatile io.grpc.MethodDescriptor<com.sci4s.fbs.Data,
      com.sci4s.fbs.hr.UserInfo> getGetLoginUserInfoMethod;
  
  private static volatile FlatbuffersUtils.FBExtactor<com.sci4s.fbs.Data> extractorOfData;
  private static FlatbuffersUtils.FBExtactor<com.sci4s.fbs.Data> getExtractorOfData() {
      if (extractorOfData != null) return extractorOfData;
      synchronized (FlatJsonGrpc.class) {
          if (extractorOfData != null) return extractorOfData;
          extractorOfData = new FlatbuffersUtils.FBExtactor<com.sci4s.fbs.Data>() {
              public com.sci4s.fbs.Data extract (ByteBuffer buffer) {
                  return com.sci4s.fbs.Data.getRootAsData(buffer);
              }
          };
          return extractorOfData;
      }
  }
  
  private static volatile FlatbuffersUtils.FBExtactor<com.sci4s.fbs.hr.UserInfo> extractorOfUserInfo;
  private static FlatbuffersUtils.FBExtactor<com.sci4s.fbs.hr.UserInfo> getExtractorOfUserInfo() {
      if (extractorOfUserInfo != null) return extractorOfUserInfo;
      synchronized (FlatJsonGrpc.class) {
          if (extractorOfUserInfo != null) return extractorOfUserInfo;
          extractorOfUserInfo = new FlatbuffersUtils.FBExtactor<com.sci4s.fbs.hr.UserInfo>() {
              public com.sci4s.fbs.hr.UserInfo extract (ByteBuffer buffer) {
                  return com.sci4s.fbs.hr.UserInfo.getRootAsUserInfo(buffer);
              }
          };
          return extractorOfUserInfo;
      }
  }
  
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static io.grpc.MethodDescriptor<com.sci4s.fbs.Data,
      com.sci4s.fbs.hr.UserInfo> getGetLoginUserInfoMethod() {
    io.grpc.MethodDescriptor<com.sci4s.fbs.Data, com.sci4s.fbs.hr.UserInfo> getGetLoginUserInfoMethod;
    if ((getGetLoginUserInfoMethod = FlatJsonGrpc.getGetLoginUserInfoMethod) == null) {
      synchronized (FlatJsonGrpc.class) {
        if ((getGetLoginUserInfoMethod = FlatJsonGrpc.getGetLoginUserInfoMethod) == null) {
          FlatJsonGrpc.getGetLoginUserInfoMethod = getGetLoginUserInfoMethod = 
              io.grpc.MethodDescriptor.<com.sci4s.fbs.Data, com.sci4s.fbs.hr.UserInfo>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "com.sci4s.fbs.hr.FlatJson", "getLoginUserInfo"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(FlatbuffersUtils.marshaller(
                  com.sci4s.fbs.Data.class, getExtractorOfData()))
              .setResponseMarshaller(FlatbuffersUtils.marshaller(
                  com.sci4s.fbs.hr.UserInfo.class, getExtractorOfUserInfo()))
                  .setSchemaDescriptor(null)
                  .build();
          }
        }
     }
     return getGetLoginUserInfoMethod;
  }
  
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  @java.lang.Deprecated // Use {@link #getLoginProcessMethod()} instead. 
  public static final io.grpc.MethodDescriptor<com.sci4s.fbs.hr.Login,
      com.sci4s.fbs.Data> METHOD_LOGIN_PROCESS = getLoginProcessMethod();
  
  private static volatile io.grpc.MethodDescriptor<com.sci4s.fbs.hr.Login,
      com.sci4s.fbs.Data> getLoginProcessMethod;
  
  private static volatile FlatbuffersUtils.FBExtactor<com.sci4s.fbs.hr.Login> extractorOfLogin;
  private static FlatbuffersUtils.FBExtactor<com.sci4s.fbs.hr.Login> getExtractorOfLogin() {
      if (extractorOfLogin != null) return extractorOfLogin;
      synchronized (FlatJsonGrpc.class) {
          if (extractorOfLogin != null) return extractorOfLogin;
          extractorOfLogin = new FlatbuffersUtils.FBExtactor<com.sci4s.fbs.hr.Login>() {
              public com.sci4s.fbs.hr.Login extract (ByteBuffer buffer) {
                  return com.sci4s.fbs.hr.Login.getRootAsLogin(buffer);
              }
          };
          return extractorOfLogin;
      }
  }
  
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static io.grpc.MethodDescriptor<com.sci4s.fbs.hr.Login,
      com.sci4s.fbs.Data> getLoginProcessMethod() {
    io.grpc.MethodDescriptor<com.sci4s.fbs.hr.Login, com.sci4s.fbs.Data> getLoginProcessMethod;
    if ((getLoginProcessMethod = FlatJsonGrpc.getLoginProcessMethod) == null) {
      synchronized (FlatJsonGrpc.class) {
        if ((getLoginProcessMethod = FlatJsonGrpc.getLoginProcessMethod) == null) {
          FlatJsonGrpc.getLoginProcessMethod = getLoginProcessMethod = 
              io.grpc.MethodDescriptor.<com.sci4s.fbs.hr.Login, com.sci4s.fbs.Data>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "com.sci4s.fbs.hr.FlatJson", "loginProcess"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(FlatbuffersUtils.marshaller(
                  com.sci4s.fbs.hr.Login.class, getExtractorOfLogin()))
              .setResponseMarshaller(FlatbuffersUtils.marshaller(
                  com.sci4s.fbs.Data.class, getExtractorOfData()))
                  .setSchemaDescriptor(null)
                  .build();
          }
        }
     }
     return getLoginProcessMethod;
  }
  
  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static FlatJsonStub newStub(io.grpc.Channel channel) {
    return new FlatJsonStub(channel);
  }
  
  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static FlatJsonBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new FlatJsonBlockingStub(channel);
  }
  
  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static FlatJsonFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new FlatJsonFutureStub(channel);
  }
  
  /**
   */
  public static abstract class FlatJsonImplBase implements io.grpc.BindableService {
    
    /**
     */
    public     void getLoginUserInfo(com.sci4s.fbs.Data request,
        io.grpc.stub.StreamObserver<com.sci4s.fbs.hr.UserInfo> responseObserver)     {
      asyncUnimplementedUnaryCall(getGetLoginUserInfoMethod(), responseObserver);
    }
    
    /**
     */
    public     void loginProcess(com.sci4s.fbs.hr.Login request,
        io.grpc.stub.StreamObserver<com.sci4s.fbs.Data> responseObserver)     {
      asyncUnimplementedUnaryCall(getLoginProcessMethod(), responseObserver);
    }
    
    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getGetLoginUserInfoMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                com.sci4s.fbs.Data,
                com.sci4s.fbs.hr.UserInfo>(
                  this, METHODID_GET_LOGIN_USER_INFO)))
          .addMethod(
            getLoginProcessMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                com.sci4s.fbs.hr.Login,
                com.sci4s.fbs.Data>(
                  this, METHODID_LOGIN_PROCESS)))
          .build();
    }
  }
  
  /**
   */
  public static final class FlatJsonStub extends io.grpc.stub.AbstractStub<FlatJsonStub> {
    private FlatJsonStub(io.grpc.Channel channel) {
      super(channel);
    }
    
    private FlatJsonStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }
    
    @java.lang.Override
    protected FlatJsonStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new FlatJsonStub(channel, callOptions);
    }
    
    /**
     */
    public     void getLoginUserInfo(com.sci4s.fbs.Data request,
        io.grpc.stub.StreamObserver<com.sci4s.fbs.hr.UserInfo> responseObserver)     {
      asyncUnaryCall(
          getChannel().newCall(getGetLoginUserInfoMethod(), getCallOptions()), request, responseObserver);
    }
    
    /**
     */
    public     void loginProcess(com.sci4s.fbs.hr.Login request,
        io.grpc.stub.StreamObserver<com.sci4s.fbs.Data> responseObserver)     {
      asyncUnaryCall(
          getChannel().newCall(getLoginProcessMethod(), getCallOptions()), request, responseObserver);
    }
  }
  
  /**
   */
  public static final class FlatJsonBlockingStub extends io.grpc.stub.AbstractStub<FlatJsonBlockingStub> {
    private FlatJsonBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }
    
    private FlatJsonBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }
    
    @java.lang.Override
    protected FlatJsonBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new FlatJsonBlockingStub(channel, callOptions);
    }
    
    /**
     */
    public     com.sci4s.fbs.hr.UserInfo getLoginUserInfo(com.sci4s.fbs.Data request)     {
      return blockingUnaryCall(
          getChannel(), getGetLoginUserInfoMethod(), getCallOptions(), request);
    }
    
    /**
     */
    public     com.sci4s.fbs.Data loginProcess(com.sci4s.fbs.hr.Login request)     {
      return blockingUnaryCall(
          getChannel(), getLoginProcessMethod(), getCallOptions(), request);
    }
  }
  
  /**
   */
  public static final class FlatJsonFutureStub extends io.grpc.stub.AbstractStub<FlatJsonFutureStub> {
    private FlatJsonFutureStub(io.grpc.Channel channel) {
      super(channel);
    }
    
    private FlatJsonFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }
    
    @java.lang.Override
    protected FlatJsonFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new FlatJsonFutureStub(channel, callOptions);
    }
    
    /**
     */
    public     com.google.common.util.concurrent.ListenableFuture<com.sci4s.fbs.hr.UserInfo> getLoginUserInfo(
        com.sci4s.fbs.Data request)     {
      return futureUnaryCall(
          getChannel().newCall(getGetLoginUserInfoMethod(), getCallOptions()), request);
    }
    
    /**
     */
    public     com.google.common.util.concurrent.ListenableFuture<com.sci4s.fbs.Data> loginProcess(
        com.sci4s.fbs.hr.Login request)     {
      return futureUnaryCall(
          getChannel().newCall(getLoginProcessMethod(), getCallOptions()), request);
    }
  }
  
  private static final int METHODID_GET_LOGIN_USER_INFO = 0;
  private static final int METHODID_LOGIN_PROCESS = 1;
  
  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final FlatJsonImplBase serviceImpl;
    private final int methodId;
  
    MethodHandlers(FlatJsonImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }
  
    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_GET_LOGIN_USER_INFO:
          serviceImpl.getLoginUserInfo((com.sci4s.fbs.Data) request,
              (io.grpc.stub.StreamObserver<com.sci4s.fbs.hr.UserInfo>) responseObserver);
          break;
        case METHODID_LOGIN_PROCESS:
          serviceImpl.loginProcess((com.sci4s.fbs.hr.Login) request,
              (io.grpc.stub.StreamObserver<com.sci4s.fbs.Data>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }
    
    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }
  
  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;
  
  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (FlatJsonGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)              
              .setSchemaDescriptor(null)              
              .addMethod(getGetLoginUserInfoMethod())              
              .addMethod(getLoginProcessMethod())              
              .build();
        }
      }
    }
    return result;
  }
}
