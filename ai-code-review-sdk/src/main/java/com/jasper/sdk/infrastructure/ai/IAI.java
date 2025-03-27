package com.jasper.sdk.infrastructure.ai;

import com.jasper.sdk.infrastructure.ai.dto.ChatResponseDTO;
import com.jasper.sdk.infrastructure.ai.dto.ChatRequestDTO;

/**
 * @author: zihong@micous.com
 * @date: 2025/3/27 11:52
 **/
public interface IAI {

    ChatResponseDTO completions(ChatRequestDTO chatRequest) throws Exception;
}
