package com.msa.grpc;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.msa.err.NotFoundBeanException;
import com.msa.grpc.SciRIO.ReqMsg;
import com.msa.grpc.SciRIO.RetMsg;
import com.msa.grpc.dto.GrpcParams;
import com.msa.grpc.dto.GrpcResp;
import com.msa.grpc.svc.TopMainProcessor;
import com.msa.grpc.utils.GrpcDataUtil;

import io.grpc.BindableService;
import io.grpc.stub.StreamObserver;

public class TopProtoMainProcessor extends MsaApiGrpc.MsaApiImplBase implements BindableService {
	
	protected Logger logger = LoggerFactory.getLogger(TopProtoMainProcessor.class);
	protected TopMainProcessor getMainProcessor() throws NotFoundBeanException { return null; }
	
	@Override
	public void callRMsg(SciRIO.Data request, StreamObserver<SciRIO.RetMsg> responseObserver) {
		GrpcParams    grpcPrms = null;
		SciRIO.RetMsg retMsg   = null;
		GrpcResp      grpcResp = null;
		try {
			grpcPrms = GrpcDataUtil.parseGrpcData(request);			
			grpcResp = getMainProcessor().callRMsg(grpcPrms);			
			retMsg   = SciRIO.RetMsg.newBuilder()
						.setResults(grpcResp.getResults())
						.setErrCode(grpcResp.getErrCode())
						.setErrMsg(grpcResp.getErrMsg())
						.build();
			
			responseObserver.onNext(retMsg);
			responseObserver.onCompleted();	
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (grpcPrms != null) { try { grpcPrms = null; } catch (Exception e1) {} }
			if (grpcResp != null) { try { grpcResp = null; } catch (Exception e1) {} }
			if (retMsg   != null) { try { retMsg   = null; } catch (Exception e1) {} }
		}
	}
	/**
	 * 서비스별  Master 데이터 싱크 서비스
	 */
	@Override
	public void getMstInfo(ReqMsg request, StreamObserver<RetMsg> responseObserver) {
		SciRIO.RetMsg retMsg = null;
		GrpcResp grpcResp = null;
		String jsonData = null;		
		Map<String,Object> params = new HashMap<String, Object>();
		String[] vals = null;
		try {
			logger.info("agentID ::: "+ request.getAgentID());
			logger.info("tblName ::: "+ request.getMsg());
			if (request.getMsg().indexOf("|") >= 0) {
				vals = request.getMsg().split("\\|");
			}			
			params.put("agentID", request.getAgentID());
			params.put("tblName", vals[0]); // 예) "tbl_custInfo" 테이블명으로 요청해야 함.
			params.put("svcKey",  vals[1]); // 
			
			grpcResp = getMainProcessor().getMstInfo(params);
			
			retMsg = SciRIO.RetMsg.newBuilder()
					.setResults(grpcResp.getResults())
					.setErrCode(grpcResp.getErrCode())
					.setErrMsg(grpcResp.getErrMsg())
					.build();
			
			responseObserver.onNext(retMsg);
			responseObserver.onCompleted();	
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (jsonData != null) { try { jsonData = null; } catch (Exception e1) {} }
			if (grpcResp != null) { try { grpcResp = null; } catch (Exception e1) {} }
			if (vals     != null) { try { vals     = null; } catch (Exception e1) {} }
			if (params   != null) { try { params   = null; } catch (Exception e1) {} }
			if (retMsg   != null) { try { retMsg   = null; } catch (Exception e1) {} }
		}
	}
}
