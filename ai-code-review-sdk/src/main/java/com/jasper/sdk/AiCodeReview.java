package com.jasper.sdk;

import com.jasper.sdk.domain.service.impl.AiCodeReviewService;
import com.jasper.sdk.infrastructure.ai.IAI;
import com.jasper.sdk.infrastructure.ai.impl.DeepSeek;
import com.jasper.sdk.infrastructure.git.GitCommand;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: zihong@micous.com
 * @date: 2025/3/24 16:10
 **/
@Slf4j
public class AiCodeReview {
    public static void main(String[] args) {
        GitCommand gitCommand = new GitCommand(
                getEnv("REVIEW_LOG_URI"),
                getEnv("CODE_TOKEN"),
                getEnv("COMMIT_PROJECT"),
                getEnv("COMMIT_BRANCH"),
                getEnv("COMMIT_AUTHOR"),
                getEnv("COMMIT_MESSAGE")
        );

        IAI iai = new DeepSeek(
                getEnv("AI_API_HOST"),
                getEnv("AI_API_SECRET")
        );

        AiCodeReviewService aiCodeReviewService = new AiCodeReviewService(gitCommand, iai);
        aiCodeReviewService.exec();
        log.info("代码评审完成");
    }

    private static String getEnv(String key) {
        String value = System.getenv(key);
        if (value == null || value.isEmpty()) {
            throw new RuntimeException("Environment variable " + key + " is not set.");
        }
        return value;
    }


}
