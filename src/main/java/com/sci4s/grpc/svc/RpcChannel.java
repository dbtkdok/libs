package com.sci4s.grpc.svc;

import static java.lang.String.format;

import java.util.concurrent.TimeUnit;

import com.sci4s.fbs.FlatJsonGrpc;
import com.sci4s.grpc.CommInfos;
import com.sci4s.grpc.ErrConstance;
import com.sci4s.grpc.MsaApiGrpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class RpcChannel {
	
	protected static final boolean IS_TRACE_LEVEL = true;
	
	protected ManagedChannel channel  = null;
	protected long TIME_OUT = 5000;
	
	protected boolean isState = true;
	
	protected FlatJsonGrpc.FlatJsonBlockingStub flatStub;
	protected MsaApiGrpc.MsaApiBlockingStub protoStub;
	
	protected String grpcAddr = null;
	protected int grpcPort = 0;
	
	public ManagedChannel getChannel() {
		return this.channel;
	}	
	
	public Object getBlockingStub(String type) {
		if ("PROTO".equals(type)) {
			return MsaApiGrpc.newBlockingStub(this.channel);
		} else {
			return FlatJsonGrpc.newBlockingStub(this.channel);
		}		
	}
	
	protected void tryOpenChannel(String GRPC_URI, String type, int inx) throws Exception {
		if ("ADDRESS".equals(CommInfos.target)) {
			System.out.println("RpcChannel.TARGER.tryOpenChannel("+ inx +"):::::"+ GRPC_URI);
			this.channel = ManagedChannelBuilder.forAddress(this.grpcAddr, this.grpcPort)
	                .usePlaintext()
	                .directExecutor()
	                .maxInboundMessageSize(ErrConstance.GRPC_MAX_SIZE)
	                .build();


		} else {
			System.out.println("RpcChannel.ADDRESS.tryOpenChannel("+ inx +"):::::"+ GRPC_URI);
			this.channel = ManagedChannelBuilder.forTarget(GRPC_URI)
					.usePlaintext()
					.directExecutor()
					.maxInboundMessageSize(ErrConstance.GRPC_MAX_SIZE)
					.build();
		}
	}
	
	/**
	 * 
	 * @param GRPC_URI
	 * @param type      - "ADDRESS/TARGER"
	 * @return
	 * @throws Exception
	 */
	public ManagedChannel openChannel(String GRPC_URI, String type) throws Exception {
		String[] url = GRPC_URI.split("\\:");
		try {
			this.grpcAddr = url[0];
			this.grpcPort = Integer.parseInt(url[1]);
			
			System.out.println(GRPC_URI +"@@@@@@@@@@GrpcChannel.openChannel@@@@@@@@@@@"+ type);
			tryOpenChannel(GRPC_URI, type, 1);

	        if (IS_TRACE_LEVEL && this.channel != null) {
	        	System.out.println(format("[INFO] %s managed channel isTerminated: %b, isShutdown: %b, state: %s", toString(), this.channel.isTerminated(), this.channel.isShutdown(), this.channel.getState(false).name()));
	        }
	        if (this.channel == null || this.channel.isTerminated() || this.channel.isShutdown()) {
	            if (this.channel != null && this.channel.isTerminated()) {
	            	System.out.println(format("[WARN] %s managed channel was marked terminated", toString()));
	            }
	            if (this.channel != null && this.channel.isShutdown()) {
	            	System.out.println(format("[WARN] %s managed channel was marked shutdown.", toString()));
	            }
	            tryOpenChannel(GRPC_URI, type, 2);
	            if (IS_TRACE_LEVEL && this.channel != null) {
		        	System.out.println(format("[INFO] %s managed channel isTerminated: %b, isShutdown: %b, state: %s", toString(), this.channel.isTerminated(), this.channel.isShutdown(), this.channel.getState(false).name()));
		        }
	        }
			return this.channel;
		} catch (Exception e){
			System.out.println("##########[ERROR]RpcChannel."+ type +".openChannel.S###########");
			e.printStackTrace();
			System.out.println("##########[ERROR]RpcChannel."+ type +".openChannel.E###########");			
		    throw e;
		}
	}
	
	public void closeChannel() throws Exception {
		
		//this.channel.shutdown().awaitTermination(this.TIME_OUT, TimeUnit.SECONDS);
		
		if (!this.channel.isShutdown()) {
			try {
				this.channel.shutdown();
				if (!this.channel.awaitTermination(this.TIME_OUT, TimeUnit.MICROSECONDS)) {
					System.out.println(format("[WARN] Timed out gracefully shutting down connection: %s", this.channel));
				}
			} catch (Exception e) {
				Thread.currentThread().interrupt();
				e.printStackTrace();
				throw e;
			}
		}
		// Forceful shut down if still not terminated.
		if (!this.channel.isTerminated()) {
			try {
				System.out.println(format("[INFO] Timed out forcefully shutting down connection: %s", this.channel));
				this.channel.shutdownNow();
				if (!this.channel.awaitTermination(this.TIME_OUT, TimeUnit.MICROSECONDS)) {
					System.out.println(format("[WARN] Timed out forcefully shutting down connection: %s", this.channel));
				}
			} catch (Exception e) {
				Thread.currentThread().interrupt();
				System.out.println(format("[ERROR] Timed out forcefully shutting down connection: %s", e));
				throw e;
			}
		}
		/*
		try {
			this.channel.shutdown();
		    if (!this.channel.awaitTermination(this.TIME_OUT, TimeUnit.MICROSECONDS)) {
		    	this.channel.shutdownNow();
		    }
		} catch (Exception e){
			logger.error("Unexpected exception while waiting for channel shutdownNow", e);
			//throw e;
		} 
		*/
	}
	
}
