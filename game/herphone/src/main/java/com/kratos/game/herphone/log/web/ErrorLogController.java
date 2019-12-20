package com.kratos.game.herphone.log.web;

import com.kratos.game.herphone.aop.PrePermissions;
import com.kratos.game.herphone.log.domain.ErrorLog;
import com.kratos.game.herphone.log.service.ErrorLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/errorLog")
@PrePermissions(required = false)
public class ErrorLogController {
    @Autowired
    private ErrorLogService errorLogService;
    private HashSet<String> set = new HashSet<>();
    /**
     * 错误日志记录
     */
    @PostMapping
    @PrePermissions(required = false)
    public ResponseEntity<?> save(@RequestBody ErrorLog errorLog) {  	
        Assert.hasText(errorLog.getErrorContent(), "错误内容为空");
        if (set.contains(errorLog.getErrorContent())) {
        	  return new ResponseEntity<>(HttpStatus.CREATED);
		}
        if (set.size()>500) {
			set.clear();
		}
        errorLog.setId(UUID.randomUUID().toString());
        errorLog.setOccurTime(new Date());
        errorLogService.persist(errorLog);
        set.add(errorLog.getErrorContent());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
