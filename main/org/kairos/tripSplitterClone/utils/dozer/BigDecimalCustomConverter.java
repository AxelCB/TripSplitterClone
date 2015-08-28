package org.kairos.tripSplitterClone.utils.dozer;

import org.dozer.DozerConverter;
import org.kairos.tripSplitterClone.utils.BigDecimalWithoutTypeAdapting;

import java.math.BigDecimal;

public class BigDecimalCustomConverter extends DozerConverter<BigDecimal, BigDecimalWithoutTypeAdapting> {

	public BigDecimalCustomConverter() {
		super(BigDecimal.class, BigDecimalWithoutTypeAdapting.class);
	}

	@Override
	public BigDecimal convertFrom(BigDecimalWithoutTypeAdapting bigDecimalWithoutTypeAdapting,
			BigDecimal bigDecimal) {
		return (BigDecimal)bigDecimalWithoutTypeAdapting;
	}

	@Override
	public BigDecimalWithoutTypeAdapting convertTo(BigDecimal bigDecimal,
			BigDecimalWithoutTypeAdapting bigDecimalWithoutTypeAdapting) {
		if(bigDecimal !=null){
			return new BigDecimalWithoutTypeAdapting(bigDecimal);
		}else{
			return null; 
		}
				
	}

}
