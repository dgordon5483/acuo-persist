package com.acuo.persist.services;

import com.acuo.persist.entity.AssetValuation;
import com.acuo.persist.entity.AssetValue;
import com.acuo.persist.entity.AssetValueRelation;
import com.acuo.persist.ids.AssetId;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import static com.acuo.common.util.ArithmeticUtils.divide;
import static java.util.stream.Collectors.toList;

@Slf4j
public class AssetValuationServiceImpl implements AssetValuationService {

    private final ValuationService valuationService;
    private final ValueService valueService;

    @Inject
    public AssetValuationServiceImpl(ValuationService valuationService, ValueService valueService) {
        this.valuationService = valuationService;
        this.valueService = valueService;
    }

    public AssetValue persist(AssetId assetId, AssetValue value) {

        AssetValuation assetValuation = valuationService.getOrCreateAssetValuationFor(assetId);
        if (assetValuation == null) {
            log.warn("unable to retrieve or create an asset valuation for the asset {}", assetId);
            return value;
        }

        deleteLatestValue(assetValuation);

        final LocalDate valuationDateTime = value.getValuationDateTime();
        createValueRelation(assetValuation, valuationDateTime, value);

        return valueService.save(value, 1);
    }

    public Collection<AssetValue> persist(List<com.acuo.common.model.results.AssetValuation> valuations) {
        return valuations.stream().map(this::persist).collect(toList());
    }

    public AssetValue persist(com.acuo.common.model.results.AssetValuation valuation) {
        final AssetId assetId = AssetId.fromString(valuation.getAssetId());
        log.info("inserting asset valuation for asset id [{}]", assetId);

        final LocalDate valuationDateTime = valuation.getValuationDateTime();
        AssetValue assetValue = createAssetValue(valuation, valuationDateTime);

        assetValue = persist(assetId, assetValue);

        log.info("valuation inserted in the db with timestamp set to {}", assetValue.getValuationDateTime());
        return assetValue;
    }

    private void createValueRelation(AssetValuation assetValuation, LocalDate valuationDateTime, AssetValue assetValue) {
        AssetValueRelation valueRelation = new AssetValueRelation();
        valueRelation.setValuation(assetValuation);
        valueRelation.setDateTime(valuationDateTime);
        valueRelation.setValue(assetValue);
        assetValue.setValuation(valueRelation);
    }

    private AssetValue createAssetValue(com.acuo.common.model.results.AssetValuation valuation, LocalDate valuationDateTime) {
        AssetValue assetValue = new AssetValue();
        assetValue.setCoupon(valuation.getCoupon());
        assetValue.setNominalCurrency(valuation.getNominalCurrency());
        final Double division = divide(valuation.getCleanMarketValue(), valuation.getNotional());
        assetValue.setUnitValue(division);
        assetValue.setPriceQuotationType(valuation.getPriceQuotationType());
        assetValue.setReportCurrency(valuation.getReportCurrency());
        assetValue.setValuationDateTime(valuationDateTime);
        assetValue.setYield(valuation.getYield());
        return assetValue;
    }

    private void deleteLatestValue(AssetValuation assetValuation) {
        final Set<AssetValueRelation> values = assetValuation.getValues();
        if (values == null) return;
        final List<AssetValue> assetValues = values
                .stream()
                .map(AssetValueRelation::getValue)
                .collect(toList());
        valueService.delete(assetValues);
        assetValuation.setValues(null);
    }
}