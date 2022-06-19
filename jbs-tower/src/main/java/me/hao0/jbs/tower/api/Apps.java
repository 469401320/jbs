package me.hao0.jbs.tower.api;

import me.hao0.jbs.common.dto.AppDeleteDto;
import me.hao0.jbs.common.dto.AppSaveDto;
import me.hao0.jbs.common.dto.JsonResponse;
import me.hao0.jbs.common.log.Logs;
import me.hao0.jbs.common.model.App;
import me.hao0.jbs.store.service.AppService;
import me.hao0.jbs.store.util.Page;
import me.hao0.jbs.common.util.Response;
import me.hao0.jbs.tower.support.Messages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/apps")
public class Apps {

    @Autowired
    private Messages messages;

    @Autowired
    private AppService appService;


    @RequestMapping(method = RequestMethod.GET)
    public JsonResponse pagingApp(
            @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(value = "appName", defaultValue = "") String appName){

        Response<Page<App>> pagingResp = appService.pagingApp(appName, pageNo, pageSize);
        if (!pagingResp.isSuccess()){
            return JsonResponse.notOk(messages.get(pagingResp.getErr()));
        }

        return JsonResponse.ok(pagingResp.getData());
    }


    @RequestMapping(method = RequestMethod.POST)
    public JsonResponse saveApp(@RequestBody AppSaveDto appSaveDto){

        App app = new App();
        app.setAppName(appSaveDto.getAppName());
        app.setAppKey(appSaveDto.getAppKey());
        app.setAppDesc(appSaveDto.getAppDesc());

        Response<Long> saveResp = appService.save(app);
        if (!saveResp.isSuccess()){
            Logs.error("failed to save app({}), cause: {}", app, saveResp.getErr());
            return JsonResponse.notOk(saveResp.getErr());
        }



        return JsonResponse.ok(saveResp.getData());
    }


    @RequestMapping(value = "/del", method = RequestMethod.POST)
    public JsonResponse delApp(@RequestBody AppDeleteDto appDeleteDto){

        Response<Boolean> delResp = appService.delete(appDeleteDto.getAppName());
        if (!delResp.isSuccess()){
            Logs.error("failed to delete app({}), cause: {}", appDeleteDto.getAppName(), delResp.getErr());
        }

        return JsonResponse.ok(delResp.getData());
    }
}
