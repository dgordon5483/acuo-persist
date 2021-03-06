package com.acuo.persist.services;

import com.acuo.common.model.results.AssetValuation;
import com.acuo.persist.entity.AssetValue;
import com.acuo.persist.ids.AssetId;

import java.util.Collection;
import java.util.List;

public interface AssetValuationService {

    AssetValue persist(AssetId assetId, AssetValue value);

    Collection<AssetValue> persist(List<AssetValuation> valuations);

    AssetValue persist(AssetValuation valuation);
}