package com.jasper.sdk.domain.service.impl;

import com.jasper.sdk.AiCodeReview;
import com.jasper.sdk.domain.service.AbstractAiCodeReviewService;
import com.jasper.sdk.infrastructure.ai.IAI;
import com.jasper.sdk.infrastructure.ai.dto.ChatRequestDTO;
import com.jasper.sdk.infrastructure.ai.dto.ChatResponseDTO;
import com.jasper.sdk.infrastructure.git.GitCommand;
import com.jasper.sdk.utils.FeiShuUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author: zihong@micous.com
 * @date: 2025/3/27 17:17
 **/
public class AiCodeReviewService extends AbstractAiCodeReviewService {


    public AiCodeReviewService(GitCommand gitCommand, IAI iai) {
        super(gitCommand, iai);
    }

    @Override
    protected String getDiffCode() throws IOException, InterruptedException {
        return gitCommand.diff();
    }

    @Override
    protected String codeReview(String diffCode) throws Exception {
        ChatRequestDTO chatRequest = new ChatRequestDTO();
        chatRequest.setModel(System.getenv("AI_MODEL"));
        // 读取 resources 下的/prompt/code-review.md文件
        String systemContent = new BufferedReader(new InputStreamReader(Objects.requireNonNull(AiCodeReview.class.getResourceAsStream("/prompt/code-review.md"))))
                .lines().collect(Collectors.joining("\n"));
        ChatRequestDTO.Message system = ChatRequestDTO.Message.builder().role("system").content(systemContent).build();
        ChatRequestDTO.Message user = ChatRequestDTO.Message.builder().role("user").content("请对以下的代码进行评审。代码为:" + diffCode).build();
        chatRequest.setMessages(Arrays.asList(system, user));
        ChatResponseDTO completions = iai.completions(chatRequest);
        return completions.getChoices().get(0).getMessage().getContent();
    }

    @Override
    protected String recordCodeReview(String recommend) throws Exception {
        return gitCommand.commitAndPush(recommend);
    }

    @Override
    protected void pushMessage(String logUrl) throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put("项目", gitCommand.getProject());
        params.put("分支", gitCommand.getBranch());
        params.put("成员", gitCommand.getAuthor());
        params.put("提交信息", gitCommand.getMessage());
        FeiShuUtils.sendRobotCardMessage("代码评审", params);
    }
}
