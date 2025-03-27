package com.jasper.sdk.domain.service;

import com.jasper.sdk.infrastructure.ai.IAI;
import com.jasper.sdk.infrastructure.git.GitCommand;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * @author: zihong@micous.com
 * @date: 2025/3/27 17:11
 **/
@Slf4j
@AllArgsConstructor
public abstract class AbstractAiCodeReviewService implements IAiCodeReviewService {
    protected final GitCommand gitCommand;
    protected final IAI iai;

    @Override
    public void exec() {
        try {
            // 1、获取提交代码
            String diffCode = getDiffCode();
            // 2、开始评审代码
            String recommend = codeReview(diffCode);
            // 3、记录评审结果，返回日志地址
            String logUrl = recordCodeReview(recommend);
            // 4、发送飞书消息
            pushMessage(logUrl);
        } catch (Exception e) {
            log.error("ai-code-review error", e);
        }
    }

    protected abstract String getDiffCode() throws IOException, InterruptedException;

    protected abstract String codeReview(String diffCode) throws Exception;

    protected abstract String recordCodeReview(String recommend) throws Exception;

    protected abstract void pushMessage(String logUrl) throws Exception;
}
