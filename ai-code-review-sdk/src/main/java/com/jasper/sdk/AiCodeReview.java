package com.jasper.sdk;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

/**
 * @author: zihong@micous.com
 * @date: 2025/3/24 16:10
 **/
@Slf4j
public class AiCodeReview {
    public static void main(String[] args) {
        ProcessBuilder pb = new ProcessBuilder("git", "diff", "HEAD~1", "HEAD");
        pb.directory(new File("."));
        try {
            Process p = pb.start();

            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            StringBuilder sb = new StringBuilder();
            while((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
            int exitCode = p.waitFor();
            System.out.println("Exited with code:" + exitCode);
            System.out.println("评审代码:" + sb.toString());

        } catch (Exception e) {
            log.error("git diff error", e);
        }
    }
}
