package com.wsw.patrickstar.service;

import com.wsw.patrickstar.exception.TaskServiceException;
import org.springframework.mail.SimpleMailMessage;

/**
 * @Author WangSongWen
 * @Date: Created in 11:14 2021/3/23
 * @Description:
 */
public interface MailService {
    void sendMail(SimpleMailMessage simpleMailMessage) throws TaskServiceException;
}
