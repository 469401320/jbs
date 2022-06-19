package me.hao0.jbs.store.service;

import me.hao0.jbs.common.dto.ClientInfo;
import me.hao0.jbs.common.dto.ServerInfo;
import me.hao0.jbs.common.util.Response;
import java.util.List;


public interface ClusterService {


    Response<List<ClientInfo>> listClients(Long appId);


    Response<List<ServerInfo>> listServers();


    Response<List<String>> listSimpleServers();

}
