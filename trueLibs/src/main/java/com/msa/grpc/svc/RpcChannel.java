package com.msa.grpc.svc;

import static java.lang.String.format;

import java.io.File;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLException;

import com.msa.fbs.FlatJsonGrpc;
import com.msa.grpc.ErrConstance;
import com.msa.grpc.MsaApiGrpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.netty.GrpcSslContexts;
import io.grpc.netty.NegotiationType;
import io.grpc.netty.NettyChannelBuilder;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;

public class RpcChannel {
	
	protected static final boolean IS_TRACE_LEVEL = true;
	
	protected ManagedChannel channel  = null;
	protected long TIME_OUT = 5000;
	
	protected boolean isState = true;
	
	protected FlatJsonGrpc.FlatJsonBlockingStub flatStub;
	protected MsaApiGrpc.MsaApiBlockingStub protoStub;
	
	protected String grpcAddr = null;
	protected int grpcPort = 0;
	
	protected String caPemFile;
	protected String crtPemFile;
	protected String privateKeyFile;
	
	private static SslContext getBuildSslContext(String caPemFile, String crtPemFile,
			String privateKeyFile) throws SSLException {
		SslContextBuilder builder = GrpcSslContexts.forClient();
		if (caPemFile != null) {
			builder.trustManager(new File(caPemFile));
		}
		if (crtPemFile != null && privateKeyFile != null) {
			builder.keyManager(new File(crtPemFile), new File(privateKeyFile));
		}
		return builder.build();
	}
	
	/**
	 * 
	 * @param type REQUIRE or ADDRESS or TARGET
	 * @param inx 시도횟수
	 * @return
	 * @throws SSLException
	 */
	private ManagedChannel getManagedChannel(String type, int inx) throws SSLException {
		if ("REQUIRE".equals(type)) {
			return NettyChannelBuilder.forAddress(this.grpcAddr, this.grpcPort)
	    		    .sslContext(getBuildSslContext(caPemFile,crtPemFile,privateKeyFile))
	    		    .negotiationType(NegotiationType.TLS)
	    		    .build();
		} else {			
			if ("ADDRESS".equals(type)) {
				return ManagedChannelBuilder.forAddress(this.grpcAddr, this.grpcPort)
		                .usePlaintext()
		                .directExecutor()
		                .maxInboundMessageSize(ErrConstance.GRPC_MAX_SIZE)
		                .build();


			} else {
				return ManagedChannelBuilder.forTarget(this.grpcAddr +":"+ this.grpcPort)
						.usePlaintext()
						.directExecutor()
						.maxInboundMessageSize(ErrConstance.GRPC_MAX_SIZE)
						.build();
			}
		}
	}
	
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
	
	protected void tryOpenChannel(String url, String type, int inx) throws Exception {
		this.channel = getManagedChannel(type, inx);
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
			tryOpenChannel(this.grpcAddr, type, 1);

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
	            tryOpenChannel(this.grpcAddr, type, 2);
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
