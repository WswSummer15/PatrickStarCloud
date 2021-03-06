package com.wsw.patrickstar.feign.fallback;

import com.wsw.patrickstar.feign.client.RecepienterClient;
import feign.hystrix.FallbackFactory;

/**
 * @Author WangSongWen
 * @Date: Created in 15:02 2021/1/18
 * @Description:
 */
public class RecepienterFallback implements FallbackFactory<RecepienterClient> {

    @Override
    public RecepienterClient create(Throwable throwable) {
        return null;
    }
}
