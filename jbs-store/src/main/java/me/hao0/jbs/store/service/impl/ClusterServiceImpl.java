package me.hao0.jbs.store.service.impl;

import com.google.common.base.Objects;
import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import me.hao0.jbs.common.dto.ClientInfo;
import me.hao0.jbs.common.dto.ServerInfo;
import me.hao0.jbs.common.log.Logs;
import me.hao0.jbs.common.model.App;
import me.hao0.jbs.common.util.CollectionUtil;
import me.hao0.jbs.common.util.ZkPaths;
import me.hao0.jbs.store.dao.AppDao;
import me.hao0.jbs.store.dao.JobServerDao;
import me.hao0.jbs.store.service.ClusterService;
import me.hao0.jbs.store.support.JbsZkClient;
import me.hao0.jbs.common.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Collections;
import java.util.List;


@Service
public class ClusterServiceImpl implements ClusterService {

    @Autowired
    private AppDao appDao;

    @Autowired
    private JobServerDao jobServerDao;

    @Autowired
    private JbsZkClient zk;

    @Override
    public Response<List<ClientInfo>> listClients(Long appId) {
        try {
            App app = appDao.findById(appId);
            if (app == null){
                Logs.warn("The app(id={}) doesn't exist when list clients", appId);
                return Response.ok(Collections.<ClientInfo>emptyList());
            }

            String appClientsPath = ZkPaths.pathOfAppClients(app.getAppName());
            zk.client().mkdirs(appClientsPath);
            List<String> clients = zk.client().gets(appClientsPath);
            if(CollectionUtil.isNullOrEmpty(clients)){
                return Response.ok(Collections.<ClientInfo>emptyList());
            }

            List<ClientInfo> clientInfos = Lists.newArrayListWithExpectedSize(clients.size());
            ClientInfo clientInfo;
            for (String client : clients){
                clientInfo = new ClientInfo();
                clientInfo.setAddr(client);
                clientInfos.add(clientInfo);
            }

            return Response.ok(clientInfos);

        } catch (Exception e){
            Logs.error("failed to list clients, cause: {}", Throwables.getStackTraceAsString(e));
            return Response.notOk("client.list.failed");
        }
    }

    @Override
    public Response<List<ServerInfo>> listServers() {
        try {

            List<String> servers = zk.client().gets(ZkPaths.SERVERS);
            if (CollectionUtil.isNullOrEmpty(servers)){
                return Response.ok(Collections.<ServerInfo>emptyList());
            }

            String leader = zk.client().getString(ZkPaths.LEADER);

            List<ServerInfo> serverInfos = Lists.newArrayListWithExpectedSize(servers.size());
            ServerInfo serverInfo;
            for (String server: servers){
                serverInfo = new ServerInfo();
                if (Objects.equal(server, leader)){
                    serverInfo.setLeader(true);
                }
                serverInfo.setServer(server);
                serverInfo.setJobCount(jobServerDao.countJobsByServer(server).intValue());
                serverInfos.add(serverInfo);
            }

            return Response.ok(serverInfos);

        } catch (Exception e){
            Logs.error("failed to list servers, cause: {}", Throwables.getStackTraceAsString(e));
            return Response.notOk("server.list.failed");
        }
    }

    @Override
    public Response<List<String>> listSimpleServers() {
        try {
            List<String> servers = zk.client().gets(ZkPaths.SERVERS);
            if (CollectionUtil.isNullOrEmpty(servers)){
                return Response.ok(Collections.<String>emptyList());
            }
            return Response.ok(servers);
        } catch (Exception e){
            Logs.error("failed to list simple servers, cause: {}", Throwables.getStackTraceAsString(e));
            return Response.notOk("server.list.failed");
        }
    }
}
