package com.theairebellion.zeus.ai.metadata.extractor;

import com.theairebellion.zeus.ai.metadata.model.Usage;

import java.util.List;

public interface UsageProvider {

    List<Usage> getUsageList(String key);

}
