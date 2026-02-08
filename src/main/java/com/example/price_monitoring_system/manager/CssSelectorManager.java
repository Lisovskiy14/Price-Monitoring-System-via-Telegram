package com.example.price_monitoring_system.manager;

import com.example.price_monitoring_system.manager.exception.CssSelectorNotFoundException;
import com.example.price_monitoring_system.manager.provider.CssSelectorProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CssSelectorManager {

    private final List<CssSelectorProvider> providers;

    public String getCssSelector(String url) {
        for (CssSelectorProvider provider : providers) {
            if (provider.supports(url)) {
                String selector = provider.provide(url);
                if (selector != null) {
                    return selector;
                }
            }
        }

        throw new CssSelectorNotFoundException(url);
    }
}
