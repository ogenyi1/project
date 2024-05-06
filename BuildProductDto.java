package ng.optisoft.rosabon.dto.request;

import lombok.*;
import ng.optisoft.rosabon.credit.model.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BuildProductDto {

    private CRPricingSPE pricingSPE;

    private CRPricingNCL pricingNCL;

    private CRPricingCashBacked pricingCashBacked;

    private CRPricingCL pricingCL;

    private CRPricingLease pricingLease;

    public static BuildProductDto toDto(
            CRPricingSPE spe, CRPricingNCL ncl,
            CRPricingCashBacked cb, CRPricingCL cl,
            CRPricingLease l){

        return BuildProductDto.builder()
                .pricingNCL(ncl)
                .pricingCL(cl)
                .pricingCashBacked(cb)
                .pricingLease(l)
                .pricingSPE(spe)
                .build();
    }

}
